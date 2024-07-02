package network.proxy.proxy;

import network.proxy.tunnel.client.TunnelClient;
import network.proxy.tunnel.server.TunnelServer;
import org.noear.socketd.transport.client.ClientProvider;
import org.noear.socketd.transport.server.ServerConfig;

import java.util.ServiceLoader;

/**
 * @author wyl
 * @date 2024年07月02日 13:10
 */
public class NetworkProxy {

    public static NetworkProxy instance = new NetworkProxy();
    private static ProxyProvider proxyProvider;

    static {
        ServiceLoader.load(ProxyProvider.class).iterator().forEachRemaining(NetworkProxy::setProxyProvider);
    }

    static void setProxyProvider(ProxyProvider proxyProvider) {
        NetworkProxy.proxyProvider = proxyProvider;
    }

    public ProxyServer createTcpServer(ServerConfig serverConfig, TunnelServer tunnelServer) {
        return proxyProvider.createTcpServer(serverConfig, tunnelServer);
    }

    public ProxyServer createUdpServer(ServerConfig serverConfig, TunnelServer tunnelServer) {
        return proxyProvider.createUdpServer(serverConfig, tunnelServer);
    }

    public ProxyClient createTcpClient(TunnelClient tunnelClient, String lanInfo, String visitorId) {
        return proxyProvider.createTcpClient(tunnelClient, lanInfo, visitorId);
    }

}
