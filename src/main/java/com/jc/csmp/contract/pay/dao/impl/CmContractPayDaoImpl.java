package com.jc.csmp.contract.pay.dao.impl;

import com.jc.csmp.contract.pay.dao.ICmContractPayDao;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-合同支付管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmContractPayDaoImpl extends BaseClientDaoImpl<CmContractPay> implements ICmContractPayDao {

	public CmContractPayDaoImpl(){}

	@Override
	public PageManager workFlowTodoQueryByPage(CmContractPay projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowTodoQueryCount", "workflowTodoQuery");
	}


	@Override
	public PageManager workFlowDoneQueryByPage(CmContractPay projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowDoneQueryCount", "workflowDoneQuery");
	}


	@Override
	public PageManager workFlowInstanceQueryByPage(CmContractPay projectPlan, PageManager page) {
		return queryByPage(projectPlan, page, "workflowInstanceQueryCount", "workflowInstanceQuery");
	}

}