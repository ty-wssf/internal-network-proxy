package io.wyl.network.proxy.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.wyl.network.common.NetworkProxyConstants;
import org.noear.dami.Dami;
import org.noear.socketd.exception.SocketDConnectionException;
import org.noear.socketd.transport.client.ClientConfig;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.impl.ChannelDefault;
import org.noear.socketd.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月25日 12:35
 */
public class ProxyTcpNioClientConnector implements ClientConnector {

    private static final Logger log = LoggerFactory.getLogger(ProxyTcpNioClientConnector.class);
    private ChannelFuture real;
    private NioEventLoopGroup workerGroup;
    private ClientConfig clientConfig;

    public ProxyTcpNioClientConnector(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public ChannelFuture connect(Consumer<ChannelFuture> consumer) {
        workerGroup = new NioEventLoopGroup(clientConfig.getCodecThreads(), new NamedThreadFactory("nettyTcpClientWork-"));

        Bootstrap bootstrap = new Bootstrap();

        ChannelHandler handler = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline()
                        .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                consumer.accept(real);
                                super.channelActive(ctx);
                            }

                            // 添加处理器
                            @Override
                            protected void channelRead0(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception {
                                // 创建一个与ByteBuf长度相等的byte数组
                                byte[] byteArray = new byte[byteBuf.readableBytes()];
                                // 将ByteBuf的内容读取到byte数组中
                                byteBuf.readBytes(byteArray);
                                Dami.<Entity, Void>bus().send(NetworkProxyConstants.PROXY_CLIENT_READ, Entity.of(byteArray)
                                        .metaPut(NetworkProxyConstants.CLIENT_CHANNEL_ID, context.channel().id().asShortText()));

                            }
                        });
            }
        };

        real = bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(handler)
                .connect(clientConfig.getHost(),
                        clientConfig.getPort());
        return real;

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

