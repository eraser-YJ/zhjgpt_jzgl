package com.jc.csmp.project.event;

import com.jc.csmp.common.enums.ProjectQuestionEnum;
import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectQuestionService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.enums.ResourceEnums;
import com.jc.system.content.service.IAttachService;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

import java.util.List;

/**
 * 问题审批通过
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmProjectQuestionAuditSuccessEvent implements EventInterface {
    private ICmProjectQuestionService cmProjectQuestionService = SpringContextHolder.getBean(ICmProjectQuestionService.class);
    private IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmProjectQuestion entity = new CmProjectQuestion();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmProjectQuestionService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.finish.toString());
                cmProjectQuestionService.update(entity);
                //获取要同步的附件信息

                //同步资源库
                if (entity.getQuestionType().equals(ProjectQuestionEnum.quality.toString())) {
                    entity.setAttachFile(attachService.getAttachIds(entity.getId(), "cm_project_question_" + ProjectQuestionEnum.quality.toString()));
                    ResourceEnums.pt_project_quality.getResourceService().rsyncData(entity);
                } else if (entity.getQuestionType().equals(ProjectQuestionEnum.safe.toString())) {
                    entity.setAttachFile(attachService.getAttachIds(entity.getId(), "cm_project_question_" + ProjectQuestionEnum.safe.toString()));
                    ResourceEnums.pt_project_safe.getResourceService().rsyncData(entity);
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
