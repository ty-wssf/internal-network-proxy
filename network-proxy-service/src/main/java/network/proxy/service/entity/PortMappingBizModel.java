
package network.proxy.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.biz.crud.CrudBizModel;

import network.proxy.dao.entity.PortMapping;

@BizModel("PortMapping")
public class PortMappingBizModel extends CrudBizModel<PortMapping>{
    public PortMappingBizModel(){
        setEntityName(PortMapping.class.getName());
    }
}
