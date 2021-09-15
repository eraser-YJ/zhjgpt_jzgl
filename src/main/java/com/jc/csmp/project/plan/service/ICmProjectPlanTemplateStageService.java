package com.jc.csmp.project.plan.service;

import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * 建设管理-项目计划模板阶段管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanTemplateStageService extends IBaseService<CmProjectPlanTemplateStage>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectPlanTemplateStage entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmProjectPlanTemplateStage entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectPlanTemplateStage entity) throws CustomException;

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	CmProjectPlanTemplateStage getById(String id);

	/**
	 * 根据id查询包含自己的全部子id
	 * @param id: 要查询的跟节点
	 * @param templateId: 模板id
	 * @return
	 */
	String[] getChildIdById(String id, String templateId);

	List<CmProjectPlanTemplateStage> selectByprojectId(CmProjectPlanTemplateStage cmProjectPlanTemplateStage);
}
