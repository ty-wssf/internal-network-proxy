package network.proxy.tunnel.client;

import network.proxy.common.TunnelConstants;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wyl
 * @date 2024年02月20日 15:55
 */
@Component
public class TunnelClientLifecycleBean implements LifecycleBean {

    private static final Logger log = LoggerFactory.getLogger(TunnelClientLifecycleBean.class);
    TunnelClient tunnelClient;

    @Override
    public void start() throws Throwable {
        String serverUrl = Solon.cfg().get(ConfigNames.tunnel_serverUrl);
        String clientId = Solon.cfg().get(ConfigNames.tunnel_clientId);
        tunnelClient = new TunnelClientDefault(serverUrl + "?@=" + clientId);
        tunnelClient.connect();
        log.info("隧道服务服务客户端启动成功：{}", clientId);
    }

    @Override
    public void stop() throws Throwable {
        LifecycleBean.super.stop();
        tunnelClient.disconnect();
    }


}
