package com.jc.csmp.contract.pay.service.impl;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.contract.pay.dao.ICmContractPayDao;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.csmp.contract.pay.service.ICmContractPayService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.content.service.IUploadService;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 建设管理-合同支付管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmContractPayServiceImpl extends BaseServiceImpl<CmContractPay> implements ICmContractPayService {

	private ICmContractPayDao cmContractPayDao;
	@Autowired
	private IInstanceService instanceService;
	@Autowired
	private IUploadService uploadService;
	@Autowired
	private ICmContractInfoService cmContractInfoService;

	@Autowired
	private ITaskService taskService;

	public CmContractPayServiceImpl(){}

	@Autowired
	public CmContractPayServiceImpl(ICmContractPayDao cmContractPayDao){
		super(cmContractPayDao);
		this.cmContractPayDao = cmContractPayDao;
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
	public Integer saveWorkflow(CmContractPay entity)  throws CustomException {
		propertyService.fillProperties(entity, false);
		entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
		Integer result = cmContractPayDao.save(entity);
		uploadService.managerForAttach(entity.getId(), "cm_contract_pay", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.htzf.toString());
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
	public Integer updateWorkflow(CmContractPay entity) throws  CustomException {
		propertyService.fillProperties(entity, true);
		Integer result = cmContractPayDao.update(entity);
		uploadService.managerForAttach(entity.getId(), "cm_contract_pay", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setBusiness_Key_(entity.getPiId());
		Map<String,Object> resultMap = taskService.excute(entity.getWorkflowBean());
		if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;
	}


	@Override
	public PageManager workFlowTodoQueryByPage(CmContractPay entity, PageManager page) {
		return cmContractPayDao.workFlowTodoQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmContractPay entity, PageManager page) {
		return cmContractPayDao.workFlowDoneQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmContractPay entity, PageManager page) {
		return cmContractPayDao.workFlowInstanceQueryByPage(entity, page);
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param entity 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version  2020-07-09 
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmContractPay entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity, true);
			result = cmContractPayDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	public CmContractPay getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmContractPay param = new CmContractPay();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmContractPayDao.queryAll(param));
	}

	@Override
	public PageManager selectAuthQuery(CmContractPay entity, PageManager page) {
		return this.cmContractPayDao.queryByPage(entity, page, "authQueryCount", "authQuery");
	}
}

