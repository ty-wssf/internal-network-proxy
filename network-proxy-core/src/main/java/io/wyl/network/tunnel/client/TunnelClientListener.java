package io.wyl.network.tunnel.client;

import io.wyl.network.common.NetworkProxyConstants;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.core.listener.MessageHandler;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:54
 */
public class TunnelClientListener extends EventListener {

    public TunnelClientListener() {
        // 处理连接请求
        doOn(NetworkProxyConstants.PROXY_SERVER_CONNECT, (session, message) -> {

        });

        // 处理数据请求
        doOn(NetworkProxyConstants.PROXY_SERVER_READ, (session, message) -> {

        });
    }

}
