package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectWeeklyDao;
import com.jc.csmp.project.domain.CmProjectWeekly;
import com.jc.csmp.project.service.ICmProjectWeeklyItemService;
import com.jc.csmp.project.service.ICmProjectWeeklyService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 建设管理-周报管理serviceImpl
 * @Author administrator
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectWeeklyServiceImpl extends BaseServiceImpl<CmProjectWeekly> implements ICmProjectWeeklyService {

	private ICmProjectWeeklyDao projectWeeklyDao;
	@Autowired
	private ICmProjectWeeklyItemService cmProjectWeeklyItemService;
	@Autowired
	private IMobileReportLogService mobileReportLogService;
	@Autowired
	private IUploadService uploadService;
	public CmProjectWeeklyServiceImpl(){}

	@Autowired
	public CmProjectWeeklyServiceImpl(ICmProjectWeeklyDao projectWeeklyDao){
		super(projectWeeklyDao);
		this.projectWeeklyDao = projectWeeklyDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectWeekly entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectWeeklyDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectWeekly entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			uploadService.managerForAttach(entity.getId(), "cm_project_weekly", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectWeekly entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			if (entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
				entity.setIfRelease(1);
				entity.setReleaseDate(new Date(System.currentTimeMillis()));
			}
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_weekly", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
				if (entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
					cmProjectWeeklyItemService.updatePlanProgress(entity.getId());
				}
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
	public CmProjectWeekly getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectWeekly param = new CmProjectWeekly();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.projectWeeklyDao.queryAll(param));
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public MobileApiResponse mobileReport(CmProjectWeekly entity, User user) {
		try {
			if (StringUtil.isEmpty(entity.getId())) {
				//新增
				entity.setStatus(0);
				entity.setIfRelease(0);
				if (entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
					entity.setIfRelease(1);
					entity.setReleaseDate(new Date(System.currentTimeMillis()));
				}
				entity.setUserId(user.getId());
				entity.setDeptId(user.getDeptId());
				MobileApiResponse apiResponse = MobileApiResponse.fromResult(this.saveEntity(entity));
				if (apiResponse.isSuccess() && !StringUtil.isEmpty(entity.getWeeklyItemIds())) {
					//更新周报事项的weeklyId
					this.cmProjectWeeklyItemService.updateWeeklyByIds(GlobalUtil.splitStr(entity.getWeeklyItemIds(), ','), ((CmProjectWeekly) apiResponse.get("body")).getId());
				}
				if (apiResponse.isSuccess() && entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
					//发布的情况，更新进度
					String weeklyId = ((CmProjectWeekly) apiResponse.get("body")).getId();
					cmProjectWeeklyItemService.updatePlanProgress(weeklyId);
					//记录周报发布日志
					this.mobileReportLogService.saveLog(MobileReportLogEnum.weekly, entity, user);
				}
				return apiResponse;
			} else {
				//修改
				if (entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
					entity.setIfRelease(1);
					entity.setReleaseDate(new Date(System.currentTimeMillis()));
					this.mobileReportLogService.saveLog(MobileReportLogEnum.weekly, entity, user);
				}
				CmProjectWeekly db = this.getById(entity.getId());
				if (db != null) {
					entity.setModifyDate(db.getModifyDate());
				}
				return MobileApiResponse.fromResult(this.updateEntity(entity));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return MobileApiResponse.error(ex.getMessage());
		}
	}

}

