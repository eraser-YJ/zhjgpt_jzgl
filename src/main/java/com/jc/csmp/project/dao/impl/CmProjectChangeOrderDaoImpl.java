package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectChangeOrderDao;
import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-工程变更单管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectChangeOrderDaoImpl extends BaseClientDaoImpl<CmProjectChangeOrder> implements ICmProjectChangeOrderDao {

	public CmProjectChangeOrderDaoImpl(){}

    @Override
    public PageManager workFlowTodoQueryByPage(CmProjectChangeOrder entity, PageManager page) {
        return queryByPage(entity, page, "workflowTodoQueryCount", "workflowTodoQuery");
    }


    @Override
    public PageManager workFlowDoneQueryByPage(CmProjectChangeOrder entity, PageManager page) {
        return queryByPage(entity, page, "workflowDoneQueryCount", "workflowDoneQuery");
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(CmProjectChangeOrder entity, PageManager page) {
        return queryByPage(entity, page, "workflowInstanceQueryCount", "workflowInstanceQuery");
    }

}