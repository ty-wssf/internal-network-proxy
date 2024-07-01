package demo;

import com.wyl.intranettunnel.common.TunnelConstants;
import com.wyl.intranettunnel.tunnel.client.TunnelClient;
import com.wyl.intranettunnel.tunnel.client.TunnelClientDefault;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月18日 10:02
 */
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        // 启动客户端
        TunnelClient tunnelClient = new TunnelClientDefault("sd:tcp://127.0.0.1:8602?" + TunnelConstants.CLIENT_ID + "=115066186");
        tunnelClient.connect();

        // 启动客户端
       /* TunnelClient tunnelClient1 = new TunnelClientDefault("sd:tcp://127.0.0.1:8602?" + TunnelConstants.CLIENT_ID + "=1");
        tunnelClient1.connect();*/
    }

}
