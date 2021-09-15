package com.jc.csmp.project.service.impl;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.project.dao.ICmProjectVisaOrderDao;
import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.csmp.project.service.ICmProjectVisaOrderService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 建设管理-工程签证单管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectVisaOrderServiceImpl extends BaseServiceImpl<CmProjectVisaOrder> implements ICmProjectVisaOrderService {

	@Autowired
	IInstanceService instanceService;

	@Autowired
	ITaskService taskService;
	@Autowired
	private IUploadService uploadService;

	private ICmProjectVisaOrderDao projectVisaOrderDao;

	public CmProjectVisaOrderServiceImpl(){}

	@Autowired
	public CmProjectVisaOrderServiceImpl(ICmProjectVisaOrderDao projectVisaOrderDao){
		super(projectVisaOrderDao);
		this.projectVisaOrderDao = projectVisaOrderDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectVisaOrder entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectVisaOrderDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectVisaOrder entity) throws CustomException {
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
	public Result updateEntity(CmProjectVisaOrder entity) throws CustomException {
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
	public CmProjectVisaOrder getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectVisaOrder param = new CmProjectVisaOrder();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.projectVisaOrderDao.queryAll(param));
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer saveWorkflow(CmProjectVisaOrder entity)  throws CustomException {
		propertyService.fillProperties(entity, false);
		entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
		entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		Integer result = projectVisaOrderDao.save(entity);
		uploadService.managerForAttach(entity.getId(), "cm_project_visa_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.qzd.toString());
		Map<String,Object> resultMap = instanceService.startProcess(entity.getWorkflowBean());
		if(resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")){
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateWorkflow(CmProjectVisaOrder entity) throws  CustomException {
		propertyService.fillProperties(entity, true);
		Integer result = projectVisaOrderDao.update(entity);
		uploadService.managerForAttach(entity.getId(), "cm_project_visa_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setBusiness_Key_(entity.getPiId());
		Map<String,Object> resultMap = taskService.excute(entity.getWorkflowBean());
		if(resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")){
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;
	}

	@Override
	public PageManager workFlowTodoQueryByPage(CmProjectVisaOrder cond, PageManager page) {
		return projectVisaOrderDao.workFlowTodoQueryByPage(cond, page);
	}

	@Override
	public PageManager workFlowDoneQueryByPage(CmProjectVisaOrder cond, PageManager page) {
		return projectVisaOrderDao.workFlowDoneQueryByPage(cond, page);
	}

	@Override
	public PageManager workFlowInstanceQueryByPage(CmProjectVisaOrder cond, PageManager page) {
		return projectVisaOrderDao.workFlowInstanceQueryByPage(cond, page);
	}
}

