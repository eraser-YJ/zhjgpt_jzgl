package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-工程变更单管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectChangeOrderDao extends IBaseDao<CmProjectChangeOrder>{

    /**
     *
     * @param entity
     * @param page
     * @return
     */
    PageManager workFlowTodoQueryByPage(CmProjectChangeOrder entity, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowDoneQueryByPage(CmProjectChangeOrder projectPlan, PageManager page) ;

    /**
     *
     * @param projectPlan
     * @param page
     * @return
     */
    PageManager workFlowInstanceQueryByPage(CmProjectChangeOrder projectPlan, PageManager page) ;
	
}
