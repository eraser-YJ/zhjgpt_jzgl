package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-工程问题管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectQuestionService extends IBaseService<CmProjectQuestion>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectQuestion entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectQuestion entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectQuestion entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmProjectQuestion getById(String id);

	/**
	 * @description 发起流程方法
	 * @param entity 实体类
	 * @return Integer 增加的记录数
	 * @author
	 * @version  2020-07-09
	 */
	Integer saveWorkflow(CmProjectQuestion entity)  throws CustomException ;

	/**
	 * @description 修改方法
	 * @param entity 实体类
	 * @return Integer 修改的记录数量
	 * @author
	 * @version  2020-07-09
	 */
	Integer updateWorkflow(CmProjectQuestion entity) throws  CustomException ;

	PageManager workFlowTodoQueryByPage(CmProjectQuestion entity, PageManager page) ;

	PageManager workFlowDoneQueryByPage(CmProjectQuestion entity, PageManager page) ;

	PageManager workFlowInstanceQueryByPage(CmProjectQuestion entity, PageManager page) ;
}
