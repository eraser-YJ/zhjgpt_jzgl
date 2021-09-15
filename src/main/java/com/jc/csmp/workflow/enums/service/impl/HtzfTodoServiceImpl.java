package com.jc.csmp.workflow.enums.service.impl;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.csmp.contract.pay.service.ICmContractPayService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.WorkflowBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流合同支付待办
 * @Author 常鹏
 * @Date 2020/8/21 10:53
 * @Version 1.0
 */
public class HtzfTodoServiceImpl extends IWorkflowTodoService {

    private ICmContractPayService cmContractPayService;

    public HtzfTodoServiceImpl() {
        cmContractPayService = SpringContextHolder.getBean(ICmContractPayService.class);
    }

    @Override
    public TodoExtendBean getByPid(String pid) {
        CmContractPay entity = new CmContractPay();
        entity.setPiId(pid);
        try {
            entity = cmContractPayService.get(entity);
            if (entity != null) {
                return TodoExtendBean.create(entity.getPayNo(), "针对合同：" + entity.getContractName() + "的支付申请");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAttachTableName() {
        return "cm_contract_pay";
    }

    @Override
    public Object getEntityByPid(String pid) {
        CmContractPay entity = new CmContractPay();
        entity.setPiId(pid);
        try {
            return cmContractPayService.get(entity);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getFlowVariable(Map<String, Object> workflowBean, Object data) {
        Map<String, Object> result = new HashMap<>(5);
        result.put("curNodeId_", workflowBean.get("curNodeId_"));
        result.put("definitionId_", workflowBean.get("definitionId_"));
        result.put("instanceId_", workflowBean.get("instanceId_"));
        return result;
    }

    @Override
    public Integer updateWorkflow(WorkflowBean workflowBean, Map<String, Object> extendData) {
        CmContractPay entity = new CmContractPay();
        entity.setWorkflowBean(workflowBean);
        try {
            if (extendData != null && extendData.size() > 0) {
                for (Map.Entry<String, Object> entry : extendData.entrySet()) {
                    PropertyDescriptor pd = new PropertyDescriptor(entry.getKey(), entity.getClass());
                    Method method = pd.getWriteMethod();
                    Object value = GlobalUtil.convertData(method, (String) entry.getValue());
                    if (value != null) {
                        method.invoke(entity, value);
                    }
                }
            }
            return cmContractPayService.updateWorkflow(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
