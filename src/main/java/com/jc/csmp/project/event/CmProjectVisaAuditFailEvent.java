package com.jc.csmp.project.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.project.dao.ICmProjectVisaOrderDao;
import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.csmp.project.service.ICmProjectVisaOrderService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

/**
 * 工程签证单审批未通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmProjectVisaAuditFailEvent implements EventInterface {
    private ICmProjectVisaOrderService cmProjectVisaOrderService = SpringContextHolder.getBean(ICmProjectVisaOrderService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmProjectVisaOrder entity = new CmProjectVisaOrder();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmProjectVisaOrderService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.fail.toString());
                cmProjectVisaOrderService.update(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
