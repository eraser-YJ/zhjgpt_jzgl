package com.jc.csmp.plan.dao;

import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.Attach;

import java.util.List;


/**
 * @title  
 * @version  
 */
 
public interface IProjectMonthPlanItemDao extends IBaseDao<ProjectMonthPlanItem>{
    List<Attach> getAttachList(ProjectMonthPlanItem cond);

    ProjectMonthPlanItem queryNewXxdj(ProjectMonthPlanItem cond);

    List<ProjectMonthPlanItem> queryNewQuestion(ProjectMonthPlanItem cond);

    ProjectMonthPlanItem queryNewNum(ProjectMonthPlanItem cond);
	
}
