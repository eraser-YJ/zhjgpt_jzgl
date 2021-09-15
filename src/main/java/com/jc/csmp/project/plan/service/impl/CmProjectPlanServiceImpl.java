package com.jc.csmp.project.plan.service.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanDao;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateTask;
import com.jc.csmp.project.plan.service.*;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 建设管理-项目计划管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPlanServiceImpl extends BaseServiceImpl<CmProjectPlan> implements ICmProjectPlanService {

	private ICmProjectPlanDao projectPlanDao;
	@Autowired
	public ICmProjectPlanTemplateTaskService cmProjectPlanTemplateTaskService;
	@Autowired
	public ICmProjectPlanStageService cmProjectPlanStageService;
	@Autowired
	public ICmProjectPlanTemplateStageService cmProjectPlanTemplateStageService;
	@Autowired
	public ICmProjectPlanTemplateService cmProjectPlanTemplateService;
	@Autowired
	private IUploadService uploadService;

	public CmProjectPlanServiceImpl(){}

	@Autowired
	public CmProjectPlanServiceImpl(ICmProjectPlanDao projectPlanDao){
		super(projectPlanDao);
		this.projectPlanDao = projectPlanDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPlan entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectPlanDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPlan entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			uploadService.managerForAttach(entity.getId(), "cm_project_plan", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectPlan entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_plan", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
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
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result modifyProgress(String id, Integer completionRatio, BigDecimal completionMoney, Date startDate, Date endDate) throws CustomException {
		if (StringUtil.isEmpty(id) || completionRatio == null) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmProjectPlan entity = this.getById(id);
		if (entity == null) {
			return Result.failure(ResultCode.RESULE_DATA_NONE);
		}
		//TODO 完成金额暂时不用更新了注释掉
		//entity.setCompletionMoney(completionMoney);
		entity.setCompletionRatio(completionRatio);
		//进度比例不是0，且实际开始时间为空，默认设置上当前值
		if (completionRatio != null && completionRatio.doubleValue() > 0 && startDate == null) {
			startDate = new Date(System.currentTimeMillis());
		}
		//进度为100，实际结束时间为空，默认实际结束时间设置当前时间
		if (completionRatio != null && completionRatio.intValue() == 100 && endDate == null) {
			endDate = new Date(System.currentTimeMillis());
		}
		entity.setActualStartDate(startDate);
		entity.setActualEndDate(endDate);
		Integer flag = this.update(entity);
		if (flag == 1) {
			return Result.success(MessageUtils.getMessage("JC_SYS_003"), entity);
		} else {
			return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
		}
	}

	@Override
	public CmProjectPlan getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPlan entity = new CmProjectPlan();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.projectPlanDao.queryAll(entity));
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntityByTemplateId(String projectId, String templateId) throws Exception {
		if (StringUtil.isEmpty(templateId) || StringUtil.isEmpty(projectId)) {
			return Result.failure(ResultCode.PARAM_NOT_COMPLETE);
		}
		CmProjectPlanTemplateStage cmProjectPlanTemplateStage =new CmProjectPlanTemplateStage();
		cmProjectPlanTemplateStage.setTemplateId(templateId);
		List<CmProjectPlanTemplateStage> cmProjectPlanTemplateStagesList = this.cmProjectPlanTemplateStageService.selectByprojectId(cmProjectPlanTemplateStage);
		List<CmProjectPlanStage> cmProjectPlanStageList = new ArrayList<>();
		for (int i=0;i<cmProjectPlanTemplateStagesList.size();i++){
			if("0".equals(cmProjectPlanTemplateStagesList.get(i).getParentId())){
				String id = cmProjectPlanTemplateStagesList.get(i).getId();
				cmProjectPlanTemplateStagesList.get(i).setId(createId());
				String newId = cmProjectPlanTemplateStagesList.get(i).getId();
				for(int j=0;j<cmProjectPlanTemplateStagesList.size();j++){
					//判断当前节点id是否等于父节点id
					if(id.equals(cmProjectPlanTemplateStagesList.get(j).getParentId())) {
						cmProjectPlanTemplateStagesList.get(j).setParentId(newId);
						cmProjectPlanTemplateStagesList.get(j).setId(createId());
					}
				}
			}
			CmProjectPlanStage cmProjectPlanStage = new CmProjectPlanStage();
			cmProjectPlanStage.setId(cmProjectPlanTemplateStagesList.get(i).getId());
			cmProjectPlanStage.setProjectId(projectId);
			cmProjectPlanStage.setStageName(cmProjectPlanTemplateStagesList.get(i).getStageName());
			cmProjectPlanStage.setParentId(cmProjectPlanTemplateStagesList.get(i).getParentId());
			cmProjectPlanStage.setQueue(cmProjectPlanTemplateStagesList.get(i).getQueue());
			cmProjectPlanStage.setDeleteFlag(cmProjectPlanTemplateStagesList.get(i).getDeleteFlag());
			cmProjectPlanStage.setCreateUser(cmProjectPlanTemplateStagesList.get(i).getCreateUser());
			cmProjectPlanStage.setCreateUserDept(cmProjectPlanTemplateStagesList.get(i).getCreateUserDept());
			cmProjectPlanStage.setCreateDate(cmProjectPlanTemplateStagesList.get(i).getCreateDate());
			cmProjectPlanStage.setCreateUserOrg(cmProjectPlanTemplateStagesList.get(i).getCreateUserOrg());
			cmProjectPlanStage.setModifyUser(cmProjectPlanTemplateStagesList.get(i).getModifyUser());
			cmProjectPlanStage.setModifyDate(cmProjectPlanTemplateStagesList.get(i).getModifyDate());
			cmProjectPlanStage.setExtStr1(cmProjectPlanTemplateStagesList.get(i).getExtStr1());
			cmProjectPlanStage.setExtStr2(cmProjectPlanTemplateStagesList.get(i).getExtStr2());
			cmProjectPlanStage.setExtStr3(cmProjectPlanTemplateStagesList.get(i).getExtStr3());
			cmProjectPlanStage.setExtStr4(cmProjectPlanTemplateStagesList.get(i).getExtStr4());
			cmProjectPlanStage.setExtStr5(cmProjectPlanTemplateStagesList.get(i).getExtStr5());
			cmProjectPlanStage.setExtDate1(cmProjectPlanTemplateStagesList.get(i).getExtDate1());
			cmProjectPlanStage.setExtDate2(cmProjectPlanTemplateStagesList.get(i).getExtDate2());
			cmProjectPlanStage.setExtNum1(cmProjectPlanTemplateStagesList.get(i).getExtNum1());
			cmProjectPlanStage.setExtNum2(cmProjectPlanTemplateStagesList.get(i).getExtNum2());
			cmProjectPlanStage.setExtNum2(cmProjectPlanTemplateStagesList.get(i).getExtNum2());
			cmProjectPlanStageList.add(cmProjectPlanStage);
		}
		this.cmProjectPlanStageService.saveList(cmProjectPlanStageList);
		//this.saveByTemplateStage(templateId, projectId);
		this.saveByTemplateStage(templateId, projectId);
		return Result.success();
	}
	private String createId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	@Override
	public void saveByTemplateStage(String templateId, String projectId) {
		CmProjectPlanTemplateTask cmProjectPlanTemplateTask =new CmProjectPlanTemplateTask();
		cmProjectPlanTemplateTask.setTemplateId(templateId);
		try {
			List<CmProjectPlanTemplateTask> cmProjectPlanTemplateTaskList =this.cmProjectPlanTemplateTaskService.queryAll(cmProjectPlanTemplateTask);
			List<CmProjectPlan> cmProjectPlanList = new ArrayList<>();
			for (int i=0;i<cmProjectPlanTemplateTaskList.size();i++) {
				cmProjectPlanTemplateTaskList.get(i).setId(createId());
				CmProjectPlan cmProjectPlan = new CmProjectPlan();
				cmProjectPlan.setId(cmProjectPlanTemplateTaskList.get(i).getId());
				cmProjectPlan.setProjectId(projectId);
				cmProjectPlan.setStageId(cmProjectPlanTemplateTaskList.get(i).getStageId());
				cmProjectPlan.setPlanCode(cmProjectPlanTemplateTaskList.get(i).getTaskNumber());
				cmProjectPlan.setPlanName(cmProjectPlanTemplateTaskList.get(i).getTaskName());
				cmProjectPlan.setStageName(cmProjectPlanTemplateTaskList.get(i).getStageName());
				cmProjectPlan.setQueue(cmProjectPlanTemplateTaskList.get(i).getQueue());
				cmProjectPlan.setDeleteFlag(cmProjectPlanTemplateTaskList.get(i).getDeleteFlag());
				cmProjectPlan.setCreateUser(cmProjectPlanTemplateTaskList.get(i).getCreateUser());
				cmProjectPlan.setCreateUserDept(cmProjectPlanTemplateTaskList.get(i).getCreateUserDept());
				cmProjectPlan.setCreateDate(cmProjectPlanTemplateTaskList.get(i).getCreateDate());
				cmProjectPlan.setCreateUserOrg(cmProjectPlanTemplateTaskList.get(i).getCreateUserOrg());
				cmProjectPlan.setModifyUser(cmProjectPlanTemplateTaskList.get(i).getModifyUser());
				cmProjectPlan.setModifyDate(cmProjectPlanTemplateTaskList.get(i).getModifyDate());
				cmProjectPlan.setExtStr1(cmProjectPlanTemplateTaskList.get(i).getExtStr1());
				cmProjectPlan.setExtStr2(cmProjectPlanTemplateTaskList.get(i).getExtStr2());
				cmProjectPlan.setExtStr3(cmProjectPlanTemplateTaskList.get(i).getExtStr3());
				cmProjectPlan.setExtStr4(cmProjectPlanTemplateTaskList.get(i).getExtStr4());
				cmProjectPlan.setExtStr5(cmProjectPlanTemplateTaskList.get(i).getExtStr5());
				cmProjectPlan.setExtDate1(cmProjectPlanTemplateTaskList.get(i).getExtDate1());
				cmProjectPlan.setExtDate2(cmProjectPlanTemplateTaskList.get(i).getExtDate2());
				cmProjectPlan.setExtNum1(cmProjectPlanTemplateTaskList.get(i).getExtNum1());
				cmProjectPlan.setExtNum2(cmProjectPlanTemplateTaskList.get(i).getExtNum2());
				cmProjectPlan.setExtNum2(cmProjectPlanTemplateTaskList.get(i).getExtNum2());
				cmProjectPlanList.add(cmProjectPlan);
			}
			this.saveList(cmProjectPlanList);
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
}

