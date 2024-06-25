package demo;

import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.tunnel.server.TunnelServer;

/**
 * @author wyl
 * @date 2024年06月24日 21:41
 */
public class ServerDemo {

    public static void main(String[] args) throws Exception {
        TunnelServer tunnelServer = InternalNetworkProxy.createTunnelServer().start(18602);
    }


}
