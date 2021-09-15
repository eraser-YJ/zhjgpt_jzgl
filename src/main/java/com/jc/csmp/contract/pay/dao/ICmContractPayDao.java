package com.jc.csmp.contract.pay.dao;

import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-合同支付管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmContractPayDao extends IBaseDao<CmContractPay>{
    /**
     *
     * @param entity
     * @param page
     * @return
     */
    PageManager workFlowTodoQueryByPage(CmContractPay entity, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowDoneQueryByPage(CmContractPay projectPlan, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowInstanceQueryByPage(CmContractPay projectPlan, PageManager page) ;
}
