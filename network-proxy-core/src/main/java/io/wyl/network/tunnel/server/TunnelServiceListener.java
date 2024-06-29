package io.wyl.network.tunnel.server;

import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import io.wyl.network.proxy.tcp.server.ProxyTcpNioServer;
import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.snack.ONode;
import org.noear.socketd.SocketD;
import org.noear.socketd.broker.BrokerListener;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.core.listener.MessageHandler;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.utils.IoConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月24日 21:29
 */
public class TunnelServiceListener extends EventListener {
    Logger log = LoggerFactory.getLogger(TunnelServiceListener.class);

    protected BrokerListener brokerListener = new BrokerListener();
    // 客户端端口映射
    private Map<Integer, Server> portMappingServerAll = new ConcurrentHashMap<>();
    private Map<Integer, PortMapping> portMappingAll = new ConcurrentHashMap<>();
    // 访问者id和对应的回复对象
    private Map<String, Payload<Entity, Entity>> visitorReplayAll = new ConcurrentHashMap<>();


    public TunnelServiceListener() {
        // 处理连接请求
        Dami.<Entity, Entity>bus().listen(NetworkProxyConstants.PROXY_SERVER_CONNECT, new TopicListener<Payload<Entity, Entity>>() {
            @Override
            public void onEvent(Payload<Entity, Entity> payload) throws Throwable {
                visitorReplayAll.put(payload.getContent().meta(NetworkProxyConstants.VISITOR_ID), payload);
                PortMapping portMapping = portMappingAll.get(payload.getContent().metaAsInt(NetworkProxyConstants.PROXY_SERVER_PORT));
                // 通过隧道服务发送数据到对应的代理客户端
                if (portMapping != null) {
                    // 获取到返回结果
                    // 发生医生连接失败
                    brokerListener.getPlayerAny(portMapping.getClientId()).sendAndSubscribe(NetworkProxyConstants.PROXY_SERVER_CONNECT, payload.getContent()).thenReply(reply -> {
                        // 连接成功
                        if (reply.metaAsInt("ack") == 1) {
                            payload.reply(Entity.of().metaPut(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_SERVER_CONNECT).metaPut(NetworkProxyConstants.ACK, "1"));
                        } else { // 连接失败
                            payload.reply(Entity.of().metaPut(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_SERVER_CONNECT).metaPut(NetworkProxyConstants.ACK, "0"));
                        }
                    }).thenError(throwable -> {
                        payload.reply(Entity.of().metaPut(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_SERVER_CONNECT).metaPut(NetworkProxyConstants.ACK, "0"));
                    });
                } else {
                    payload.reply(Entity.of().metaPut(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_SERVER_CONNECT).metaPut(NetworkProxyConstants.ACK, "0"));
                }
            }
        });

        // 处理断开连接请求
        Dami.<Entity, Entity>bus().listen(NetworkProxyConstants.PROXY_SERVER_DISCONNECT, new TopicListener<Payload<Entity, Entity>>() {
            @Override
            public void onEvent(Payload<Entity, Entity> payload) throws Throwable {
                PortMapping portMapping = portMappingAll.get(payload.getContent().metaAsInt(NetworkProxyConstants.PROXY_SERVER_PORT));
                // 通过隧道服务发送数据到对应的代理客户端
                if (portMapping != null) {
                    // 获取到返回结果，断开连接是发送了就行，不用管结果
                    brokerListener.getPlayerAny(portMapping.getClientId()).send(NetworkProxyConstants.PROXY_SERVER_DISCONNECT, payload.getContent());
                    // 认为断开连接成功
                    payload.reply(Entity.of().metaPut(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_SERVER_DISCONNECT).metaPut(NetworkProxyConstants.ACK, "1"));
                }
            }
        });

        // 处理访问者数据
        Dami.<Entity, Entity>bus().listen(NetworkProxyConstants.PROXY_SERVER_READ, new TopicListener<Payload<Entity, Entity>>() {
            @Override
            public void onEvent(Payload<Entity, Entity> payload) throws Throwable {
                // 通过隧道服务发送数据到对应的代理客户端
                PortMapping portMapping = portMappingAll.get(payload.getContent().metaAsInt(NetworkProxyConstants.PROXY_SERVER_PORT));
                if (portMapping != null) {
                    brokerListener.getPlayerAny(portMapping.getClientId()).send(NetworkProxyConstants.PROXY_SERVER_READ, payload.getContent());
                }
            }
        });


        // 接收来真实服务端的数据
        doOn(NetworkProxyConstants.PROXY_CLIENT_READ, (session, message) -> {
            log.info("访问者收到数据:{}", message.meta(NetworkProxyConstants.VISITOR_ID));
            // 发送到对应的访问者
            Payload<Entity, Entity> visitorReplay = visitorReplayAll.get(message.meta(NetworkProxyConstants.VISITOR_ID));
            if (visitorReplay != null) {
                message.putMeta(NetworkProxyConstants.REPLY_TYPE, NetworkProxyConstants.PROXY_CLIENT_READ);
                visitorReplay.reply(message);
            }
        });

    }

    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);

        // 增加经理人支持
        brokerListener.onOpen(session);
    }

    @Override
    public void onClose(Session session) {
        super.onClose(session);

        // 增加经理人支持
        brokerListener.onClose(session);
    }

    public TunnelServiceListener addPortMapping(PortMapping portMapping) throws IOException {
        if (portMappingServerAll.get(portMapping.getServerPort()) != null) {
            throw new IllegalArgumentException("This proxty server already exists for port：" + portMapping.getServerPort());
        }
        // 创建代理服务
        ServerConfig serverConfig = new ServerConfig("sd:proxy-tcp");
        serverConfig.port(portMapping.getServerPort());
        Server server = new ProxyTcpNioServer(serverConfig).start();
        portMappingServerAll.put(portMapping.getServerPort(), server);
        portMappingAll.put(portMapping.getServerPort(), portMapping);
        return this;
    }

}
