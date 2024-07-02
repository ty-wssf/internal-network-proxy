package network.proxy.proxy.udp.client;

import network.proxy.proxy.ClientConnectionCallback;
import network.proxy.proxy.ProxyClient;
import network.proxy.tunnel.client.TunnelClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.noear.socketd.transport.client.ClientConfig;
import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月18日 20:30
 */
public class UdpProxyClientDefault implements ProxyClient {

    private static final Logger log = LoggerFactory.getLogger(UdpProxyClientDefault.class);

    private ChannelFuture real;
    private NioEventLoopGroup workerGroup;
    private ClientConfig config;
    private TunnelClient tunnelClient;
    private String visitorId;
    private String lanInfo;

    public UdpProxyClientDefault(TunnelClient tunnelClient, String lanInfo, String visitorId) {
        this.tunnelClient = tunnelClient;
        this.visitorId = visitorId;
        this.lanInfo = lanInfo;
    }

    @Override
    public void connect(ClientConnectionCallback callback) {
        // 关闭之前的资源
        disconnect();

        workerGroup = new NioEventLoopGroup(2, new NamedThreadFactory("proxyTcpClientWork-"));

        Bootstrap bootstrap = new Bootstrap();
        real = bootstrap
                .group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(new UdpProxyServerChannelHandler(tunnelClient, visitorId)).connect(lanInfo.split(":")[0], Integer.parseInt(lanInfo.split(":")[1]));
        // 添加连接回调
        real.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                callback.onConnectSuccess();
            } else {
                callback.onConnectFailure(future.cause());
            }
        });
    }

    @Override
    public void disconnect() {
        if (real != null) {
            real.channel().close();
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public ProxyClient config(ClientConfigHandler configHandler) {
        return null;
    }

    @Override
    public void send(byte[] data) {
        if (real.channel().isActive()) {
            log.debug("访问者 {} 代理通道发送数据", visitorId);
            real.channel().writeAndFlush(Unpooled.wrappedBuffer(data));
            log.debug("访问者 {} 代理通道发送数据成功", visitorId);
        } else {
            connect(new ClientConnectionCallback() {
                @Override
                public void onConnectSuccess() throws IOException {
                    log.debug("访问者 {} 代理通道发送数据", visitorId);
                    real.channel().writeAndFlush(Unpooled.wrappedBuffer(data));
                    log.debug("访问者 {} 代理通道发送数据成功", visitorId);
                }

                @Override
                public void onConnectFailure(Throwable e) throws IOException {
                    log.warn("访问者 {} 代理客户端连接服务器失败", visitorId);
                }
            });
        }

    }

    public ClientConfig getConfig() {
        return config;
    }

}
