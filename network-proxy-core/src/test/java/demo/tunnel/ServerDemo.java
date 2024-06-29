package demo.tunnel;

import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.tunnel.server.PortMapping;
import io.wyl.network.tunnel.server.TunnelServer;

/**
 * @author wyl
 * @date 2024年06月24日 21:41
 */
public class ServerDemo {

    public static void main(String[] args) throws Exception {
        TunnelServer tunnelServer = InternalNetworkProxy.createTunnelServer().start(18602);
        tunnelServer.addPortMapping(new PortMapping("1", 1023, "10.20.10.152", 30007, "tcp"));
        tunnelServer.addPortMapping(new PortMapping("1", 1024, "www.ahbcht.com", 9097, "http"));
    }


}
