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

    String REPLY_TYPE = "reply.type";
    String ACK = "ack";

    /**
     * 读取代理服务数据主题
     */
    String PROXY_SERVER_READ = "proxy.server.read";

    String PROXY_SERVER_CONNECT = "proxy.server.connect";
    String PROXY_SERVER_DISCONNECT = "proxy.server.disconnect";
    String PROXY_SERVER_PORT = "proxy.server.port";

    String PROXY_CLIENT_READ = "proxy.client.read";

    String VISITOR_ID = "visitor.id";
    String CLIENT_CHANNEL_ID = "client.channel.id";
    String CLIENT_ID = "client.id";
    String CLIENT_PORT = "client.port";
    String CLIENT_IP = "client.ip";

}
