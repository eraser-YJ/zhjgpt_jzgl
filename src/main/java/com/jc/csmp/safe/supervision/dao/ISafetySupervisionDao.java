package com.jc.csmp.safe.supervision.dao;

import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;


/**
 * @title  
 * @version  
 */
 
public interface ISafetySupervisionDao extends IBaseDao<SafetySupervision>{

 public PageManager workFlowTodoQueryByPage(SafetySupervision cond, PageManager page) ;

 public PageManager workFlowDoneQueryByPage(SafetySupervision cond, PageManager page) ;

 public PageManager workFlowInstanceQueryByPage(SafetySupervision cond, PageManager page) ;
}
