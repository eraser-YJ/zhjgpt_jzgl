package com.jc.csmp.contract.info.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.contract.info.dao.ICmContractInfoDao;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;

import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;

/**
 * 建设管理-合同管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmContractInfoServiceImpl extends BaseServiceImpl<CmContractInfo> implements ICmContractInfoService {

	private ICmContractInfoDao cmContractInfoDao;
	@Autowired
	private IInstanceService instanceService;
	@Autowired
	private IUploadService uploadService;
	@Autowired
	private ITaskService taskService;

	public CmContractInfoServiceImpl(){}

	@Autowired
	public CmContractInfoServiceImpl(ICmContractInfoDao cmContractInfoDao){
		super(cmContractInfoDao);
		this.cmContractInfoDao = cmContractInfoDao;
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
	public Integer saveWorkflow(CmContractInfo entity) throws CustomException {
		propertyService.fillProperties(entity, false);
		entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
		Integer result = cmContractInfoDao.save(entity);
		uploadService.managerForAttach(entity.getId(), "cm_contract_info", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.htba.toString());
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
	public Integer updateWorkflow(CmContractInfo entity) throws  CustomException {
		propertyService.fillProperties(entity, true);
		Integer result = cmContractInfoDao.update(entity);
		uploadService.managerForAttach(entity.getId(), "cm_contract_info", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
		entity.getWorkflowBean().setBusiness_Key_(entity.getPiId());
		Map<String,Object> resultMap = taskService.excute(entity.getWorkflowBean());
		if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
			return Integer.parseInt(resultMap.get("code").toString());
		}
		return result;
	}


	@Override
	public PageManager workFlowTodoQueryByPage(CmContractInfo entity, PageManager page) {
		return cmContractInfoDao.workFlowTodoQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmContractInfo entity, PageManager page) {
		return cmContractInfoDao.workFlowDoneQueryByPage(entity, page);
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmContractInfo entity, PageManager page) {
		return cmContractInfoDao.workFlowInstanceQueryByPage(entity, page);
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
	public Integer deleteByIds(CmContractInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity, true);
			result = cmContractInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;

	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmContractInfo entity) throws CustomException {
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
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	public CmContractInfo getById(String contractId) {
		if (StringUtil.isEmpty(contractId)) {
			return null;
		}
		CmContractInfo param = new CmContractInfo();
		param.setId(contractId);
		return GlobalUtil.getFirstItem(this.cmContractInfoDao.queryAll(param));
	}

	@Override
	public Result updateTotalPayment(String id, BigDecimal totalPayment) {
		if (StringUtil.isEmpty(id) || totalPayment == null) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		CmContractInfo entity = new CmContractInfo();
		entity.setId(id);
		entity.setTotalPayment(totalPayment);
		this.cmContractInfoDao.updateTotalPayment(entity);
		return Result.success();
	}

	@Override
	public Result updateContractMoney(String id, BigDecimal changeAmount) {
		if (StringUtil.isEmpty(id) || changeAmount == null) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		CmContractInfo entity = this.getById(id);
		if (entity == null) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		BigDecimal contractMoney = entity.getContractMoney() == null ? new BigDecimal(0) : entity.getContractMoney();
		BigDecimal totalPayment = entity.getTotalPayment() == null ? new BigDecimal(0) : entity.getTotalPayment();
		//验证未支付的合同金额是否满足改变的金额
		BigDecimal result = GlobalUtil.subtract(contractMoney, totalPayment);
		result = GlobalUtil.add(result, changeAmount);
		if (result.compareTo(new BigDecimal(0)) == -1) {
			return Result.failure(GlobalContext.CUSTOM_SIGN_ERROR, "修改后合同金额小于0，无法修改");
		}
		entity.setContractMoney(GlobalUtil.add(contractMoney, changeAmount));
		try {
			this.cmContractInfoDao.update(entity);
		} catch (DBException e) {
			e.printStackTrace();
			return Result.failure(GlobalContext.CUSTOM_SIGN_ERROR, e.getMessage());
		}
		return Result.success();
	}
}

