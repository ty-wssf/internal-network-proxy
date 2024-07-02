package network.proxy.proxy;

import network.proxy.tunnel.client.TunnelClient;
import network.proxy.tunnel.server.TunnelServer;
import org.noear.socketd.transport.server.ServerConfig;

/**
 * @author wyl
 * @date 2024年07月02日 13:10
 */
public interface ProxyProvider {


    ProxyServer createTcpServer(ServerConfig serverConfig, TunnelServer tunnelServer);

    ProxyServer createUdpServer(ServerConfig serverConfig, TunnelServer tunnelServer);

    ProxyClient createTcpClient(TunnelClient tunnelClient, String lanInfo, String visitorId);

}
