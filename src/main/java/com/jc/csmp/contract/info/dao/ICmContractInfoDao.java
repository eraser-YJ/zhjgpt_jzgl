package com.jc.csmp.contract.info.dao;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-合同管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmContractInfoDao extends IBaseDao<CmContractInfo>{
    /**
     *
     * @param entity
     * @param page
     * @return
     */
    PageManager workFlowTodoQueryByPage(CmContractInfo entity, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowDoneQueryByPage(CmContractInfo projectPlan, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowInstanceQueryByPage(CmContractInfo projectPlan, PageManager page) ;

    /**
     * 更新合同支付总额
     * @param entity
     * @return
     */
    Integer updateTotalPayment(CmContractInfo entity);
}
