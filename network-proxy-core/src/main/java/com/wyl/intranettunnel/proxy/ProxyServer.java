package com.wyl.intranettunnel.proxy;

import org.noear.socketd.transport.server.ServerConfig;

import java.io.IOException;

/**
 * 服务端
 *
 * @author wyl
 * @date 2024年02月15日 9:52
 */
public interface ProxyServer {

    /**
     * 获取配置
     */
    ServerConfig getConfig();

    /**
     * 启动
     */
    ProxyServer start() throws IOException;

    /**
     * 停止
     */
    void stop();

}
