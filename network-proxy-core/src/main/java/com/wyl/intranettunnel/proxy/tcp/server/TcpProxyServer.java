package com.wyl.intranettunnel.proxy.tcp.server;

import com.wyl.intranettunnel.proxy.ProxyServer;
import com.wyl.intranettunnel.tunnel.server.TunnelServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.noear.socketd.exception.SocketDException;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.utils.NamedThreadFactory;
import org.noear.socketd.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月15日 9:52
 */
public class TcpProxyServer implements ProxyServer {

    private static final Logger log = LoggerFactory.getLogger(TcpProxyServer.class);
    private ChannelFuture server;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private ServerConfig config;
    private TunnelServer tunnelServer;
    private boolean isStarted;

    public TcpProxyServer(ServerConfig config, TunnelServer tunnelServer) {
        this.config = config;
        this.tunnelServer = tunnelServer;
    }

    @Override
    public ServerConfig getConfig() {
        return this.config;
    }

    @Override
    public ProxyServer start() throws IOException {
        if (isStarted) {
            throw new IllegalStateException("proxy tcp server started");
        } else {
            isStarted = true;
        }

        bossGroup = new NioEventLoopGroup(getConfig().getIoThreads(), new NamedThreadFactory("nettyTcpServerBoss-"));
        workGroup = new NioEventLoopGroup(getConfig().getCodecThreads(), new NamedThreadFactory("nettyTcpServerWork-"));

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TcpProxyServerChannelHandler(tunnelServer));
                        }
                    });

            if (StrUtils.isEmpty(getConfig().getHost())) {
                server = bootstrap.bind(getConfig().getPort()).await();
            } else {
                server = bootstrap.bind(getConfig().getHost(), getConfig().getPort()).await();
            }
        } catch (Throwable e) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new SocketDException("proxy tcp server start failed!", e);
            }
        }

        log.info("proxy tcp server started port：" + getConfig().getPort());

        return this;
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
