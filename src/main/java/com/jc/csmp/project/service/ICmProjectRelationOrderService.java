package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectRelationOrder;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-项目工程联系单service实现
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectRelationOrderService extends IBaseService<CmProjectRelationOrder>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectRelationOrder entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectRelationOrder entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectRelationOrder entity) throws CustomException;

	/**
	 * 我收到的签收单
	 * @param entity
	 * @param page
	 * @return
	 */
	PageManager myQuery(CmProjectRelationOrder entity, PageManager page);

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmProjectRelationOrder getById(String id);
}
