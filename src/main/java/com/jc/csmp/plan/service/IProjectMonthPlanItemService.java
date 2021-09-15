package com.jc.csmp.plan.service;

import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * @Version 1.0
 */
public interface IProjectMonthPlanItemService extends IBaseService<ProjectMonthPlanItem>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ProjectMonthPlanItem entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ProjectMonthPlanItem entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ProjectMonthPlanItem entity) throws CustomException;

	void deleteByHeadId(String headId)throws CustomException;


	List<Attach> getAttachList(ProjectMonthPlanItem cond) ;

	ProjectMonthPlanItem queryNewXxdj(ProjectMonthPlanItem cond) ;

	List<ProjectMonthPlanItem> queryNewQuestion(ProjectMonthPlanItem cond) ;

	ProjectMonthPlanItem queryNewNum(ProjectMonthPlanItem cond);
}
