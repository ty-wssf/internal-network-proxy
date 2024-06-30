
package network.proxy.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.biz.crud.CrudBizModel;

import network.proxy.dao.entity.ProxyClients;

@BizModel("ProxyClients")
public class ProxyClientsBizModel extends CrudBizModel<ProxyClients>{
    public ProxyClientsBizModel(){
        setEntityName(ProxyClients.class.getName());
    }
}
