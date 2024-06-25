package demo.proxy;

import io.wyl.network.InternalNetworkProxy;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.SimpleListener;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:58
 */
public class ClientDemo1 {

    public static void main(String[] args) throws IOException {
        SocketD.createClient("sd:proxy-tcp://127.0.0.1:18603").open();
    }

}
