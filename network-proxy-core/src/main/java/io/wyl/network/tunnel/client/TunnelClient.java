package io.wyl.network.tunnel.client;

import org.noear.socketd.transport.client.ClientConfigHandler;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:49
 */
public interface TunnelClient {

    /**
     * 连接
     */
    TunnelClient connect() throws IOException;

    /**
     * 断开连接
     */
    void disconnect() throws IOException;

    /**
     * 客户端配置
     */
    TunnelClient config(ClientConfigHandler configHandler);

}
