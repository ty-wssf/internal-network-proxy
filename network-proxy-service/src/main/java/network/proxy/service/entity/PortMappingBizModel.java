
package network.proxy.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.api.core.annotations.biz.BizMutation;
import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.annotations.core.Name;
import io.nop.biz.crud.CrudBizModel;
import io.nop.core.context.IServiceContext;
import network.proxy.dao.entity.PortMapping;

@BizModel("PortMapping")
public class PortMappingBizModel extends CrudBizModel<PortMapping> {
    public PortMappingBizModel() {
        setEntityName(PortMapping.class.getName());
    }

    @BizMutation
    public boolean enabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        PortMapping entity = get(id, false, context);
        entity.setEnabled(true);
        dao().updateEntity(entity);
        return true;
    }

    @BizMutation
    public boolean disabled(@Name("id") @Description("@i18n:biz.id|对象的主键标识") String id, IServiceContext context) {
        PortMapping entity = get(id, false, context);
        entity.setEnabled(false);
        dao().updateEntity(entity);
        return true;
    }

}
