package network.proxy.proxy;

import network.proxy.proxy.tcp.client.TcpProxyClientDefault;
import network.proxy.proxy.tcp.server.TcpProxyServer;
import network.proxy.proxy.udp.server.UdpProxyServer;
import network.proxy.tunnel.client.TunnelClient;
import network.proxy.tunnel.server.TunnelServer;
import org.noear.socketd.transport.server.ServerConfig;

/**
 * @author wyl
 * @date 2024年07月02日 13:26
 */
public class ProxyProviderImpl implements ProxyProvider {

    @Override
    public ProxyServer createTcpServer(ServerConfig serverConfig, TunnelServer tunnelServer) {
        return new TcpProxyServer(serverConfig, tunnelServer);
    }

    @Override
    public ProxyServer createUdpServer(ServerConfig serverConfig, TunnelServer tunnelServer) {
        return new UdpProxyServer(serverConfig, tunnelServer);
    }

    @Override
    public ProxyClient createTcpClient(TunnelClient tunnelClient, String lanInfo, String visitorId) {
        return new TcpProxyClientDefault(tunnelClient, lanInfo, visitorId);
    }

}
