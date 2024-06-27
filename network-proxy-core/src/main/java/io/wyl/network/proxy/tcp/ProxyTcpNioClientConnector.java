package io.wyl.network.proxy.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import org.noear.dami.Dami;
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
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                // 添加处理器
                                @Override
                                protected void channelRead0(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception {
                                    // 创建一个与ByteBuf长度相等的byte数组
                                    byte[] byteArray = new byte[byteBuf.readableBytes()];

                                    // 将ByteBuf的内容读取到byte数组中
                                    byteBuf.readBytes(byteArray);
                                    // Dami.bus().send(NetworkProxyConstants.PROXY_READ, new ProxyData(context.channel().id().asShortText(), byteArray));
                                }
                            });
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

