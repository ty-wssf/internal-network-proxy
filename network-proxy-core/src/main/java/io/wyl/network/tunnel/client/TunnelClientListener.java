package io.wyl.network.tunnel.client;

import io.wyl.network.common.NetworkProxyConstants;
import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.client.ClientSession;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.listener.EventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wyl
 * @date 2024年06月24日 21:54
 */
public class TunnelClientListener extends EventListener {

    // 访问者id和客户端建立关联关系
    private Map<String, ClientSession> sessionAll = new ConcurrentHashMap<>();

    public TunnelClientListener() {
        // 处理连接请求
        doOn(NetworkProxyConstants.PROXY_SERVER_CONNECT, (session, message) -> {
            System.out.println("收到连接请求");
            // ProxyData proxyData = ONode.deserialize(new String(message.dataAsBytes()));
            // 发送数据到隧道服务端
            Dami.<Entity, Void>bus().listen(NetworkProxyConstants.PROXY_CLIENT_READ, new TopicListener<Payload<Entity, Void>>() {
                @Override
                public void onEvent(Payload<Entity, Void> payload) throws Throwable {
                    session.send(NetworkProxyConstants.PROXY_CLIENT_READ, payload.getContent());
                }
            });

            ClientSession clientSession = SocketD.createClient("sd:proxy-tcp://10.20.10.152:30007").openOrThow();
            sessionAll.put(message.meta(NetworkProxyConstants.VISITOR_ID), clientSession);

            session.reply(message, Entity.of().metaPut("ack", "0"));
        });

        // 处理断开连接请求
        doOn(NetworkProxyConstants.PROXY_SERVER_DISCONNECT, (session, message) -> {
            System.out.println("收到断开连接请求");
        });

        // 处理数据请求
        doOn(NetworkProxyConstants.PROXY_SERVER_READ, (session, message) -> {
            ClientSession clientSession = sessionAll.get(message.meta(NetworkProxyConstants.VISITOR_ID));
            // clientSession.send()
        });

    }

}
