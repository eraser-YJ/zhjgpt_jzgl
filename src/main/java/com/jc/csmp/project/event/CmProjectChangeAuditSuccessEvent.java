package com.jc.csmp.project.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.csmp.project.service.ICmProjectChangeOrderService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

/**
 * 工程变更单单审批通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmProjectChangeAuditSuccessEvent implements EventInterface {
    private ICmProjectChangeOrderService cmProjectChangeOrderService = SpringContextHolder.getBean(ICmProjectChangeOrderService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmProjectChangeOrder entity = new CmProjectChangeOrder();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmProjectChangeOrderService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.finish.toString());
                cmProjectChangeOrderService.update(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
