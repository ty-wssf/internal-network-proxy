package com.wyl.intranettunnel.tunnel.server;

import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.stream.RequestStream;
import org.noear.socketd.transport.stream.SendStream;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 通道服务
 *
 * @author wyl
 * @date 2024年02月14日 16:09
 */
public interface TunnelServer {

    /**
     * 添加本地服务端口和代理局域网信息的映射关系
     */
    TunnelServer addPortMapping(PortMapping portMapping);

    /**
     * 添加本地服务端口和代理局域网信息的映射关系
     */
    TunnelServer removeMappingByPort(Integer port);

    /**
     * 获取映射信息
     */
    PortMapping getMappingByPort(Integer port);

    TunnelServer addClientInfo(ClientInfo clientInfo);

    TunnelServer removeClientInfo(String clientId);

    ClientInfo getClientInfo(String clientId);

    TunnelServer addVisitorWriteCallback(String visitorId, Consumer<byte[]> func);

    TunnelServer removeVisitorWriteCallback(String visitorId);

    /**
     * 启动
     */
    TunnelServer start(int port) throws Exception;


    /**
     * 停止
     */
    void stop();

    SendStream send(String event, Entity entity) throws IOException;

    default RequestStream sendAndRequest(String event, Entity entity) throws IOException {
        return this.sendAndRequest(event, entity, 0L);
    }

    RequestStream sendAndRequest(String event, Entity entity, long timeout) throws IOException;

}
