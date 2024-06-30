package io.wyl.network.proxy.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import org.noear.dami.Dami;
import org.noear.socketd.SocketD;
import org.noear.socketd.exception.SocketDException;
import org.noear.socketd.transport.core.Entity;
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
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月25日 9:10
 */
public class ProxyTcpNioServer implements Server {
    private static final Logger log = LoggerFactory.getLogger(TcpNioServer.class);
    private boolean isStarted;
    private final ServerConfig config;
    private ChannelFuture server;
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workGroup;

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

        if (bossGroup == null) {
            bossGroup = new NioEventLoopGroup(getConfig().getIoThreads(), new NamedThreadFactory("proxyTcpServerBoss-"));
            workGroup = new NioEventLoopGroup(getConfig().getCodecThreads(), new NamedThreadFactory("proxyTcpServerWork-"));
        }

        try {
            ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
                                    ctx.channel().config().setOption(ChannelOption.AUTO_READ, false);
                                    // Entity
                                    Entity connectEntity = Entity.of().metaPut(NetworkProxyConstants.VISITOR_ID, ctx.channel().id().asShortText())
                                            .metaPut(NetworkProxyConstants.PROXY_SERVER_PORT, String.valueOf(sa.getPort()));
                                    Dami.<Entity, Entity>bus().sendAndSubscribe(NetworkProxyConstants.PROXY_SERVER_CONNECT, connectEntity, reply -> {
                                        // 可能返回真实的服务端返回的数据和连接成功的回复信息
                                        String replyType = reply.meta(NetworkProxyConstants.REPLY_TYPE);
                                        if (Objects.equals(replyType, NetworkProxyConstants.PROXY_SERVER_CONNECT)) { // 连接回复
                                            if (reply.metaAsInt(NetworkProxyConstants.ACK) == 1) {
                                                log.info("访问者 {} 连接成功", ctx.channel().id().asShortText());
                                                ctx.channel().config().setOption(ChannelOption.AUTO_READ, true);
                                                ctx.fireChannelActive();
                                            } else {
                                                ctx.close();
                                                log.info("访问者 {} 连接失败", ctx.channel().id().asShortText());
                                            }
                                        } else { // 真实的服务端返回的数据
                                            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(reply.dataAsBytes()));
                                        }
                                    });
                                }

                                // 添加处理器
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                                    InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();

                                    // 创建一个与ByteBuf长度相等的byte数组
                                    byte[] byteArray = new byte[byteBuf.readableBytes()];

                                    // 将ByteBuf的内容读取到byte数组中
                                    byteBuf.readBytes(byteArray);

                                    // 通过代理客户端发送到真实的服务端
                                    Dami.bus().send(NetworkProxyConstants.PROXY_SERVER_READ, Entity.of(byteArray)
                                            .metaPut(NetworkProxyConstants.VISITOR_ID, ctx.channel().id().asShortText())
                                            .metaPut(NetworkProxyConstants.PROXY_SERVER_PORT, String.valueOf(sa.getPort())));
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
                                    super.channelInactive(ctx);
                                    log.info("访问者 {} 断开连接", ctx.channel().id().asShortText());
                                    Entity disconnectEntity = Entity.of().metaPut(NetworkProxyConstants.VISITOR_ID, ctx.channel().id().asShortText())
                                            .metaPut(NetworkProxyConstants.PROXY_SERVER_PORT, String.valueOf(sa.getPort()));
                                    Dami.<Entity, Entity>bus().send(NetworkProxyConstants.PROXY_SERVER_DISCONNECT, disconnectEntity);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
                                    // 当出现异常就关闭连接
                                    ctx.close();
                                    log.error("访问者 error", cause);
                                    // 为了让代理客户端释放资源
                                    Dami.<Entity, Entity>bus().send(NetworkProxyConstants.PROXY_SERVER_DISCONNECT,
                                            Entity.of().metaPut(NetworkProxyConstants.VISITOR_ID, ctx.channel().id().asShortText()));
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
                bossGroup = null;
            }

            if (workGroup != null) {
                workGroup.shutdownGracefully();
                workGroup = null;
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Server stop error", e);
            }
        }
        log.info("proxy server stop: {server=" + getConfig().getLocalUrl() + "}");
    }

}
