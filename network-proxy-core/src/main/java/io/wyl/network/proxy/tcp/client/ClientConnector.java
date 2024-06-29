package io.wyl.network.proxy.tcp.client;

import io.netty.channel.ChannelFuture;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author wyl
 * @date 2024年06月29日 10:03
 */
public interface ClientConnector {
    ChannelFuture connect(Consumer<ChannelFuture> consumer);

    void close() throws IOException;
}
