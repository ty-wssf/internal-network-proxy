package com.wyl.intranettunnel.tunnel.client;

import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.stream.RequestStream;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月15日 20:53
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

    default RequestStream sendAndRequest(String event, Entity entity) throws IOException {
        return this.sendAndRequest(event, entity, 0L);
    }

    RequestStream sendAndRequest(String event, Entity entity, long timeout) throws IOException;

}
