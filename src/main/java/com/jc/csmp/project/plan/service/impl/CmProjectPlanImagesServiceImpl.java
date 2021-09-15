package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanImagesDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanImages;
import com.jc.csmp.project.plan.service.ICmProjectPlanImagesService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-项目形象进度serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanImagesServiceImpl extends BaseServiceImpl<CmProjectPlanImages> implements ICmProjectPlanImagesService {

	private ICmProjectPlanImagesDao cmProjectPlanImagesDao;

	@Autowired
	private IUploadService uploadService;

	public CmProjectPlanImagesServiceImpl(){}

	@Autowired
	public CmProjectPlanImagesServiceImpl(ICmProjectPlanImagesDao cmProjectPlanImagesDao){
		super(cmProjectPlanImagesDao);
		this.cmProjectPlanImagesDao = cmProjectPlanImagesDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlanImages entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPlanImagesDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlanImages entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			uploadService.managerForAttach(entity.getId(), "cm_project_plan_images", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectPlanImages entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_plan_images", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
				return Result.success(MessageUtils.getMessage("JC_SYS_003"));
			} else {
				return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"), entity);
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	public CmProjectPlanImages getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlanImages param = new CmProjectPlanImages();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectPlanImagesDao.queryAll(param));
	}

	@Override
	public List<CmProjectPlanImages> getByProjectId(String projectId) {
		CmProjectPlanImages entity = new CmProjectPlanImages();
		entity.setProjectId(projectId);
		entity.addOrderByField(" t.create_date ");
		return this.cmProjectPlanImagesDao.queryAll(entity);
	}
}

