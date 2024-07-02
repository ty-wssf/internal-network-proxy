package network.proxy.proxy.udp.client;

import network.proxy.tunnel.client.TunnelClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wyl
 * @date 2024年02月20日 9:29
 */
public class UdpProxyServerChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(UdpProxyServerChannelHandler.class);
    private TunnelClient tunnelClient;
    private String visitorId;

    public UdpProxyServerChannelHandler(TunnelClient tunnelClient, String visitorId) {
        this.tunnelClient = tunnelClient;
        this.visitorId = visitorId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        System.out.println();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("udp Client ProxyChannel Error", cause);
    }

}
