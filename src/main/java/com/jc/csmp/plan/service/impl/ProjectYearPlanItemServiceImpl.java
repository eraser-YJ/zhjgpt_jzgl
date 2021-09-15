package com.jc.csmp.plan.service.impl;

import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.dao.IProjectYearPlanItemDao;
import com.jc.csmp.plan.service.IProjectYearPlanItemService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
 * @Version 1.0
 */
@Service
public class ProjectYearPlanItemServiceImpl extends BaseServiceImpl<ProjectYearPlanItem> implements IProjectYearPlanItemService {

	private IProjectYearPlanItemDao cmProjectYearPlanItemDao;

	public ProjectYearPlanItemServiceImpl(){}

	@Autowired
	public ProjectYearPlanItemServiceImpl(IProjectYearPlanItemDao cmProjectYearPlanItemDao){
		super(cmProjectYearPlanItemDao);
		this.cmProjectYearPlanItemDao = cmProjectYearPlanItemDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ProjectYearPlanItem entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectYearPlanItemDao.delete(entity,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	public Integer deleteByHeadId(String headId) throws CustomException{
		ProjectYearPlanItem headCond = new ProjectYearPlanItem();
		headCond.setHeadId(headId);
		Integer result = -1;
		try{
			result = cmProjectYearPlanItemDao.delete(headCond,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(headCond);
			throw ce;
		}
		return result;
	}


	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ProjectYearPlanItem entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(ProjectYearPlanItem entity) throws CustomException {
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

}

