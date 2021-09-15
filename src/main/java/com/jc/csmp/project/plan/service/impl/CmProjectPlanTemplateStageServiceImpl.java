package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateStageDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateTask;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateStageService;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateTaskService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import net.bytebuddy.asm.Advice;
import net.sf.ehcache.pool.sizeof.annotations.IgnoreSizeOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设管理-项目计划模板阶段管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanTemplateStageServiceImpl extends BaseServiceImpl<CmProjectPlanTemplateStage> implements ICmProjectPlanTemplateStageService {

	private ICmProjectPlanTemplateStageDao cmProjectPlanTemplateStageDao;
	@Autowired
	private ICmProjectPlanTemplateTaskService cmProjectPlanTemplateTaskService;

	public CmProjectPlanTemplateStageServiceImpl(){}

	@Autowired
	public CmProjectPlanTemplateStageServiceImpl(ICmProjectPlanTemplateStageDao cmProjectPlanTemplateStageDao){
		super(cmProjectPlanTemplateStageDao);
		this.cmProjectPlanTemplateStageDao = cmProjectPlanTemplateStageDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlanTemplateStage entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPlanTemplateStageDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlanTemplateStage entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			if (StringUtil.isEmpty(entity.getParentId())) {
				entity.setParentId("0");
			}
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
	public Result updateEntity(CmProjectPlanTemplateStage entity) throws CustomException {
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
	public CmProjectPlanTemplateStage getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlanTemplateStage param = new CmProjectPlanTemplateStage();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectPlanTemplateStageDao.queryAll(param));
	}

	@Override
	public String[] getChildIdById(String id, String templateId) {
		if (StringUtil.isEmpty(id)) {
			id = "0";
		}
		if (StringUtil.isEmpty(templateId)) {
			return new String[]{"1"};
		}
		CmProjectPlanTemplateStage param = new CmProjectPlanTemplateStage();
		param.setTemplateId(templateId);
		List<CmProjectPlanTemplateStage> stageList = this.cmProjectPlanTemplateStageDao.queryAll(param);
		List<CmProjectPlanTemplateStage> dataList = new ArrayList<>();
		if (stageList != null && stageList.size() > 0) {
			if (id.equals("0")) {
				dataList.addAll(stageList);
			} else {
				for (CmProjectPlanTemplateStage entity : stageList) {
					if (entity.getId().equals(id)) {
						dataList.add(entity);
						convert(stageList, dataList, id);
						break;
					}
				}
			}
		}
		int size = dataList.size();
		if (size > 0) {
			int index = 0;
			String[] array = new String[size];
			for (CmProjectPlanTemplateStage entity : dataList) {
				array[index++] = entity.getId();
			}
			return array;
		}
		return new String[]{"1"};
	}

	@Override
	public List<CmProjectPlanTemplateStage> selectByprojectId(CmProjectPlanTemplateStage cmProjectPlanTemplateStage) {
		return this.cmProjectPlanTemplateStageDao.selectByprojectId(cmProjectPlanTemplateStage);
	}

	private void convert(List<CmProjectPlanTemplateStage> stageList, List<CmProjectPlanTemplateStage> dataList, String parentId) {
		for (CmProjectPlanTemplateStage entity : stageList) {
			if (entity.getParentId().equals(parentId)) {
				dataList.add(entity);
				convert(stageList, dataList, entity.getId());
			}
		}
	}
}
