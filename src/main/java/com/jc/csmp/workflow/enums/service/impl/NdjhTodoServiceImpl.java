package com.jc.csmp.workflow.enums.service.impl;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.csmp.project.service.ICmProjectChangeOrderService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.WorkflowBean;

import java.util.Map;

/**
 * 工作流年度计划待办
 * @Author 常鹏
 * @Date 2020/8/21 10:53
 * @Version 1.0
 */
public class NdjhTodoServiceImpl extends IWorkflowTodoService {

    private IProjectYearPlanService projectPlanService;

    public NdjhTodoServiceImpl() {
        projectPlanService = SpringContextHolder.getBean(IProjectYearPlanService.class);
    }

    @Override
    public TodoExtendBean getByPid(String pid) {
        ProjectYearPlan entity = new ProjectYearPlan();
        entity.setPiId(pid);
        try {
            entity = projectPlanService.get(entity);
            if (entity != null) {
                return TodoExtendBean.create(entity.getPlanSeqno() + "", entity.getPlanYear() + "年" + entity.getPlanName() + "审批");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAttachTableName() {
        return "";
    }

    @Override
    public Object getEntityByPid(String pid) {
        ProjectYearPlan entity = new ProjectYearPlan();
        entity.setPiId(pid);
        try {
            return projectPlanService.get(entity);
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
