package com.wyl.intranettunnel.proxy.tcp.client;

import com.wyl.intranettunnel.common.TunnelConstants;
import com.wyl.intranettunnel.tunnel.client.TunnelClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.utils.IoConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author wyl
 * @date 2024年01月31日 16:01
 */
@ChannelHandler.Sharable
public class TcpProxyClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(TcpProxyClientChannelHandler.class);

    private TunnelClient tunnelClient;
    private String visitorId;

    public TcpProxyClientChannelHandler(TunnelClient tunnelClient, String visitorId) {
        this.tunnelClient = tunnelClient;
        this.visitorId = visitorId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        log.debug("访问者 {} 代理客户端接收数据", visitorId);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ctx.channel().config().setOption(ChannelOption.AUTO_READ, false);
        tunnelClient.sendAndRequest(TunnelConstants.TRANSFER_DATA, new EntityDefault().dataSet(bytes)
                .metaPut(TunnelConstants.VISITOR_ID, visitorId)).thenReply(new IoConsumer<Reply>() {
            @Override
            public void accept(Reply reply) throws IOException {
                int ack = Integer.parseInt(reply.metaOrDefault(TunnelConstants.META_ACK, "0"));
                if (ack > 0) {
                    log.debug("访问者 {} 代理客户端接收数据发送到隧道服务端成功", visitorId);
                } else {
                    log.debug("访问者 {} 代理客户端接收数据发送到隧道服务端失败", visitorId);
                }
                ctx.channel().config().setOption(ChannelOption.AUTO_READ, true);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("访问者 {} 代理客户端断开连接", visitorId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("tcp Client ProxyChannel Error", cause);
    }

}
