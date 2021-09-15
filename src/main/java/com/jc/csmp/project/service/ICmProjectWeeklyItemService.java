package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-周报事项service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectWeeklyItemService extends IBaseService<CmProjectWeeklyItem>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectWeeklyItem entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmProjectWeeklyItem entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectWeeklyItem entity) throws CustomException;

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	CmProjectWeeklyItem getById(String id);

	/**
	 * 根据周报id和计划id查询
	 * @param weeklyId
	 * @param planId
	 * @return
	 */
	CmProjectWeeklyItem getByPlanAndWeekly(String weeklyId, String planId);

	/**
	 * 根据周报信息更新计划进度
	 * @param weeklyId
	 * @return
	 */
	Result updatePlanProgress(String weeklyId);

	/**
	 * 根据ids更新weeklyId
	 * @param ids
	 * @param weeklyId
	 * @return
	 */
	Result updateWeeklyByIds(String[] ids, String weeklyId);
}
