package com.jc.digitalchina.service;

import com.jc.digitalchina.domain.CmUserRelation;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.system.security.domain.User;

/**
 * 建设管理-自定义字典项service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmUserRelationService extends IBaseService<CmUserRelation>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmUserRelation entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmUserRelation entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmUserRelation entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmUserRelation getById(String id);

	/**
	 * 根据uuid获取对象
	 * @param uuid
	 * @return
	 */
	User getByUuid(String uuid);
}
