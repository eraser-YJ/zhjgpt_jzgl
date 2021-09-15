package com.jc.csmp.contract.info.dao.impl;

import com.jc.csmp.contract.info.dao.ICmContractInfoDao;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-合同管理DaoImpl
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmContractInfoDaoImpl extends BaseClientDaoImpl<CmContractInfo> implements ICmContractInfoDao{

	public CmContractInfoDaoImpl(){}

	@Override
	public PageManager workFlowTodoQueryByPage(CmContractInfo projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowTodoQueryCount", "workflowTodoQuery");
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmContractInfo projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowDoneQueryCount", "workflowDoneQuery");
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmContractInfo projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowInstanceQueryCount", "workflowInstanceQuery");
	}

	@Override
	public Integer updateTotalPayment(CmContractInfo entity) {
		return this.getTemplate().update(getNameSpace(entity) + ".updateTotalPayment", entity);
	}

}