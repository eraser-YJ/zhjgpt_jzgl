package com.jc.csmp.contract.pay.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.csmp.contract.pay.service.ICmContractPayService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

/**
 * 合同支付审批未通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmContractPayAuditFailEvent implements EventInterface {
    private ICmContractPayService cmContractPayService = SpringContextHolder.getBean(ICmContractPayService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmContractPay entity = new CmContractPay();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmContractPayService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.fail.toString());
                cmContractPayService.update(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
