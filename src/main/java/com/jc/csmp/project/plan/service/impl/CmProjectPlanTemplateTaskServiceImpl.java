package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateTaskDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateTask;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateTaskService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-项目计划模板任务管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanTemplateTaskServiceImpl extends BaseServiceImpl<CmProjectPlanTemplateTask> implements ICmProjectPlanTemplateTaskService {

	private ICmProjectPlanTemplateTaskDao cmProjectPlanTemplateTaskDao;

	public CmProjectPlanTemplateTaskServiceImpl(){}

	@Autowired
	public CmProjectPlanTemplateTaskServiceImpl(ICmProjectPlanTemplateTaskDao cmProjectPlanTemplateTaskDao){
		super(cmProjectPlanTemplateTaskDao);
		this.cmProjectPlanTemplateTaskDao = cmProjectPlanTemplateTaskDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlanTemplateTask entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPlanTemplateTaskDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlanTemplateTask entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity.getId());
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectPlanTemplateTask entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				return Result.success(MessageUtils.getMessage("JC_SYS_003"));
			} else {
				return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	public CmProjectPlanTemplateTask getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlanTemplateTask param = new CmProjectPlanTemplateTask();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectPlanTemplateTaskDao.queryAll(param));
	}
}
