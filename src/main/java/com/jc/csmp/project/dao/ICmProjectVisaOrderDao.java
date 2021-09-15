package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-工程签证单管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectVisaOrderDao extends IBaseDao<CmProjectVisaOrder> {
    PageManager workFlowTodoQueryByPage(CmProjectVisaOrder cond, PageManager page) ;
    PageManager workFlowDoneQueryByPage(CmProjectVisaOrder cond, PageManager page) ;
    PageManager workFlowInstanceQueryByPage(CmProjectVisaOrder cond, PageManager page) ;
}
