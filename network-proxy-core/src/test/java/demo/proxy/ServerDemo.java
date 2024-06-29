package demo.proxy;

import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.proxy.tcp.server.ProxyTcpNioServer;
import io.wyl.network.tunnel.server.TunnelServer;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.server.ServerConfig;

/**
 * @author wyl
 * @date 2024年06月24日 21:41
 */
public class ServerDemo {

    public static void main(String[] args) throws Exception {
        ServerConfig serverConfig = new ServerConfig("");
        new ProxyTcpNioServer(serverConfig).start();
    }


}
