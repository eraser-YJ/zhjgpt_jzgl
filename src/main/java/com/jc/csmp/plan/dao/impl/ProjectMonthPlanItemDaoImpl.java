package com.jc.csmp.plan.dao.impl;

import com.jc.csmp.plan.dao.IProjectMonthPlanItemDao;
import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.Attach;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectMonthPlanItemDaoImpl extends BaseClientDaoImpl<ProjectMonthPlanItem> implements IProjectMonthPlanItemDao {

	public ProjectMonthPlanItemDaoImpl(){}

	@Override
	public List<Attach> getAttachList(ProjectMonthPlanItem cond) {
		return this.getTemplate().selectList(this.getNameSpace(cond) + "." + "getAttachList", cond);
	}

	@Override
	public ProjectMonthPlanItem queryNewXxdj(ProjectMonthPlanItem cond) {
		return this.getTemplate().selectOne(this.getNameSpace(cond) + "." + "queryNewXxdj", cond);
	}

	@Override
	public List<ProjectMonthPlanItem> queryNewQuestion(ProjectMonthPlanItem cond) {
		return this.getTemplate().selectList(this.getNameSpace(cond) + "." + "queryNewQuestion", cond);
	}

	@Override
	public ProjectMonthPlanItem queryNewNum(ProjectMonthPlanItem cond) {
		return this.getTemplate().selectOne(this.getNameSpace(cond) + "." + "queryNewNum", cond);
	}
}