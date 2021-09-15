package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplate;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-项目计划模板管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanTemplateServiceImpl extends BaseServiceImpl<CmProjectPlanTemplate> implements ICmProjectPlanTemplateService {

	private ICmProjectPlanTemplateDao cmProjectPlanTemplateDao;

	public CmProjectPlanTemplateServiceImpl(){}

	@Autowired
	public CmProjectPlanTemplateServiceImpl(ICmProjectPlanTemplateDao cmProjectPlanTemplateDao){
		super(cmProjectPlanTemplateDao);
		this.cmProjectPlanTemplateDao = cmProjectPlanTemplateDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlanTemplate entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPlanTemplateDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlanTemplate entity) throws CustomException {
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
	public Result updateEntity(CmProjectPlanTemplate entity) throws CustomException {
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
	public CmProjectPlanTemplate getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlanTemplate param = new CmProjectPlanTemplate();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectPlanTemplateDao.queryAll(param));
	}
}
