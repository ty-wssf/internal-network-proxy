package io.wyl.network.tunnel.client;

import org.noear.socketd.SocketD;
import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.client.ClientSession;
import org.noear.socketd.transport.core.Asserts;
import org.noear.socketd.utils.StrUtils;

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
    //客户端名字
    private String name;

    public TunnelClientDefault(String url) {
        this.url = url;
        this.clientListener = new TunnelClientListener();
    }

    @Override
    public TunnelClient connect() throws IOException {
        String url_ = url;
        if (StrUtils.isNotEmpty(name)) {
            if (url.contains("?")) {
                url_ = url + "&@=" + name;
            } else {
                url_ = url + "?@=" + name;
            }
        }
        clientSession = SocketD.createClient(url_)
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

    @Override
    public TunnelClient nameAs(String name) {
        Asserts.assertNull("name", name);

        this.name = name;
        return this;
    }

}
