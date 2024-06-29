package network.proxy.service.initialize;

import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.ioc.BeanContainer;
import io.nop.core.initialize.ICoreInitializer;
import io.nop.dao.api.IDaoProvider;
import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.tunnel.server.TunnelServer;
import network.proxy.dao.entity.PortMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author wyl
 * @date 2024年06月28日 17:22
 */
public class ProxyServerInitializer implements ICoreInitializer {


    @Override
    public void initialize() {
        IDaoProvider daoProvider = BeanContainer.getBeanByType(IDaoProvider.class);
        PortMapping example = new PortMapping();
        example.setEnabled(true);
        try {
            TunnelServer tunnelServer = InternalNetworkProxy.createTunnelServer().start(18602);
            List<PortMapping> list = daoProvider.daoFor(PortMapping.class).findAllByExample(example);
            for (int i = 0; i < list.size(); i++) {
                PortMapping portMapping = list.get(i);
                io.wyl.network.tunnel.server.PortMapping portMapping_ = new io.wyl.network.tunnel.server.PortMapping(String.valueOf(list.get(i).getId()),
                        portMapping.getServerPort(), portMapping.getClientIp(), portMapping.getClientPort(), portMapping.getProtocol());

                tunnelServer.addPortMapping(portMapping_);
            }
        } catch (Exception e) {
            // throw new NopException(e);
        }

    }

}
