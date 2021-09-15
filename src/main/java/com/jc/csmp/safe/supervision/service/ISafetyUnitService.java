package com.jc.csmp.safe.supervision.service;

import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * @Version 1.0
 */
public interface ISafetyUnitService extends IBaseService<SafetyUnit>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(SafetyUnit entity,boolean logicDelete) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(SafetyUnit entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(SafetyUnit entity) throws CustomException;


}
