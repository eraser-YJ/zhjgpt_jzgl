package com.jc.csmp.safe.supervision.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.safe.supervision.dao.ISafetySupervisionDao;
import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.domain.PageManager;
/**
 * @title   
 * @version
 */
@Repository
public class SafetySupervisionDaoImpl extends BaseClientDaoImpl<SafetySupervision> implements ISafetySupervisionDao{

	public SafetySupervisionDaoImpl(){}

    @Override
    public PageManager workFlowTodoQueryByPage(SafetySupervision cond, PageManager page) {
        return queryByPage(cond,page,"workflowTodoQueryCount","workflowTodoQuery");
    }


    @Override
    public PageManager workFlowDoneQueryByPage(SafetySupervision cond, PageManager page) {
        return queryByPage(cond,page,"workflowDoneQueryCount","workflowDoneQuery");
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(SafetySupervision cond, PageManager page) {
        return queryByPage(cond,page,"workflowInstanceQueryCount","workflowInstanceQuery");
    }


}