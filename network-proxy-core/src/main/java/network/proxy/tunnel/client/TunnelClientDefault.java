package network.proxy.tunnel.client;

import network.proxy.common.TunnelConstants;
import network.proxy.proxy.ClientConnectionCallback;
import network.proxy.proxy.ProxyClient;
import network.proxy.proxy.tcp.client.TcpProxyClientDefault;
import network.proxy.proxy.udp.client.UdpProxyClientDefault;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.client.Client;
import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.client.ClientSession;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.transport.core.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wyl
 * @date 2024年02月15日 20:54
 */
public class TunnelClientDefault extends EventListener implements TunnelClient {

    private static final Logger log = LoggerFactory.getLogger(TunnelClientDefault.class);

    //服务端地址
    private String serverUrl;
    //客户端
    private Client client;
    //客户端会话
    private ClientSession clientSession;
    //客户端配置
    private ClientConfigHandler clientConfigHandler;
    // 访问者id -> 真实的连接客户端
    private Map<String, ProxyClient> proxyClientAll = new ConcurrentHashMap<>();

    public TunnelClientDefault(String serverUrl) {
        this.serverUrl = serverUrl;

        // 连接指令
        doOn(TunnelConstants.CONNECT, (s, m) -> {
            if (m.isRequest() || m.isSubscribe()) {
                ProxyClient client = new TcpProxyClientDefault(this, m.meta(TunnelConstants.LAN_INFO), m.meta(TunnelConstants.VISITOR_ID));
                client.connect(new ClientConnectionCallback() {
                    @Override
                    public void onConnectSuccess() throws IOException {
                        proxyClientAll.put(m.meta(TunnelConstants.VISITOR_ID), client);
                        // client
                        s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "1"));
                    }

                    @Override
                    public void onConnectFailure(Throwable e) throws IOException {
                        s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "0"));
                    }
                });
            }

        });
        // 断开连接指令
        doOn(TunnelConstants.DISCONNECT, (s, m) -> {
            ProxyClient proxyClient = proxyClientAll.get(m.meta(TunnelConstants.VISITOR_ID));
            if (proxyClient != null) {
                proxyClient.disconnect();
                proxyClientAll.remove(m.meta(TunnelConstants.VISITOR_ID));
            }
            if (m.isRequest()) {
                s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "1"));
            }
        });
        // 数据传输指令
        doOn(TunnelConstants.TRANSFER_DATA, (s, m) -> {
            String protocol = m.meta(TunnelConstants.PROTOCOL);
            if (TunnelConstants.UDP_PROTOCOL.equals(protocol)) {
                String visitorId = m.meta(TunnelConstants.VISITOR_ID);
                if (proxyClientAll.get(visitorId) == null) {
                    ProxyClient client = new UdpProxyClientDefault(this, m.meta(TunnelConstants.LAN_INFO), m.meta(TunnelConstants.VISITOR_ID));
                    client.connect(new ClientConnectionCallback() {
                        @Override
                        public void onConnectSuccess() throws IOException {
                            client.send(m.dataAsBytes());
                            proxyClientAll.put(m.meta(TunnelConstants.VISITOR_ID), client);
                            // client
                            s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "1"));
                        }

                        @Override
                        public void onConnectFailure(Throwable e) throws IOException {
                            s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "0"));
                        }
                    });
                } else {
                    proxyClientAll.get(visitorId).send(m.dataAsBytes());
                }
            } else {
                ProxyClient proxyClient = proxyClientAll.get(m.meta(TunnelConstants.VISITOR_ID));
                if (proxyClient != null) {
                    proxyClient.send(m.dataAsBytes());
                }
                if (m.isRequest()) {
                    s.reply(m, new EntityDefault().metaPut(TunnelConstants.META_ACK, "1"));
                }
            }
        });
    }

    @Override
    public TunnelClient connect() throws IOException {
        client = SocketD.createClient(this.serverUrl);

        if (clientConfigHandler != null) {
            client.config(clientConfigHandler);
        }

        clientSession = client.listen(this).openOrThow();

        return this;
    }

    @Override
    public void disconnect() throws IOException {
        if (clientSession != null) {
            clientSession.close();
        }
    }

    @Override
    public TunnelClient config(ClientConfigHandler configHandler) {
        clientConfigHandler = configHandler;
        return this;
    }

    @Override
    public ClientSession getSession() {
        return clientSession;
    }


}
