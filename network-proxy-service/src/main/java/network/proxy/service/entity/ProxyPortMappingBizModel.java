
package network.proxy.service.entity;

import network.proxy.tunnel.server.ClientInfo;
import network.proxy.tunnel.server.PortMapping;
import network.proxy.tunnel.server.TunnelServer;
import network.proxy.tunnel.server.TunnelServerDefault;
import io.nop.api.core.annotations.biz.BizModel;
import io.nop.api.core.annotations.biz.BizMutation;
import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.annotations.core.Name;
import io.nop.api.core.exceptions.ErrorCode;
import io.nop.api.core.exceptions.NopException;
import io.nop.biz.crud.CrudBizModel;
import io.nop.core.context.IServiceContext;
import io.nop.dao.api.DaoProvider;
import jakarta.annotation.PreDestroy;
import network.proxy.dao.entity.ProxyClients;
import network.proxy.dao.entity.ProxyPortMapping;

import java.util.List;

@BizModel("ProxyPortMapping")
public class ProxyPortMappingBizModel extends CrudBizModel<ProxyPortMapping> {

    TunnelServer tunnelServer = new TunnelServerDefault();

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
        PortMapping portMapping = new PortMapping();
        portMapping.setClientId(String.valueOf(entity.getClientId()));
        portMapping.setServerPort(entity.getServerPort());
        portMapping.setClientIp(entity.getClientIp());
        portMapping.setClientPort(entity.getClientPort());
        portMapping.setProtocol(entity.getProtocol());
        tunnelServer.addPortMapping(portMapping);

        return true;
    }

    @BizMutation
    public boolean disabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        ProxyPortMapping entity = get(id, false, context);
        entity.setEnabled(false);
        dao().updateEntity(entity);

        // 移除服务
        tunnelServer.removeMappingByPort(entity.getServerPort());
        return true;
    }

    // 延迟加载
    public void lazyInit() {
        try {
            tunnelServer.start(8602);
        } catch (Exception e) {
            throw NopException.wrap(e);
        }

        ProxyClients proxyClientsExample = new ProxyClients();
        proxyClientsExample.setEnabled(true);
        List<ProxyClients> proxyClientsList = DaoProvider.instance().daoFor(ProxyClients.class).findAllByExample(proxyClientsExample);
        for (int i = 0; i < proxyClientsList.size(); i++) {
            // 默认添加启动的客户端
            tunnelServer.addClientInfo(new ClientInfo(String.valueOf(proxyClientsList.get(i).getId())));
        }

        ProxyPortMapping proxyPortMappingExample = new ProxyPortMapping();
        proxyClientsExample.setEnabled(true);
        List<ProxyPortMapping> proxyPortMappingList = DaoProvider.instance().daoFor(ProxyPortMapping.class).findAllByExample(proxyPortMappingExample);
        for (int i = 0; i < proxyPortMappingList.size(); i++) {
            // 默认启动启用的代理服务
            PortMapping portMapping = new PortMapping();
            portMapping.setClientId(String.valueOf(proxyPortMappingList.get(i).getClientId()));
            portMapping.setServerPort(proxyPortMappingList.get(i).getServerPort());
            portMapping.setClientIp(proxyPortMappingList.get(i).getClientIp());
            portMapping.setClientPort(proxyPortMappingList.get(i).getClientPort());
            portMapping.setProtocol(proxyPortMappingList.get(i).getProtocol());
            tunnelServer.addPortMapping(portMapping);
        }
    }

    void addClientInfo(ClientInfo clientInfo) {
        tunnelServer.addClientInfo(clientInfo);
    }

    void removeClientInfo(String clientId) {
        tunnelServer.removeClientInfo(clientId);
    }

    @PreDestroy
    public void destroy() {
        if (tunnelServer == null) {
            tunnelServer.stop();
        }
    }

}
