package com.jc.csmp.workflow.enums.service.impl;

import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectQuestionService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.WorkflowBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流问题待办
 * @Author 常鹏
 * @Date 2020/8/21 10:53
 * @Version 1.0
 */
public class QuestionTodoServiceImpl extends IWorkflowTodoService {

    private ICmProjectQuestionService cmProjectQuestionService;

    public QuestionTodoServiceImpl() {
        cmProjectQuestionService = SpringContextHolder.getBean(ICmProjectQuestionService.class);
    }

    @Override
    public TodoExtendBean getByPid(String pid) {
        CmProjectQuestion entity = new CmProjectQuestion();
        entity.setPiId(pid);
        try {
            entity = cmProjectQuestionService.get(entity);
            if (entity != null) {
                String questionType = "";
                if (entity.getQuestionType().equals("quality")) {
                    questionType = "质量问题";
                } else if (entity.getQuestionType().equals("safe")) {
                    questionType = "安全问题";
                } else if (entity.getQuestionType().equals("scene")) {
                    questionType = "现场问题";
                }
                return TodoExtendBean.create(entity.getCode(), "标题为：" + entity.getTitle() + "的" + questionType + "申请");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAttachTableName() {
        return "cm_project_question";
    }

    @Override
    public Object getEntityByPid(String pid) {
        CmProjectQuestion entity = new CmProjectQuestion();
        entity.setPiId(pid);
        try {
            return cmProjectQuestionService.get(entity);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getFlowVariable(Map<String, Object> workflowBean, Object data) {
        CmProjectQuestion entity = (CmProjectQuestion) data;
        Map<String, Object> result = new HashMap<>(5);
        result.put("curNodeId_", workflowBean.get("curNodeId_"));
        result.put("definitionId_", workflowBean.get("definitionId_"));
        result.put("instanceId_", workflowBean.get("instanceId_"));
        //设置自动审批人
        entity.getExtStr1();
        result.put("workflowVar_[partaUnitLeaderId]", entity.getPartaUnitLeaderId());
        result.put("workflowVar_[superviseLeaderId]", entity.getSuperviseLeaderId());
        return result;
    }

    @Override
    public Integer updateWorkflow(WorkflowBean workflowBean, Map<String, Object> extendData) {
        CmProjectQuestion entity = new CmProjectQuestion();
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
            return cmProjectQuestionService.updateWorkflow(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}