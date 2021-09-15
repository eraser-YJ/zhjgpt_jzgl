package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-工程问题管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectQuestionDao extends IBaseDao<CmProjectQuestion> {
    PageManager workFlowTodoQueryByPage(CmProjectQuestion cond, PageManager page);
    PageManager workFlowDoneQueryByPage(CmProjectQuestion cond, PageManager page);
    PageManager workFlowInstanceQueryByPage(CmProjectQuestion cond, PageManager page) ;
}
