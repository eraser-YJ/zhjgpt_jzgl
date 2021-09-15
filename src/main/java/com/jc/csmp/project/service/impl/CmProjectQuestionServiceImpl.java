package com.jc.csmp.project.service.impl;

import com.jc.csmp.common.enums.ProjectQuestionEnum;
import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.project.dao.ICmProjectQuestionDao;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectQuestionService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.workflow.external.WorkflowBean;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 建设管理-工程问题管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectQuestionServiceImpl extends BaseServiceImpl<CmProjectQuestion> implements ICmProjectQuestionService {

	@Autowired
	IInstanceService instanceService;

	@Autowired
	ITaskService taskService;
	@Autowired
	private IUploadService uploadService;
	private ICmProjectQuestionDao cmProjectQuestionDao;
	@Autowired
	private IMobileReportLogService mobileReportLogService;

	public CmProjectQuestionServiceImpl(){}

	@Autowired
	public CmProjectQuestionServiceImpl(ICmProjectQuestionDao cmProjectQuestionDao){
		super(cmProjectQuestionDao);
		this.cmProjectQuestionDao = cmProjectQuestionDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectQuestion entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectQuestionDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectQuestion entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			uploadService.managerForAttach(entity.getId(), "cm_project_question", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectQuestion entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_question", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
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
	public CmProjectQuestion getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectQuestion param = new CmProjectQuestion();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmProjectQuestionDao.queryAll(param));
	}

	/**
	* @description 发起流程方法
	* @return Integer 增加的记录数
	* @author
	* @version  2020-07-09
	*/
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer saveWorkflow(CmProjectQuestion entity)  throws CustomException {
		propertyService.fillProperties(entity, false);
		entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
		entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		Integer result = cmProjectQuestionDao.save(entity);
		uploadService.managerForAttach(entity.getId(), ProjectQuestionEnum.getByCode(entity.getQuestionType()).getBusinessTable(), entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		if (entity.getQuestionType().equals("quality")) {
			entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.zlwt.toString());
		} else if (entity.getQuestionType().equals("safe")) {
			entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.aqwt.toString());
		} else if (entity.getQuestionType().equals("scene")) {
			entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.xcwt.toString());
		}
		Map<String,Object> resultMap = instanceService.startProcess(entity.getWorkflowBean());
		if(resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")){
			return Integer.parseInt(resultMap.get("code").toString());
		}
		mobileReportLogService.saveLog(MobileReportLogEnum.question, entity, SystemSecurityUtils.getUser());
		return result;

	}

	/**
	* @description 修改方法
	* @return Integer 修改的记录数量
	* @author
	* @version  2020-07-09
	*/
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateWorkflow(CmProjectQuestion entity) throws  CustomException {
		propertyService.fillProperties(entity, true);
		CmProjectQuestion db = this.getById(entity.getId());
		Integer result = cmProjectQuestionDao.update(entity);
		WorkflowBean bean = entity.getWorkflowBean();
		uploadService.managerForAttach(entity.getId(), ProjectQuestionEnum.getByCode(db.getQuestionType()).getBusinessTable(), entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setBusiness_Key_(entity.getPiId());
		Map<String,Object> resultMap = taskService.excute(entity.getWorkflowBean());
		if(resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")){
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;
	}

	@Override
	public PageManager workFlowTodoQueryByPage(CmProjectQuestion entity, PageManager page) {
		return cmProjectQuestionDao.workFlowTodoQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmProjectQuestion entity, PageManager page) {
		return cmProjectQuestionDao.workFlowDoneQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmProjectQuestion entity, PageManager page) {
		return cmProjectQuestionDao.workFlowInstanceQueryByPage(entity, page);
	}

}

