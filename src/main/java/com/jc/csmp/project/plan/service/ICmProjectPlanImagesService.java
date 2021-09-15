package com.jc.csmp.project.plan.service;

import com.jc.csmp.project.domain.CmProjectWeekly;
import com.jc.csmp.project.plan.domain.CmProjectPlanImages;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * 建设管理-形象进度service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanImagesService extends IBaseService<CmProjectPlanImages>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectPlanImages entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectPlanImages entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectPlanImages entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmProjectPlanImages getById(String id);

	/**
	 * 根据id获取列表
	 * @param projectId
	 * @return
	 */
	List<CmProjectPlanImages> getByProjectId(String projectId);
}
