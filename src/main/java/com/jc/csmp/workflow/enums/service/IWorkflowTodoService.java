package com.jc.csmp.workflow.enums.service;

import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.workflow.external.WorkflowBean;

import java.util.Map;

/**
 * 待办任务接口
 * @Author 常鹏
 * @Date 2020/8/21 10:48
 * @Version 1.0
 */
public abstract class IWorkflowTodoService {
    /**
     * 根据工作流的id获取业务数据
     * @param pid
     * @return
     */
    public abstract TodoExtendBean getByPid(String pid);

    /**
     * 获取附件表标识
     * @return
     */
    public abstract String getAttachTableName();

    /**
     * 获取业务数据
     * @param pid
     * @return
     */
    public abstract Object getEntityByPid(String pid);

    /**
     * 获取流程变量
     * @param workflowBean
     * @param data
     * @return
     */
    public abstract Map<String, Object> getFlowVariable(Map<String, Object> workflowBean, Object data);

    /**
     * 工作流流转
     * @param workflowBean
     * @param extendData
     * @return
     */
    public abstract Integer updateWorkflow(WorkflowBean workflowBean, Map<String, Object> extendData);
}
