package com.jc.csmp.plan.ctrl.service;

import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IProjectYearPlanCtrlService extends IBaseService<ProjectYearPlanCtrl>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ProjectYearPlanCtrl entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ProjectYearPlanCtrl entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ProjectYearPlanCtrl entity) throws CustomException;

	ProjectYearPlanCtrl getReqCrtl(int year);

	ProjectYearPlanCtrl getChangeCrtl(int year);
}
