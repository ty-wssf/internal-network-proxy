package io.wyl.network.tunnel.server;

import org.noear.socketd.transport.server.ServerConfigHandler;

/**
 * 网络隧道服务
 *
 * @author wyl
 * @date 2024年06月24日 21:17
 */
public interface TunnelServer {

    /**
     * 服务端配置
     */
    TunnelServer config(ServerConfigHandler configHandler);

    /**
     * 启动
     */
    TunnelServer start(int port) throws Exception;

    /**
     * 停止
     */
    void stop();

}
