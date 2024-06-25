package demo.proxy;

import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.tunnel.server.TunnelServer;
import org.noear.socketd.SocketD;

/**
 * @author wyl
 * @date 2024年06月24日 21:41
 */
public class ServerDemo {

    public static void main(String[] args) throws Exception {
        SocketD.createServer("sd:proxy-tcp")
                .config(config -> config.port(18603))
                .start();
    }


}
