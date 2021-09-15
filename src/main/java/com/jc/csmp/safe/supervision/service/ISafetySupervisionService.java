package com.jc.csmp.safe.supervision.service;

import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Version 1.0
 */
public interface ISafetySupervisionService extends IBaseService<SafetySupervision>{

	Integer deleteByIds(SafetySupervision entity) throws CustomException;

	Result saveEntity(SafetySupervision entity) throws CustomException;

	Result updateEntity(SafetySupervision entity) throws CustomException;
	
	Integer saveWorkflow(SafetySupervision projectPlan)  throws CustomException ;
	
	Integer updateWorkflow(SafetySupervision cond) throws  CustomException ;

	PageManager workFlowTodoQueryByPage(SafetySupervision cond, PageManager page) ;

	PageManager workFlowDoneQueryByPage(SafetySupervision cond, PageManager page) ;

	PageManager workFlowInstanceQueryByPage(SafetySupervision cond, PageManager page) ;

	boolean createAdviceNote(SafetySupervision entity) throws CustomException;
	void downAdviceNote(SafetySupervision entity, HttpServletResponse response, HttpServletRequest request) throws CustomException;

	void exportExcelAdviceNote(SafetySupervision entity, HttpServletResponse response, HttpServletRequest request) throws CustomException, IOException;

}
