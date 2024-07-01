
package network.proxy.service.entity;

import com.wyl.intranettunnel.tunnel.server.ClientInfo;
import io.nop.api.core.annotations.biz.BizModel;
import io.nop.api.core.annotations.biz.BizMutation;
import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.annotations.core.Name;
import io.nop.biz.crud.CrudBizModel;
import io.nop.core.context.IServiceContext;
import io.nop.dao.api.IDaoProvider;
import jakarta.inject.Inject;
import network.proxy.dao.entity.ProxyClients;

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
        ProxyClients entity = get(id, false, context);
        entity.setEnabled(false);
        dao().updateEntity(entity);

        portMappingBizModel.removeClientInfo(String.valueOf(entity.getId()));
        return true;
    }

}
