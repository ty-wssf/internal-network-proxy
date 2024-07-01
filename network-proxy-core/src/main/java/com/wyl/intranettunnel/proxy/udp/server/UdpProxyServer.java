package com.wyl.intranettunnel.proxy.udp.server;

import com.wyl.intranettunnel.proxy.ProxyServer;
import com.wyl.intranettunnel.tunnel.server.TunnelServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.noear.socketd.exception.SocketDException;
import org.noear.socketd.transport.server.ServerConfig;
import org.noear.socketd.utils.NamedThreadFactory;
import org.noear.socketd.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月18日 19:47
 */
public class UdpProxyServer implements ProxyServer {

    private static final Logger log = LoggerFactory.getLogger(UdpProxyServer.class);
    private ChannelFuture server;

    private EventLoopGroup bossGroup;
    private ServerConfig config;
    private TunnelServer tunnelServer;
    private boolean isStarted;

    public UdpProxyServer(ServerConfig config, TunnelServer tunnelServer) {
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
            throw new IllegalStateException("proxy udp server started");
        } else {
            isStarted = true;
        }

        bossGroup = new NioEventLoopGroup(getConfig().getCodecThreads(), new NamedThreadFactory("nettyUdpServerBoss-"));

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    //.option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpProxyServerChannelHandler(tunnelServer));

            if (StrUtils.isEmpty(getConfig().getHost())) {
                server = bootstrap.bind(getConfig().getPort()).await();
            } else {
                server = bootstrap.bind(getConfig().getHost(), getConfig().getPort()).await();
            }

        } catch (Exception e) {
            bossGroup.shutdownGracefully();

            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new SocketDException("proxy udp server start failed!", e);
            }
        }

        log.info("proxy udp server started port：" + getConfig().getPort());

        return this;
    }

    @Override
    public void stop() {

    }

}
