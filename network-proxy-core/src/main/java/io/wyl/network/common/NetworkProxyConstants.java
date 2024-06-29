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
    String PROXY_SERVER_READ = "1";

    String PROXY_SERVER_CONNECT = "2";
    String PROXY_SERVER_DISCONNECT = "3";
    String PROXY_SERVER_PORT = "4";

    String PROXY_CLIENT_READ = "5";

    String VISITOR_ID = "6";
    String CLIENT_CHANNEL_ID = "7";
    String CLIENT_ID = "client.id";
    String CLIENT_PORT = "8";
    String CLIENT_IP = "9";

}
