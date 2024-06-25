package io.wyl.network.tunnel.client;

import org.noear.socketd.SocketD;
import org.noear.socketd.cluster.ClusterClientSession;
import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.client.ClientSession;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:50
 */
public class TunnelClientDefault implements TunnelClient {

    //服务端地址
    private final String url;
    //客户端会话
    private ClientSession clientSession;
    //客户端监听
    private final TunnelClientListener clientListener;
    //客户端配置
    private ClientConfigHandler clientConfigHandler;

    public TunnelClientDefault(String url) {
        this.url = url;
        this.clientListener = new TunnelClientListener();
    }

    @Override
    public TunnelClient connect() throws IOException {
        clientSession = SocketD.createClient(url)
                .config(c -> {
                    if (clientConfigHandler != null) {
                        clientConfigHandler.clientConfig(c);
                    }
                })
                .listen(clientListener)
                .open();
        return this;
    }

    @Override
    public void disconnect() throws IOException {
        clientSession.close();
    }

    @Override
    public TunnelClient config(ClientConfigHandler configHandler) {
        this.clientConfigHandler = configHandler;
        return this;
    }

}
