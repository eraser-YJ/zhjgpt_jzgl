package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectVisaOrderDao;
import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-工程签证单管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectVisaOrderDaoImpl extends BaseClientDaoImpl<CmProjectVisaOrder> implements ICmProjectVisaOrderDao {

	public CmProjectVisaOrderDaoImpl(){}

    @Override
    public PageManager workFlowTodoQueryByPage(CmProjectVisaOrder cond, PageManager page) {
        return queryByPage(cond,page,"workflowTodoQueryCount","workflowTodoQuery");
    }

    @Override
    public PageManager workFlowDoneQueryByPage(CmProjectVisaOrder cond, PageManager page) {
        return queryByPage(cond,page,"workflowDoneQueryCount","workflowDoneQuery");
    }

    @Override
    public PageManager workFlowInstanceQueryByPage(CmProjectVisaOrder cond, PageManager page) {
        return queryByPage(cond,page,"workflowInstanceQueryCount","workflowInstanceQuery");
    }

}