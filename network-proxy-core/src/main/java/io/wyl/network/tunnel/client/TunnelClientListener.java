package io.wyl.network.tunnel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.proxy.tcp.client.ProxyTcpNioClientConnector;
import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.client.ClientConfig;
import org.noear.socketd.transport.client.ClientSession;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.utils.IoConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月24日 21:54
 */
public class TunnelClientListener extends EventListener {
    Logger log = LoggerFactory.getLogger(TunnelClientListener.class);

    // 访问者id和客户端建立关联关系
    private Map<String, ChannelFuture> sessionAll = new ConcurrentHashMap<>();
    private Map<String, String> sessionAll_ = new ConcurrentHashMap<>();
    private Session session;

    public TunnelClientListener() {
        Dami.<Entity, Void>bus().listen(NetworkProxyConstants.PROXY_CLIENT_READ, new TopicListener<Payload<Entity, Void>>() {
            @Override
            public void onEvent(Payload<Entity, Void> payload) throws Throwable {
                Entity entity = payload.getContent();
                entity.putMeta(NetworkProxyConstants.VISITOR_ID, sessionAll_.get(entity.meta(NetworkProxyConstants.CLIENT_CHANNEL_ID)));
                // 客户端怎么关联访问者
                session.send(NetworkProxyConstants.PROXY_CLIENT_READ, entity);
            }
        });
        // 处理连接请求
        doOn(NetworkProxyConstants.PROXY_SERVER_CONNECT, (session, message) -> {
            System.out.println("收到连接请求");
            ClientConfig clientConfig = new ClientConfig("sd:proxy-tcp://10.20.10.152:30007");
            new ProxyTcpNioClientConnector(clientConfig).connect(new Consumer<ChannelFuture>() {
                @Override
                public void accept(ChannelFuture future) {
                    sessionAll_.put(future.channel().id().asShortText(), message.meta(NetworkProxyConstants.VISITOR_ID));
                    sessionAll.put(message.meta(NetworkProxyConstants.VISITOR_ID), future);
                    try {
                        session.reply(message, Entity.of().metaPut("ack", "1"));
                    } catch (IOException e) {
                        log.info("连接失败: {}", message.meta(NetworkProxyConstants.VISITOR_ID));
                        // throw new RuntimeException(e);
                    }
                    log.info("连接成功: {}", message.meta(NetworkProxyConstants.VISITOR_ID));
                }
            });

        });

        // 处理断开连接请求
        doOn(NetworkProxyConstants.PROXY_SERVER_DISCONNECT, (session, message) -> {
            System.out.println("收到断开连接请求");
        });

        // 处理数据请求
        doOn(NetworkProxyConstants.PROXY_SERVER_READ, (session, message) -> {
            log.info("11111");
            ChannelFuture channelFuture = sessionAll.get(message.meta(NetworkProxyConstants.VISITOR_ID));
            channelFuture.channel().writeAndFlush(Unpooled.wrappedBuffer(message.dataAsBytes()));
        });

    }

   /* @Override
    public EventListener on(IoConsumer<Session> onOpen) {
        return super.doOnOpen(onOpen);
    }*/

    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);
        this.session = session;
    }

}
