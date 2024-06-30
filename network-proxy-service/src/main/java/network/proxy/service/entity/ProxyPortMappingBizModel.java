
package network.proxy.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.biz.crud.CrudBizModel;

import network.proxy.dao.entity.ProxyPortMapping;

@BizModel("ProxyPortMapping")
public class ProxyPortMappingBizModel extends CrudBizModel<ProxyPortMapping>{
    public ProxyPortMappingBizModel(){
        setEntityName(ProxyPortMapping.class.getName());
    }
}
