package network.proxy.proxy.tcp.server;

import network.proxy.proxy.ProxyServer;
import network.proxy.tunnel.server.TunnelServer;
import org.noear.socketd.transport.server.ServerConfig;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年07月02日 17:27
 */
public class TcpProxyServer implements ProxyServer {

    private ServerConfig config;
    private TunnelServer tunnelServer;

    public TcpProxyServer(ServerConfig config, TunnelServer tunnelServer) {
        this.config = config;
        this.tunnelServer = tunnelServer;
    }

    @Override
    public ServerConfig getConfig() {
        return this.config;
    }

    @Override
    public ProxyServer start() throws IOException {
        return null;
    }

    @Override
    public void stop() {

    }

}
