package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectWeekly;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @Version 1.0
 */
public interface ICmProjectWeeklyService extends IBaseService<CmProjectWeekly>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectWeekly entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectWeekly entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectWeekly entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmProjectWeekly getById(String id);

	/**
	 * 移动端上报接口
	 * @param entity
	 * @param user
	 * @return
	 */
    MobileApiResponse mobileReport(CmProjectWeekly entity, User user);
}
