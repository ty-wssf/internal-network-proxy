package com.wyl.intranettunnel.proxy.tcp.server;

import com.wyl.intranettunnel.common.TunnelConstants;
import com.wyl.intranettunnel.tunnel.server.TunnelServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.utils.IoConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;


/**
 * @author wyl
 * @date 2024年01月31日 16:01
 */
@ChannelHandler.Sharable
public class TcpProxyServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(TcpProxyServerChannelHandler.class);
    private TunnelServer tunnelServer;

    public TcpProxyServerChannelHandler(TunnelServer tunnelServer) {
        this.tunnelServer = tunnelServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        log.info("开始连接：{}", ctx.channel().id().asShortText());
        // 添加写回调函数
        tunnelServer.addVisitorWriteCallback(ctx.channel().id().asShortText(), new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(bytes));
            }
        });
        ctx.channel().config().setOption(ChannelOption.AUTO_READ, false);
        // 发送连接请求到代理客户端，让代理客户端连接到真正的服务
        tunnelServer.sendAndRequest(TunnelConstants.CONNECT, new EntityDefault()
                .metaPut(TunnelConstants.PROTOCOL, TunnelConstants.TCP_PROTOCOL)
                .metaPut(TunnelConstants.CLIENT_ID, tunnelServer.getMappingByPort(sa.getPort()).getClientId())
                .metaPut(TunnelConstants.LAN_INFO, tunnelServer.getMappingByPort(sa.getPort()).getClientIp() + ":" +
                        tunnelServer.getMappingByPort(sa.getPort()).getClientPort())
                .metaPut(TunnelConstants.VISITOR_ID, ctx.channel().id().asShortText())).thenReply(
                new IoConsumer<Reply>() {
                    @Override
                    public void accept(Reply reply) throws IOException {
                        int ack = Integer.parseInt(reply.metaOrDefault(TunnelConstants.META_ACK, "0"));
                        if (ack > 0) {
                            log.info("访问者 {} 连接成功", ctx.channel().id().asShortText());
                            ctx.channel().config().setOption(ChannelOption.AUTO_READ, true);
                            ctx.fireChannelActive();
                        } else {
                            ctx.close();
                            log.info("访问者 {} 连接失败", ctx.channel().id().asShortText());
                        }
                    }
                }
        );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        log.debug("访问者 {} 接收数据", ctx.channel().id().asShortText());
        Channel visitorChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) visitorChannel.localAddress();

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        // 发送数据到代理客户端，再由代理客户端转发到真正的服务
        tunnelServer.send(TunnelConstants.TRANSFER_DATA, new EntityDefault().dataSet(bytes)
                .metaPut(TunnelConstants.CLIENT_ID, tunnelServer.getMappingByPort(sa.getPort()).getClientId())
                .metaPut(TunnelConstants.VISITOR_ID, ctx.channel().id().asShortText())
        );

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除写回调函数
        tunnelServer.removeVisitorWriteCallback(ctx.channel().id().asShortText());
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().localAddress();
        log.info("访问者断开连接：{}", ctx.channel().id().asShortText());
        ctx.fireChannelInactive();
        tunnelServer.sendAndRequest(TunnelConstants.DISCONNECT, new EntityDefault()
                .metaPut(TunnelConstants.CLIENT_ID, tunnelServer.getMappingByPort(sa.getPort()).getClientId())
                .metaPut(TunnelConstants.VISITOR_ID, ctx.channel().id().asShortText())).thenReply(
                new IoConsumer<Reply>() {
                    @Override
                    public void accept(Reply reply) throws IOException {
                        int ack = Integer.parseInt(reply.metaOrDefault(TunnelConstants.META_ACK, "0"));
                        if (ack > 0) {
                            log.info("访问者断开连接成功：{}", ctx.channel().id().asShortText());
                        }
                    }
                }
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        ctx.close();
        log.error("VisitorChannel error", cause);
    }

}
