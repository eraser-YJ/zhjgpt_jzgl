package com.jc.system.dic.service;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.system.dic.domain.DicType;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IDicTypeService {
	DicType save(DicType dicType) throws CustomException;

	List<DicType> query(DicType dicType) throws CustomException;

	public DicType get(DicType dicType);

	/**
	 * @description 删除流程实例(递归删除子类型)
	 * @param dicType
	 */
	void delete(DicType dicType) throws CustomException;

	/**
	 * @description 更新字典类型
	 * @param dicType
	 *            字典类型
	 */
	void updateDicType(DicType dicType) throws CustomException;
}
