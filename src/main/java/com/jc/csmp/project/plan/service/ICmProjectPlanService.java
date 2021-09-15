package com.jc.csmp.project.plan.service;

import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建设管理-项目计划管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanService extends IBaseService<CmProjectPlan>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectPlan entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectPlan entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectPlan entity) throws CustomException;

	/**
	 * 更新进度
	 * @param id: id
	 * @param completionRatio：进度
	 * @param completionMoney: 金额
	 * @param startDate: 实际开始时间
	 * @param endDate: 实际结束时间
	 * @return
	 * @throws CustomException
	 */
	Result modifyProgress(String id, Integer completionRatio, BigDecimal completionMoney, Date startDate, Date endDate) throws CustomException;

	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	CmProjectPlan getById(String id);

	/**
	 * 根据模板自动将模板的内容写入计划中
	 * @param projectId
	 * @param templateId
	 * @return
	 */
    Result saveEntityByTemplateId(String projectId, String templateId) throws Exception;

	/**
	 * 根据模板将模板的阶段保存到项目中
	 * @param templateId
	 * @param projectId
	 */
	void saveByTemplateStage(String templateId, String projectId);
}
