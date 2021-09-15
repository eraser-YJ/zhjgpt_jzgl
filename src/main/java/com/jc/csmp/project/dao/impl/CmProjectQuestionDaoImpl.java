package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectQuestionDao;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-工程问题管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectQuestionDaoImpl extends BaseClientDaoImpl<CmProjectQuestion> implements ICmProjectQuestionDao {

	public CmProjectQuestionDaoImpl(){}

    @Override
    public PageManager workFlowTodoQueryByPage(CmProjectQuestion cond, PageManager page) {
        return queryByPage(cond,page,"workflowTodoQueryCount","workflowTodoQuery");
    }


    @Override
    public PageManager workFlowDoneQueryByPage(CmProjectQuestion cond, PageManager page) {
        return queryByPage(cond,page,"workflowDoneQueryCount","workflowDoneQuery");
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(CmProjectQuestion cond, PageManager page) {
        return queryByPage(cond,page,"workflowInstanceQueryCount","workflowInstanceQuery");
    }


}