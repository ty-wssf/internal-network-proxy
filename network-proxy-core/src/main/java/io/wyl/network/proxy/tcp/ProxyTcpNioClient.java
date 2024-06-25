package io.wyl.network.proxy.tcp;

import org.noear.socketd.transport.client.*;
import org.noear.socketd.transport.core.Constants;
import org.noear.socketd.transport.netty.tcp.TcpNioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wyl
 * @date 2024年06月25日 12:32
 */
public class ProxyTcpNioClient extends TcpNioClient {

    private static final Logger log = LoggerFactory.getLogger(ClientBase.class);

    public ProxyTcpNioClient(ClientConfig config) {
        super(config);
    }

    protected ClientConnector createConnector() {
        return new ProxyTcpNioClientConnector(this);
    }

    /*@Override
    public ClientSession open() {
        ClientConnector connector = createConnector();
        ClientChannel clientChannel = new ClientChannel(this, connector);
        log.info("Socket.D client successfully connected: {link={}}", getConfig().getLinkUrl());
        return clientChannel.getSession();
    }

    protected ClientConnector createConnector() {
        return new ProxyTcpNioClientConnector(this);
    }*/

}
