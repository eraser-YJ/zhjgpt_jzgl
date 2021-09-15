package com.jc.csmp.plan.service;

import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * @Version 1.0
 */
public interface IProjectYearPlanItemService extends IBaseService<ProjectYearPlanItem>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ProjectYearPlanItem entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ProjectYearPlanItem entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ProjectYearPlanItem entity) throws CustomException;

	Integer deleteByHeadId(String headId) throws CustomException;

}
