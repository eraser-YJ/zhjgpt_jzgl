package com.jc.csmp.plan.event;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

import java.util.Date;

/**
 * 通过实践
 */
public class PlanPassEvent implements EventInterface {

    // 流程主表
    private IProjectYearPlanService service = SpringContextHolder.getBean(IProjectYearPlanService.class);


    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        Long start = System.currentTimeMillis();
        String msg1 = start + " PlanPassEvent开始执行*****" + workflowBean.getBusiness_Key_() + "*****";
        System.out.println(msg1);
        try {
            ProjectYearPlan data = service.queryByPiid(workflowBean.getBusiness_Key_());
            if (data == null) {
                return false;
            }
            ProjectYearPlan newData = new ProjectYearPlan();
            newData.setId(data.getId());
            newData.setPlanStatus(20);
            newData.setModifyDateNew(new Date());
            service.update(newData);
            return true;
        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            String msg2 = start + " PlanPassEvent结束执行*****" + workflowBean.getBusiness_Key_() + "*****";
            System.out.println(msg2);
        }
    }
}
