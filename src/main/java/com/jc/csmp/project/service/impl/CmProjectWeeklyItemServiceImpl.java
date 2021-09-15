package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectWeeklyItemDao;
import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.service.ICmProjectWeeklyItemService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-周报事项serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectWeeklyItemServiceImpl extends BaseServiceImpl<CmProjectWeeklyItem> implements ICmProjectWeeklyItemService {

	private ICmProjectWeeklyItemDao cmProjectWeeklyItemDao;
	@Autowired
	private ICmProjectPlanService cmProjectPlanService;
	
	public CmProjectWeeklyItemServiceImpl(){}

	@Autowired
	public CmProjectWeeklyItemServiceImpl(ICmProjectWeeklyItemDao cmProjectWeeklyItemDao){
		super(cmProjectWeeklyItemDao);
		this.cmProjectWeeklyItemDao = cmProjectWeeklyItemDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectWeeklyItem entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectWeeklyItemDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectWeeklyItem entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity);
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectWeeklyItem entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				return Result.success(MessageUtils.getMessage("JC_SYS_003"), entity);
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
	public CmProjectWeeklyItem getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectWeeklyItem param = new CmProjectWeeklyItem();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectWeeklyItemDao.queryAll(param));
	}

	@Override
	public CmProjectWeeklyItem getByPlanAndWeekly(String weeklyId, String planId) {
		if (StringUtil.isEmpty(weeklyId) || StringUtil.isEmpty(planId)) {
			return null;
		}
		CmProjectWeeklyItem entity = new CmProjectWeeklyItem();
		entity.setWeeklyId(weeklyId);
		entity.setPlanId(planId);
		return GlobalUtil.getFirstItem(this.cmProjectWeeklyItemDao.queryAll(entity));
	}

	@Override
	public Result updatePlanProgress(String weeklyId) {
		CmProjectWeeklyItem entity = new CmProjectWeeklyItem();
		entity.setWeeklyId(weeklyId);
		List<CmProjectWeeklyItem> itemList = this.cmProjectWeeklyItemDao.queryAll(entity);
		if (itemList != null) {
			for (CmProjectWeeklyItem item : itemList) {
				try {
					CmProjectPlan plan = this.cmProjectPlanService.getById(item.getPlanId());
					this.cmProjectPlanService.modifyProgress(item.getPlanId(), item.getFinishRatio(), item.getFinishMoney(), plan.getActualStartDate(), plan.getActualEndDate());
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.success();
	}

	@Override
	public Result updateWeeklyByIds(String[] ids, String weeklyId) {
		CmProjectWeeklyItem entity = new CmProjectWeeklyItem();
		entity.setWeeklyId(weeklyId);
		entity.setPrimaryKeys(ids);
		this.cmProjectWeeklyItemDao.updateWeeklyByIds(entity);
		return Result.success();
	}
}
