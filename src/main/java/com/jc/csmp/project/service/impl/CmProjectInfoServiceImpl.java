package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectInfoDao;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectDataAuthService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-项目管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectInfoServiceImpl extends BaseServiceImpl<CmProjectInfo> implements ICmProjectInfoService {

	private ICmProjectInfoDao cmProjectInfoDao;
	@Autowired
	private IUploadService uploadService;
	@Autowired
	private ICmProjectPersonService cmProjectPersonService;
	@Autowired
	private ICmProjectDataAuthService cmProjectDataAuthService;

	public CmProjectInfoServiceImpl(){}

	@Autowired
	public CmProjectInfoServiceImpl(ICmProjectInfoDao cmProjectInfoDao){
		super(cmProjectInfoDao);
		this.cmProjectInfoDao = cmProjectInfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectInfo entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			entity.setIfFinish("0");
			this.save(entity);
			this.cmProjectPersonService.saveAutoData(entity.getId(), entity.getSuperviseDeptId(), entity.getBuildDeptId());
			cmProjectDataAuthService.saveOrUpdate(entity.getId(), new String[]{entity.getSuperviseDeptId(), entity.getBuildDeptId()});
			uploadService.managerForAttach(entity.getId(), "cm_project_info", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			//同步项目到资源库
			ResourceEnums.pt_project_info.getResourceService().rsyncData(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectInfo entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				this.cmProjectPersonService.saveAutoData(entity.getId(), entity.getSuperviseDeptId(), entity.getBuildDeptId());
				cmProjectDataAuthService.saveOrUpdate(entity.getId(), new String[]{entity.getSuperviseDeptId(), entity.getBuildDeptId()});
				uploadService.managerForAttach(entity.getId(), "cm_project_info", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
				//同步项目到资源库
				ResourceEnums.pt_project_info.getResourceService().rsyncData(entity);
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
	public Result checkProjectNumber(String id, String projectNumber) {
		if (StringUtil.isEmpty(projectNumber)) {
			return Result.success();
		}
		CmProjectInfo param = new CmProjectInfo();
		param.setProjectNumber(projectNumber);
		List<CmProjectInfo> entityList = this.cmProjectInfoDao.queryAll(param);
		if (entityList == null || entityList.size() == 0) {
			return Result.success();
		}
		boolean result = true;
		for (CmProjectInfo entity : entityList) {
			if (entity.getProjectNumber().equals(projectNumber)) {
				if (id == null || !id.equals(entity.getId())) {
					result = false;
					break;
				}
			}
		}
		if (result) {
			return Result.success();
		}
		return Result.failure(1, "项目编号已存在");
	}

	@Override
	public CmProjectInfo getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectInfo param = new CmProjectInfo();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectInfoDao.queryAll(param));
	}

	@Override
	public CmProjectInfo getbyProjectNumber(String projectNumber) {
		if (StringUtil.isEmpty(projectNumber)) {
			return null;
		}
		CmProjectInfo entity = new CmProjectInfo();
		entity.setProjectNumber(projectNumber);
		return GlobalUtil.getFirstItem(this.cmProjectInfoDao.queryAll(entity));
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result finishProject(String projectNumbers) throws Exception {
		if (StringUtil.isEmpty(projectNumbers)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		this.cmProjectInfoDao.finishByProjectNumbers(projectNumbers);
		return Result.success();
	}

	@Override
	public List<CmProjectInfo> getListBySuperviseDeptId(String deptId) {
		CmProjectInfo entity = new CmProjectInfo();
		entity.setIfFinish("0");
		entity.setSuperviseDeptId(deptId);
		return this.cmProjectInfoDao.queryAll(entity);
	}


	@Override
	public PageManager queryProjectSafety(CmProjectInfo o, PageManager page) {
		 return this.cmProjectInfoDao.queryProjectSafety(o,page);
	}
}
