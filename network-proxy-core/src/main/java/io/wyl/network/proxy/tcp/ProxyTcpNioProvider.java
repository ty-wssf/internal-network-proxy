package io.wyl.network.proxy.tcp;

import org.noear.socketd.transport.client.Client;
import org.noear.socketd.transport.client.ClientConfig;
import org.noear.socketd.transport.client.ClientProvider;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.transport.server.ServerProvider;

/**
 * @author wyl
 * @date 2024年06月25日 9:27
 */
public class ProxyTcpNioProvider implements ClientProvider, ServerProvider {

    @Override
    public String[] schemas() {
        return new String[]{"sd:proxy-tcp"};
    }

    @Override
    public Server createServer(ServerConfig serverConfig) {
        return new ProxyTcpNioServer(serverConfig);
    }

    @Override
    public Client createClient(ClientConfig clientConfig) {
        return new ProxyTcpNioClient(clientConfig);
    }

}
