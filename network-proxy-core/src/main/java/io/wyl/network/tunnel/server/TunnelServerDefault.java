package io.wyl.network.tunnel.server;

import io.wyl.network.common.NetworkProxyConstants;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfigHandler;

import java.io.IOException;
import java.util.List;

/**
 * @author wyl
 * @date 2024年06月24日 21:20
 */
public class TunnelServerDefault implements TunnelServer {

    //服务端架构
    private final String serverSchema;
    //服务端监听器
    private final TunnelServiceListener serverListener;
    //服务端
    private Server server;
    //服务端配置处理
    private ServerConfigHandler serverConfigHandler;

    public TunnelServerDefault() {
        this.serverSchema = "sd:tcp";
        serverListener = new TunnelServiceListener();
    }

    @Override
    public TunnelServer config(ServerConfigHandler configHandler) {
        serverConfigHandler = configHandler;
        return this;
    }

    @Override
    public TunnelServer start(int port) throws Exception {
        //创建 SocketD 服务并配置（使用 tpc 通讯）
        server = SocketD.createServer(serverSchema);

        server.config(c -> c.serialSend(false)
                .streamTimeout(NetworkProxyConstants.SERVER_STREAM_TIMEOUT_DEFAULT)
                .ioThreads(2));

        //配置
        if (serverConfigHandler != null) {
            server.config(serverConfigHandler);
        }

        server.config(c -> c.port(port)).listen(serverListener);

        //启动
        server.start();

        return this;
    }

    @Override
    public void stop() throws IOException {
        List<PortMapping> list = serverListener.getPortMappingList();
        for (int i = 0; i < list.size(); i++) {
            serverListener.removePortMapping(list.get(i).getServerPort());
        }
        //停止
        server.stop();
    }

    @Override
    public TunnelServer addPortMapping(PortMapping portMapping) throws IOException {
        serverListener.addPortMapping(portMapping);
        return this;
    }

    @Override
    public TunnelServer removePortMapping(Integer serverPort) throws IOException {
        serverListener.removePortMapping(serverPort);
        return this;
    }

}
