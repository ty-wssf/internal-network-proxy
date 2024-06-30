
package network.proxy.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.api.core.annotations.biz.BizMutation;
import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.annotations.core.Name;
import io.nop.api.core.beans.query.QueryBean;
import io.nop.api.core.exceptions.ErrorCode;
import io.nop.api.core.exceptions.NopException;
import io.nop.biz.crud.CrudBizModel;

import io.nop.core.context.IServiceContext;
import io.nop.dao.api.DaoProvider;
import io.wyl.network.InternalNetworkProxy;
import io.wyl.network.tunnel.server.PortMapping;
import io.wyl.network.tunnel.server.TunnelServer;
import jakarta.annotation.PreDestroy;
import network.proxy.dao.entity.ProxyClients;
import network.proxy.dao.entity.ProxyPortMapping;

import java.io.IOException;
import java.util.List;

@BizModel("ProxyPortMapping")
public class ProxyPortMappingBizModel extends CrudBizModel<ProxyPortMapping> {

    private TunnelServer tunnelServer = null;

    public ProxyPortMappingBizModel() {
        setEntityName(ProxyPortMapping.class.getName());
    }

    @BizMutation
    public boolean enabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        ProxyPortMapping entity = get(id, false, context);
        entity.setEnabled(true);
        dao().updateEntity(entity);

        // 判断对应客户端是否启用
        ProxyClients proxyClients = DaoProvider.instance().daoFor(ProxyClients.class).getEntityById(entity.getClientId());
        if (proxyClients == null) {
            throw new NopException(ErrorCode.define("proxy.client.unknown", "关联客户端不存在"));
        }
        if (!proxyClients.getEnabled()) {
            throw new NopException(ErrorCode.define("proxy.client.disabled", "关联客户端未启用"));
        }

        // 新增服务
        try {
            PortMapping portMapping = new PortMapping(String.valueOf(entity.getClientId()),
                    entity.getServerPort(), entity.getClientIp(), entity.getClientPort(),
                    entity.getProtocol());
            tunnelServer.addPortMapping(portMapping);
        } catch (IOException e) {
            throw NopException.adapt(e);
        }
        return true;
    }

    @BizMutation
    public boolean disabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        ProxyPortMapping entity = get(id, false, context);
        entity.setEnabled(false);
        dao().updateEntity(entity);

        // 移除服务
        try {
            tunnelServer.removePortMapping(entity.getServerPort());
        } catch (IOException e) {
            throw NopException.adapt(e);
        }
        return true;
    }

    // 延迟加载
    public void lazyInit() {
        if (tunnelServer == null) {
            try {
                tunnelServer = InternalNetworkProxy.createTunnelServer().start(18602);
            } catch (Exception e) {
                throw NopException.adapt(e);
            }
            ProxyPortMapping example = new ProxyPortMapping();
            example.setEnabled(true);
            List<ProxyPortMapping> list = DaoProvider.instance().daoFor(ProxyPortMapping.class).findAllByExample(example);
            for (int i = 0; i < list.size(); i++) {
                ProxyPortMapping proxyPortMapping = list.get(i);
                PortMapping portMapping = new PortMapping(String.valueOf(list.get(i).getClientId()),
                        proxyPortMapping.getServerPort(), proxyPortMapping.getClientIp(), proxyPortMapping.getClientPort(),
                        proxyPortMapping.getProtocol());
                try {
                    tunnelServer.addPortMapping(portMapping);
                } catch (IOException e) {
                    throw NopException.adapt(e);
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (tunnelServer == null) {
            try {
                tunnelServer.stop();
            } catch (IOException e) {
                throw NopException.adapt(e);
            }
        }
    }

}
