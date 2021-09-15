package com.jc.csmp.contract.pay.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.csmp.contract.pay.service.ICmContractPayService;
import com.jc.csmp.project.service.ICmProjectDataAuthService;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

import java.util.Date;
import java.util.List;

/**
 * 合同审批通过线
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmContractPayAuditSuccessEvent implements EventInterface {
    private ICmContractPayService cmContractPayService = SpringContextHolder.getBean(ICmContractPayService.class);
    private ICmContractInfoService cmContractInfoService = SpringContextHolder.getBean(ICmContractInfoService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmContractPay entity = new CmContractPay();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmContractPayService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.finish.toString());
                //更新累计支付金额同时修改合同表的支付金额
                if (entity.getReplyMoney() == null) {
                    entity.setReplyMoney(entity.getApplyMoney());
                }
                entity.setPayMoney(GlobalUtil.add(entity.getTotalPayment(), entity.getReplyMoney()));
                entity.setTotalPayment(GlobalUtil.add(entity.getTotalPayment(), entity.getReplyMoney()));
                entity.setModifyDate(new Date(System.currentTimeMillis()));
                cmContractPayService.update(entity);
                cmContractInfoService.updateTotalPayment(entity.getContractId(), entity.getTotalPayment());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
