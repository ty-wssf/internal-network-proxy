package com.wyl.intranettunnel.tunnel.server;

import com.wyl.intranettunnel.common.TunnelConstants;
import com.wyl.intranettunnel.proxy.ProxyServer;
import com.wyl.intranettunnel.proxy.tcp.server.TcpProxyServer;
import com.wyl.intranettunnel.proxy.udp.server.UdpProxyServer;
import org.noear.socketd.SocketD;
import org.noear.socketd.cluster.LoadBalancer;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.transport.stream.RequestStream;
import org.noear.socketd.transport.stream.SendStream;
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
    private Map<String, Session> sessionAll = new ConcurrentHashMap();
    private Map<String, Set<Session>> clientSessions = new ConcurrentHashMap();
    // 本地服务端口 -> 映射信息
    private Map<Integer, PortMapping> portMappingAll = new ConcurrentHashMap();
    // 本地服务端口 -> 本地服务
    private Map<Integer, ProxyServer> proxyServerAll = new ConcurrentHashMap();
    // 客户端id -> 客户端详细信息
    private Map<String, ClientInfo> clientInfoAll = new ConcurrentHashMap();
    // 访问者id -> 访问者接收数据的回调函数
    private Map<String, Consumer<byte[]>> visitorWriteCallbackAll = new ConcurrentHashMap<>();

    public TunnelServerDefault() {
        // 数据传输指令
        doOn(TunnelConstants.TRANSFER_DATA, (s, m) -> {
            String visitorId = m.meta(TunnelConstants.VISITOR_ID);
            Consumer<byte[]> func = visitorWriteCallbackAll.get(visitorId);
            if (func != null) {
                func.accept(m.dataAsBytes());
            }
            if (m.isRequest()) {
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

        server.config(c -> c.port(port)).listen(this);

        server.start();

        return this;
    }

    @Override
    public void stop() {
        //停止
        server.stop();
    }

    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);

        String clientId = session.param(TunnelConstants.CLIENT_ID);

        // clientId不能为空
        if (StrUtils.isEmpty(clientId)) {
            session.close();
        }

        if (!clientInfoAll.containsKey(clientId)) {
            session.close();
        }

        log.info("Client channel opened, clientId={} sessionId={}", clientId, session.sessionId());

        // 添加会话、
        addClientSession(clientId, session);
    }

    @Override
    public void onClose(Session session) {
        super.onClose(session);

        String clientId = session.param(TunnelConstants.CLIENT_ID);
        log.info("Server channel closed, clientId={} sessionId={}", clientId, session.sessionId());

        // 移除会话
        sessionAll.remove(session.sessionId());
        clientSessions.get(clientId).remove(session);
    }

    private List<PortMapping> getMappingsByClientId(String clientId) {
        List<PortMapping> matchingMappings = new ArrayList<>();
        for (PortMapping mapping : portMappingAll.values()) {
            if (mapping.getClientId().equals(clientId)) {
                matchingMappings.add(mapping);
            }
        }
        return matchingMappings;
    }

    @Override
    public SendStream send(String event, Entity entity) throws IOException {
        Collection<Session> playerAll = clientSessions.get(entity.meta(TunnelConstants.CLIENT_ID));
        Session session = LoadBalancer.getAny(playerAll, entity.meta(TunnelConstants.VISITOR_ID)::hashCode);
        return session.send(event, entity);
    }

    @Override
    public RequestStream sendAndRequest(String event, Entity entity, long timeout) throws IOException {
        Collection<Session> playerAll = clientSessions.get(entity.meta(TunnelConstants.CLIENT_ID));
        Session session = LoadBalancer.getAny(playerAll, entity.meta(TunnelConstants.VISITOR_ID)::hashCode);
        log.info("sessionid：{}", session.sessionId());
        return session.sendAndRequest(event, entity, timeout);
    }

    private void addClientSession(String clientId, Session session) {
        //注册客户端会话
        Set<Session> sessions = clientSessions.computeIfAbsent(clientId, n -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        sessions.add(session);
        sessionAll.put(session.sessionId(), session);
    }

}
