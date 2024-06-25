package demo.tunnel;

import io.wyl.network.InternalNetworkProxy;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:58
 */
public class ClientDemo1 {

    public static void main(String[] args) throws IOException {
        InternalNetworkProxy.createTunnelClient("sd:tcp://127.0.0.1:18602").connect();
    }

}
