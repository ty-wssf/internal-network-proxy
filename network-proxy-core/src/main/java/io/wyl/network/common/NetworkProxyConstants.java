package io.wyl.network.common;

/**
 * @author wyl
 * @date 2024年06月24日 21:35
 */
public interface NetworkProxyConstants {

    /**
     * 服务端流超时默认值
     */
    long SERVER_STREAM_TIMEOUT_DEFAULT = 60 * 1000 * 5;

    /**
     * 读取代理服务数据主题
     */
    String PROXY_SERVER_READ = "proxy.server.read";

    String PROXY_SERVER_CONNECT = "proxy.server.connect";

}
