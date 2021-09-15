package com.jc.csmp.contract.info.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

/**
 * 合同审批未通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmContractAuditFailEvent implements EventInterface {
    private ICmContractInfoService cmContractInfoService = SpringContextHolder.getBean(ICmContractInfoService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmContractInfo entity = new CmContractInfo();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmContractInfoService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.fail.toString());
                cmContractInfoService.update(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
