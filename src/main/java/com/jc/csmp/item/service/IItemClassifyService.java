package com.jc.csmp.item.service;

import com.jc.csmp.item.domain.ItemClassify;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IItemClassifyService extends IBaseService<ItemClassify>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ItemClassify entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ItemClassify entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ItemClassify entity) throws CustomException;

	ItemClassify get(ItemClassify entity) throws CustomException;

	/**
	 * 根据code获取数据
	 * @param itemCode
	 * @return
	 */
	ItemClassify getByItemCode(String itemCode);
}
