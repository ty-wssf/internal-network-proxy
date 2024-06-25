package io.wyl.network.proxy.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import org.noear.dami.Dami;
import org.noear.socketd.SocketD;
import org.noear.socketd.exception.SocketDException;
import org.noear.socketd.transport.core.Listener;
import org.noear.socketd.transport.netty.tcp.TcpNioServer;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.transport.server.ServerConfigHandler;
import org.noear.socketd.utils.NamedThreadFactory;
import org.noear.socketd.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月25日 9:10
 */
public class ProxyTcpNioServer implements Server {
    private static final Logger log = LoggerFactory.getLogger(TcpNioServer.class);
    private boolean isStarted;
    private final ServerConfig config;
    private ChannelFuture server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public ProxyTcpNioServer(ServerConfig config) {
        this.config = config;
    }

    @Override
    public String getTitle() {
        return "tcp/nio/netty 4.1/" + SocketD.version();
    }

    @Override
    public ServerConfig getConfig() {
        return config;
    }

    @Override
    public Server config(ServerConfigHandler configHandler) {
        if (configHandler != null) {
            configHandler.serverConfig(config);
        }
        return this;
    }

    @Override
    public Server listen(Listener listener) {
        return null;
    }

    @Override
    public Server start() throws IOException {
        if (isStarted) {
            throw new IllegalStateException("Socket.D server started");
        } else {
            isStarted = true;
        }

        bossGroup = new NioEventLoopGroup(getConfig().getIoThreads(), new NamedThreadFactory("nettyTcpServerBoss-"));
        workGroup = new NioEventLoopGroup(getConfig().getCodecThreads(), new NamedThreadFactory("nettyTcpServerWork-"));

        try {
            ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>() {
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
                                    Dami.bus().send(NetworkProxyConstants.PROXY_READ, new ProxyData(context.channel().id().asShortText(), byteArray));
                                }
                            });
                }
            };

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelHandler);

            if (StrUtils.isEmpty(getConfig().getHost())) {
                server = bootstrap.bind(getConfig().getPort()).await();
            } else {
                server = bootstrap.bind(getConfig().getHost(), getConfig().getPort()).await();
            }
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new SocketDException("proxy server start failed!", e);
            }
        }

        log.info("proxy server started: {server=" + getConfig().getLocalUrl() + "}");

        return this;
    }

    @Override
    public void prestop() {

    }

    @Override
    public void stop() {
        if (isStarted) {
            isStarted = false;
        } else {
            return;
        }

        try {
            if (server != null) {
                server.channel().close();
            }

            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }

            if (workGroup != null) {
                workGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Server stop error", e);
            }
        }
    }

}
