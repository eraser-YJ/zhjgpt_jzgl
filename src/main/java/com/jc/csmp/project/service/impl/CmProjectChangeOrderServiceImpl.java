package com.jc.csmp.project.service.impl;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.project.dao.ICmProjectChangeOrderDao;
import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.csmp.project.service.ICmProjectChangeOrderService;
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
 * 建设管理-工程变更单管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectChangeOrderServiceImpl extends BaseServiceImpl<CmProjectChangeOrder> implements ICmProjectChangeOrderService {

	private ICmProjectChangeOrderDao cmPprojectChangeOrderDao;

	@Autowired
	private IInstanceService instanceService;
	@Autowired
	private IUploadService uploadService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private ICmContractInfoService cmContractInfoService;

	public CmProjectChangeOrderServiceImpl(){}

	@Autowired
	public CmProjectChangeOrderServiceImpl(ICmProjectChangeOrderDao cmPprojectChangeOrderDao){
		super(cmPprojectChangeOrderDao);
		this.cmPprojectChangeOrderDao = cmPprojectChangeOrderDao;
	}

	/**
	 * @description 发起流程方法
	 * @param entity 实体类
	 * @return Integer 增加的记录数
	 * @author
	 * @version  2020-07-09 
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer saveWorkflow(CmProjectChangeOrder entity)  throws CustomException {
		propertyService.fillProperties(entity, false);
		entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
		Integer result = cmPprojectChangeOrderDao.save(entity);
		uploadService.managerForAttach(entity.getId(), "cm_project_change_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		if (entity.getChangeType() != null && entity.getChangeType().equals("1")) {
			entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.gcbgd.toString());
		} else {
			entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.htbgd.toString());
		}
		Map<String,Object> resultMap = instanceService.startProcess(entity.getWorkflowBean());
		if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;

	}

	/**
	 * @description 修改方法
	 * @param entity 实体类
	 * @return Integer 修改的记录数量
	 * @author
	 * @version  2020-07-09 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateWorkflow(CmProjectChangeOrder entity) throws  CustomException {
		propertyService.fillProperties(entity, true);
		Integer result = cmPprojectChangeOrderDao.update(entity);
		uploadService.managerForAttach(entity.getId(), "cm_project_change_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setBusiness_Key_(entity.getPiId());
		Map<String,Object> resultMap = taskService.excute(entity.getWorkflowBean());
		if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;
	}


	@Override
	public PageManager workFlowTodoQueryByPage(CmProjectChangeOrder entity, PageManager page) {
		return cmPprojectChangeOrderDao.workFlowTodoQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmProjectChangeOrder entity, PageManager page) {
		return cmPprojectChangeOrderDao.workFlowDoneQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmProjectChangeOrder entity, PageManager page) {
		return cmPprojectChangeOrderDao.workFlowInstanceQueryByPage(entity, page);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectChangeOrder entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmPprojectChangeOrderDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectChangeOrder entity) throws CustomException {
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
	public Result updateEntity(CmProjectChangeOrder entity) throws CustomException {
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
	public CmProjectChangeOrder getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectChangeOrder param = new CmProjectChangeOrder();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmPprojectChangeOrderDao.queryAll(param));
	}
}

