package com.wyl.intranettunnel.proxy.udp.server;

import com.wyl.intranettunnel.common.TunnelConstants;
import com.wyl.intranettunnel.tunnel.server.TunnelServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author wyl
 * @date 2024年02月18日 19:51
 */
@ChannelHandler.Sharable
public class UdpProxyServerChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(UdpProxyServerChannelHandler.class);
    private TunnelServer tunnelServer;

    public UdpProxyServerChannelHandler(TunnelServer tunnelServer) {
        this.tunnelServer = tunnelServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        log.debug("访问者 {} 接收数据", ctx.channel().id().asShortText());
        Channel visitorChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) visitorChannel.localAddress();

        ByteBuf buf = packet.content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        // 发送数据到代理客户端，再由代理客户端转发到真正的服务
        tunnelServer.send(TunnelConstants.TRANSFER_DATA, new EntityDefault().dataSet(bytes)
                .metaPut(TunnelConstants.CLIENT_ID, tunnelServer.getMappingByPort(sa.getPort()).getClientId())
                .metaPut(TunnelConstants.VISITOR_ID, ctx.channel().id().asShortText())
                .metaPut(TunnelConstants.PROTOCOL, TunnelConstants.UDP_PROTOCOL)
                .metaPut(TunnelConstants.LAN_INFO, tunnelServer.getMappingByPort(sa.getPort()).getClientIp() + ":" +
                        tunnelServer.getMappingByPort(sa.getPort()).getClientPort())
        );
    }

}
