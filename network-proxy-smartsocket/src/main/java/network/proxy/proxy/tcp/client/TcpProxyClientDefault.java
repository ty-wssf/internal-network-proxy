package network.proxy.proxy.tcp.client;

import network.proxy.common.TunnelConstants;
import network.proxy.proxy.ClientConnectionCallback;
import network.proxy.proxy.ProxyClient;
import network.proxy.tunnel.client.TunnelClient;
import org.noear.socketd.transport.client.ClientConfigHandler;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.extension.protocol.ByteArrayProtocol;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

/**
 * @author wyl
 * @date 2024年07月02日 13:36
 */
public class TcpProxyClientDefault implements ProxyClient {

    private static final Logger log = LoggerFactory.getLogger(TcpProxyClientDefault.class);

    private final TunnelClient tunnelClient;
    private final String visitorId;
    private final String lanInfo;
    AioQuickClient real;
    AioSession aioSession;

    public TcpProxyClientDefault(TunnelClient tunnelClient, String lanInfo, String visitorId) {
        this.tunnelClient = tunnelClient;
        this.visitorId = visitorId;
        this.lanInfo = lanInfo;
    }

    @Override
    public void connect(ClientConnectionCallback callback) {
        // 关闭之前的资源
        disconnect();

        real = new AioQuickClient(lanInfo.split(":")[0], Integer.parseInt(lanInfo.split(":")[1]), new ByteArrayProtocol(), new AbstractMessageProcessor<byte[]>() {

            @Override
            public void process0(AioSession aioSession, byte[] bytes) {
                log.debug("访问者 {} 代理客户端接收数据", visitorId);

                try {
                    tunnelClient.getSession().send(TunnelConstants.TRANSFER_DATA, new EntityDefault().dataSet(bytes)
                            .metaPut(TunnelConstants.VISITOR_ID, visitorId));
                } catch (IOException e) {
                    log.warn("client process0 error", e);
                }

            }

            @Override
            public void stateEvent0(AioSession aioSession, StateMachineEnum state, Throwable throwable) {
                switch (state) {
                    case NEW_SESSION: {
                        // 连接打开
                    }
                    break;

                    case SESSION_CLOSED:
                        // 连接关闭
                        break;

                    case PROCESS_EXCEPTION:
                    case DECODE_EXCEPTION:
                    case INPUT_EXCEPTION:
                    case ACCEPT_EXCEPTION:
                    case OUTPUT_EXCEPTION:
                        // 连接异常
                        log.error("tcp Client ProxyChannel Error", throwable);
                        break;
                }
            }
        });
        try {
            real.start(null, new CompletionHandler<AioSession, Object>() {
                @Override
                public void completed(AioSession result, Object attachment) {
                    aioSession = result;
                    try {
                        callback.onConnectSuccess();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    try {
                        callback.onConnectFailure(exc);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            // ...
        }

    }

    @Override
    public void disconnect() {
        try {
            if (real != null) {
                real.shutdown();
            }
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.debug("Client connector close error", e);
            }
        }
    }

    @Override
    public ProxyClient config(ClientConfigHandler configHandler) {
        return null;
    }

    @Override
    public void send(byte[] data) {
        try {
            if (!aioSession.isInvalid()) {
                log.debug("访问者 {} 代理通道发送数据", visitorId);
                aioSession.writeBuffer().write(data);
                log.debug("访问者 {} 代理通道发送数据成功", visitorId);
            } else {
                connect(new ClientConnectionCallback() {
                    @Override
                    public void onConnectSuccess() throws IOException {
                        log.debug("访问者 {} 代理通道发送数据", visitorId);
                        aioSession.writeBuffer().write(data);
                        log.debug("访问者 {} 代理通道发送数据成功", visitorId);
                    }

                    @Override
                    public void onConnectFailure(Throwable e) {
                        log.warn("访问者 {} 代理客户端连接服务器失败", visitorId);
                    }
                });
            }
        } catch (IOException e) {
            //...
        }
    }

}
