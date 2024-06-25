package io.wyl.network;

import io.wyl.network.tunnel.client.TunnelClient;
import io.wyl.network.tunnel.client.TunnelClientDefault;
import io.wyl.network.tunnel.server.TunnelServer;
import io.wyl.network.tunnel.server.TunnelServerDefault;

/**
 * 内网代理
 *
 * @author wyl
 * @date 2024年06月24日 21:23
 */
public class InternalNetworkProxy {

    /**
     * 创建隧道服务端
     */
    public static TunnelServer createTunnelServer() {
        return new TunnelServerDefault();
    }

    /**
     * 创建隧道服务端
     */
    public static TunnelClient createTunnelClient(String url) {
        return new TunnelClientDefault(url);
    }

}
