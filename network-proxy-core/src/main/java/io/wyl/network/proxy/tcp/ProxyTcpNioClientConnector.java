package io.wyl.network.proxy.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noear.socketd.exception.SocketDConnectionException;
import org.noear.socketd.transport.client.ClientConnectorBase;
import org.noear.socketd.transport.core.ChannelInternal;
import org.noear.socketd.transport.core.impl.ChannelDefault;
import org.noear.socketd.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月25日 12:35
 */
public class ProxyTcpNioClientConnector extends ClientConnectorBase<ProxyTcpNioClient> {

    private static final Logger log = LoggerFactory.getLogger(ProxyTcpNioClientConnector.class);
    private ChannelFuture real;
    private NioEventLoopGroup workerGroup;

    public ProxyTcpNioClientConnector(ProxyTcpNioClient client) {
        super(client);
    }

    @Override
    public ChannelInternal connect() throws IOException {
        //关闭之前的资源
        close();

        workerGroup = new NioEventLoopGroup(getConfig().getCodecThreads(), new NamedThreadFactory("nettyTcpClientWork-"));

        try {
            Bootstrap bootstrap = new Bootstrap();

            ChannelHandler handler = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                }
            };

            real = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(handler)
                    .connect(client.getConfig().getHost(),
                            client.getConfig().getPort())
                    .await();
        } catch (Throwable e) {
            close();
            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new SocketDConnectionException("Connection failed: " + client.getConfig().getLinkUrl(), e);
            }
        }

        return new ChannelDefault<>(real.channel(), client);
    }

    @Override
    public void close() throws IOException {
        try {
            if (real != null) {
                real.channel().close();
            }

            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.debug("Client connector close error", e);
            }
        }
    }

}

