package network.proxy.tunnel.client;

import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.client.ClientSession;

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

    ClientSession getSession();

}
