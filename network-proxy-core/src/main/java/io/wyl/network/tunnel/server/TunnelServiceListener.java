package io.wyl.network.tunnel.server;

import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.snack.ONode;
import org.noear.socketd.SocketD;
import org.noear.socketd.broker.BrokerListener;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.utils.IoConsumer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月24日 21:29
 */
public class TunnelServiceListener extends EventListener {

    protected BrokerListener brokerListener = new BrokerListener();
    // 客户端端口映射
    private Map<Integer, Server> portMappingServerAll = new ConcurrentHashMap<>();
    private Map<Integer, PortMapping> portMappingAll = new ConcurrentHashMap<>();


    public TunnelServiceListener() {
        // 处理连接请求
        Dami.<ProxyData, Boolean>bus().listen(NetworkProxyConstants.PROXY_SERVER_CONNECT, new TopicListener<Payload<ProxyData, Boolean>>() {
            @Override
            public void onEvent(Payload<ProxyData, Boolean> payload) throws Throwable {
                PortMapping portMapping = portMappingAll.get(payload.getContent().getPort());
                // 通过隧道服务发送数据到对应的代理客户端
                if (portMapping != null) {
                    brokerListener.getPlayerAny(portMapping.getClientId()).sendAndSubscribe(NetworkProxyConstants.PROXY_SERVER_CONNECT,
                                    Entity.of(ONode.serialize(payload.getContent())))
                            .thenReply(new IoConsumer<Reply>() { // 获取到返回结果
                                @Override
                                public void accept(Reply reply) throws IOException {
                                    // 连接成功
                                    if (reply.metaAsInt("ack") == 1) {
                                        payload.reply(true);
                                    } else { // 连接失败
                                        payload.reply(false);
                                    }
                                }
                            }).thenError(new Consumer<Throwable>() { // 发生医生连接失败
                                @Override
                                public void accept(Throwable throwable) {
                                    payload.reply(false);
                                }
                            });
                }
            }
        });

        // 处理访问者数据
        Dami.<ProxyData, Object>bus().listen(NetworkProxyConstants.PROXY_SERVER_READ, new TopicListener<Payload<ProxyData, Object>>() {
            @Override
            public void onEvent(Payload<ProxyData, Object> voidPayload) throws Throwable {
                // 通过隧道服务发送数据到对应的代理客户端
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
        Server server = SocketD.createServer("sd:proxy-tcp")
                .config(config -> config.port(portMapping.getServerPort()))
                .start();
        portMappingServerAll.put(portMapping.getServerPort(), server);
        portMappingAll.put(portMapping.getServerPort(), portMapping);
        return this;
    }

}
