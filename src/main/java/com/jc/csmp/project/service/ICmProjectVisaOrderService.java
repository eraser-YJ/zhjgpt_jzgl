package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-工程签证单管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectVisaOrderService extends IBaseService<CmProjectVisaOrder>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectVisaOrder entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectVisaOrder entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectVisaOrder entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmProjectVisaOrder getById(String id);

	/**
	 * @description 发起流程方法
	 * @param entity 实体类
	 * @return Integer 增加的记录数
	 * @author
	 * @version  2020-07-09
	 */
	Integer saveWorkflow(CmProjectVisaOrder entity)  throws CustomException ;

	/**
	 * @description 修改方法
	 * @param entity 实体类
	 * @return Integer 修改的记录数量
	 * @author
	 * @version  2020-07-09
	 */
	Integer updateWorkflow(CmProjectVisaOrder entity) throws  CustomException ;

	PageManager workFlowTodoQueryByPage(CmProjectVisaOrder cond, PageManager page) ;

	PageManager workFlowDoneQueryByPage(CmProjectVisaOrder cond, PageManager page) ;

	PageManager workFlowInstanceQueryByPage(CmProjectVisaOrder cond, PageManager page) ;
}
