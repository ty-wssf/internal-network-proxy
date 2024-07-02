package network.proxy.proxy;

import org.noear.socketd.transport.client.ClientConfigHandler;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月17日 17:09
 */
public interface ProxyClient {

    /**
     * 连接
     */
    void connect(ClientConnectionCallback callback);

    /**
     * 断开连接
     */
    void disconnect() throws IOException;

    /**
     * 客户端配置
     */
    ProxyClient config(ClientConfigHandler configHandler);

    void send(byte[] data);

}
