package com.jc.csmp.workflow.enums.service.impl;

import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.csmp.safe.supervision.service.ISafetySupervisionService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.WorkflowBean;

import java.util.Map;

/**
 * 工作流工程报监待办
 * @Author 常鹏
 * @Date 2020/8/21 10:53
 * @Version 1.0
 */
public class GcbjTodoServiceImpl extends IWorkflowTodoService {

    private ISafetySupervisionService safetySupervisionService;

    public GcbjTodoServiceImpl() {
        safetySupervisionService = SpringContextHolder.getBean(ISafetySupervisionService.class);
    }

    @Override
    public TodoExtendBean getByPid(String pid) {
        SafetySupervision entity = new SafetySupervision();
        entity.setPiId(pid);
        try {
            entity = safetySupervisionService.get(entity);
            if (entity != null) {
                return TodoExtendBean.create(entity.getProjectNumber(), entity.getProjectName() + "的报监审批");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAttachTableName() {
        return "cm_safety_supervision";
    }

    @Override
    public Object getEntityByPid(String pid) {
        SafetySupervision entity = new SafetySupervision();
        entity.setPiId(pid);
        try {
            return entity = safetySupervisionService.get(entity);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getFlowVariable(Map<String, Object> workflowBean, Object data) {
        return null;
    }

    @Override
    public Integer updateWorkflow(WorkflowBean workflowBean, Map<String, Object> extendData) {
        return null;
    }
}
