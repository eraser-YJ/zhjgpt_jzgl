package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanStageDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设管理-项目计划阶段管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanStageServiceImpl extends BaseServiceImpl<CmProjectPlanStage> implements ICmProjectPlanStageService {

	private ICmProjectPlanStageDao cmProjectPlanStageDao;

	public CmProjectPlanStageServiceImpl(){}

	@Autowired
	public CmProjectPlanStageServiceImpl(ICmProjectPlanStageDao cmProjectPlanStageDao){
		super(cmProjectPlanStageDao);
		this.cmProjectPlanStageDao = cmProjectPlanStageDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlanStage entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPlanStageDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlanStage entity) throws CustomException {
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
	public Result updateEntity(CmProjectPlanStage entity) throws CustomException {
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
	public CmProjectPlanStage getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlanStage param = new CmProjectPlanStage();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectPlanStageDao.queryAll(param));
	}

	@Override
	public List<CmProjectPlanStage> queryGenttCharts(CmProjectPlanStage entity) {
		return this.cmProjectPlanStageDao.queryGenttCharts(entity);
	}

	@Override
	public String[] getChildIdById(String id, String projectId) {
		if (StringUtil.isEmpty(id)) {
			id = "0";
		}
		if (StringUtil.isEmpty(projectId)) {
			return new String[]{"1"};
		}
		CmProjectPlanStage param = new CmProjectPlanStage();
		param.setProjectId(projectId);
		List<CmProjectPlanStage> stageList = this.cmProjectPlanStageDao.queryAll(param);
		List<CmProjectPlanStage> dataList = new ArrayList<>();
		if (stageList != null && stageList.size() > 0) {
			if (id.equals("0")) {
				dataList.addAll(stageList);
			} else {
				for (CmProjectPlanStage entity : stageList) {
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
			for (CmProjectPlanStage entity : dataList) {
				array[index++] = entity.getId();
			}
			return array;
		}
		return new String[]{"1"};
	}

	private void convert(List<CmProjectPlanStage> stageList, List<CmProjectPlanStage> dataList, String parentId) {
		for (CmProjectPlanStage entity : stageList) {
			if (entity.getParentId().equals(parentId)) {
				dataList.add(entity);
				convert(stageList, dataList, entity.getId());
			}
		}
	}

	@Override
	public void saveByTemplateStage(List<CmProjectPlanTemplateStage> cmProjectPlanTemplateStageList) {
		this.cmProjectPlanStageDao.saveByTemplateStage(cmProjectPlanTemplateStageList);
	}
}
