package com.jc.csmp.equipment.info.service;

import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IEquipmentExinfoService extends IBaseService<EquipmentExinfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(EquipmentExinfo entity) throws CustomException;
	public Integer deleteById(String id) throws CustomException;
	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(EquipmentExinfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(EquipmentExinfo entity) throws CustomException;
}
