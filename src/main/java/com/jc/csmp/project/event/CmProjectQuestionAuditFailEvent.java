package com.jc.csmp.project.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectQuestionService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

/**
 * 问题审批未通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmProjectQuestionAuditFailEvent implements EventInterface {
    private ICmProjectQuestionService cmProjectQuestionService = SpringContextHolder.getBean(ICmProjectQuestionService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmProjectQuestion entity = new CmProjectQuestion();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmProjectQuestionService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.fail.toString());
                cmProjectQuestionService.update(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
