
package network.proxy.service.entity;

import network.proxy.tunnel.server.ClientInfo;
import io.nop.api.core.annotations.biz.BizModel;
import io.nop.api.core.annotations.biz.BizMutation;
import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.annotations.core.Name;
import io.nop.biz.crud.CrudBizModel;
import io.nop.core.context.IServiceContext;
import io.nop.dao.api.DaoProvider;
import jakarta.inject.Inject;
import network.proxy.dao.entity.ProxyClients;
import network.proxy.dao.entity.ProxyPortMapping;

import java.util.List;

@BizModel("ProxyClients")
public class ProxyClientsBizModel extends CrudBizModel<ProxyClients> {

    private ProxyPortMappingBizModel portMappingBizModel;

    @Inject
    public void setProxyPortMappingBizModel(ProxyPortMappingBizModel portMappingBizModel) {
        this.portMappingBizModel = portMappingBizModel;
    }

    public ProxyClientsBizModel() {
        setEntityName(ProxyClients.class.getName());
    }

    @BizMutation
    public boolean enabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        ProxyClients entity = get(id, false, context);
        entity.setEnabled(true);
        dao().updateEntity(entity);

        portMappingBizModel.addClientInfo(new ClientInfo(String.valueOf(entity.getId())));
        return true;
    }

    @BizMutation
    public boolean disabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        // 禁用关联的运行中的服务
        ProxyPortMapping proxyPortMappingExample = new ProxyPortMapping();
        proxyPortMappingExample.setEnabled(true);
        proxyPortMappingExample.setClientId(Integer.valueOf(id));
        List<ProxyPortMapping> proxyPortMappingList = DaoProvider.instance().daoFor(ProxyPortMapping.class).findAllByExample(proxyPortMappingExample);
        if (!proxyPortMappingList.isEmpty()) {
            for (int i = 0; i < proxyPortMappingList.size(); i++) {
                portMappingBizModel.disabled(String.valueOf(proxyPortMappingList.get(i).getId()), context);
            }
        }

        ProxyClients entity = get(id, false, context);
        entity.setEnabled(false);
        dao().updateEntity(entity);

        portMappingBizModel.removeClientInfo(String.valueOf(entity.getId()));
        return true;
    }

}
