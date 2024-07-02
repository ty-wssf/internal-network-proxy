package network.proxy.tunnel.server;

import network.proxy.common.TunnelConstants;
import network.proxy.proxy.ProxyServer;
import network.proxy.proxy.tcp.server.TcpProxyServer;
import network.proxy.proxy.udp.server.UdpProxyServer;
import org.noear.socketd.SocketD;
import org.noear.socketd.broker.BrokerListener;
import org.noear.socketd.cluster.LoadBalancer;
import org.noear.socketd.transport.core.Asserts;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 通道服务默认实现
 *
 * @author wyl
 * @date 2024年02月14日 16:14
 */
public class TunnelServerDefault extends EventListener implements TunnelServer {
    private static final Logger log = LoggerFactory.getLogger(TunnelServerDefault.class);
    //服务端
    private Server server;
    protected final BrokerListener brokerListener = new BrokerListener(); //为 rpc 服务的
    // 本地服务端口 -> 映射信息
    private final Map<Integer, PortMapping> portMappingAll = new ConcurrentHashMap();
    // 本地服务端口 -> 本地服务
    private final Map<Integer, ProxyServer> proxyServerAll = new ConcurrentHashMap();
    // 客户端id -> 客户端详细信息
    private final Map<String, ClientInfo> clientInfoAll = new ConcurrentHashMap();
    // 访问者id -> 访问者接收数据的回调函数
    private final Map<String, Consumer<byte[]>> visitorWriteCallbackAll = new ConcurrentHashMap<>();

    public TunnelServerDefault() {
        // 数据传输指令
        doOn(TunnelConstants.TRANSFER_DATA, (s, m) -> {
            String visitorId = m.meta(TunnelConstants.VISITOR_ID);
            Consumer<byte[]> func = visitorWriteCallbackAll.get(visitorId);
            if (func != null) {
                func.accept(m.dataAsBytes());
            }
            if (m.isRequest() || m.isSubscribe()) {
                s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "1"));
            }
        });
    }

    @Override
    public TunnelServer addPortMapping(PortMapping portMapping) {
        portMappingAll.put(portMapping.getServerPort(), portMapping);

        proxyServerAll.computeIfAbsent(portMapping.getServerPort(), key -> {
            ProxyServer server;
            if (TunnelConstants.UDP_PROTOCOL.equals(portMapping.getProtocol())) {
                ServerConfig config = new ServerConfig("sd:udp").port(portMapping.getServerPort());
                server = new UdpProxyServer(config, this);
            } else {
                ServerConfig config = new ServerConfig("sd:tcp").port(portMapping.getServerPort());
                server = new TcpProxyServer(config, this);
            }

            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return server;
        });
        return this;
    }

    @Override
    public TunnelServer removeMappingByPort(Integer port) {
        portMappingAll.remove(port);

        proxyServerAll.get(port).stop();
        proxyServerAll.remove(port);
        return this;
    }

    @Override
    public PortMapping getMappingByPort(Integer port) {
        return portMappingAll.get(port);
    }

    @Override
    public TunnelServer addClientInfo(ClientInfo clientInfo) {
        clientInfoAll.put(clientInfo.getClientId(), clientInfo);
        return this;
    }

    @Override
    public TunnelServer removeClientInfo(String clientId) {
        clientInfoAll.remove(clientId);
        return this;
    }

    @Override
    public ClientInfo getClientInfo(String clientId) {
        return clientInfoAll.get(clientId);
    }

    @Override
    public TunnelServer addVisitorWriteCallback(String visitorId, Consumer<byte[]> func) {
        visitorWriteCallbackAll.put(visitorId, func);
        return this;
    }

    @Override
    public TunnelServer removeVisitorWriteCallback(String visitorId) {
        visitorWriteCallbackAll.remove(visitorId);
        return this;
    }

    @Override
    public TunnelServer start(int port) throws Exception {
        //创建 SocketD 服务并配置（使用 tpc 通讯）
        server = SocketD.createServer("sd:tcp");

        server.config(c -> c.port(port).workThreads(1)).listen(this);

        server.start();

        return this;
    }

    @Override
    public void stop() {
        //停止隧道服务
        if (server != null) {
            server.stop();
        }
        //停止所有代理服务
        proxyServerAll.forEach((serverPort, proxyServer) -> {
            proxyServer.stop();
        });
    }

    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);

        String clientId = session.name();

        // clientId不能为空
        if (StrUtils.isEmpty(clientId)) {
            session.close();
            Asserts.assertNull("tunnel client name", clientId);
        }

        if (!clientInfoAll.containsKey(clientId)) {
            session.close();
            log.warn("tunnel client not exist, clientId={} sessionId={}", clientId, session.sessionId());
        } else {
            log.info("tunnel client opend, clientId={} sessionId={}", clientId, session.sessionId());

            // 添加会话、
            brokerListener.onOpen(session);
        }
    }

    @Override
    public void onClose(Session session) {
        super.onClose(session);

        String clientId = session.param(TunnelConstants.CLIENT_ID);
        log.info("tunnel client closed, clientId={} sessionId={}", clientId, session.sessionId());

        // 移除会话
        brokerListener.onClose(session);
    }

    @Override
    public Session getSession(Entity entity) {
        return LoadBalancer.getAnyByHash(brokerListener.getPlayerAll(entity.meta(TunnelConstants.CLIENT_ID)),
                entity.meta(TunnelConstants.VISITOR_ID));
    }

}
