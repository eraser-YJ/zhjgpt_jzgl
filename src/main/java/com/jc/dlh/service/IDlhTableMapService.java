package com.jc.dlh.service;

import com.jc.dlh.domain.DlhTableMap;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IDlhTableMapService extends IBaseService<DlhTableMap>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(DlhTableMap entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(DlhTableMap entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(DlhTableMap entity) throws CustomException;
}
