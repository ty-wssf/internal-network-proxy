package io.wyl.network.tunnel.server;

import io.wyl.network.common.NetworkProxyConstants;
import io.wyl.network.common.ProxyData;
import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.socketd.broker.BrokerListener;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.listener.EventListener;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年06月24日 21:29
 */
public class TunnelServiceListener extends EventListener {

    protected BrokerListener brokerListener = new BrokerListener();


    public TunnelServiceListener() {
        Dami.<ProxyData, Object>bus().listen(NetworkProxyConstants.PROXY_READ, new TopicListener<Payload<ProxyData, Object>>() {
            @Override
            public void onEvent(Payload<ProxyData, Object> voidPayload) throws Throwable {
                // 通过隧道服务发送数据到对应的代理客户端
            }
        });
    }

    @Override
    public void onOpen(Session session) throws IOException {
        super.onOpen(session);

        // 增加经理人支持
        brokerListener.onOpen(session);
    }

    @Override
    public void onClose(Session session) {
        super.onClose(session);

        // 增加经理人支持
        brokerListener.onClose(session);
    }

    public TunnelServer addPortMapping(PortMapping portMapping) {
        return null;
    }

}
