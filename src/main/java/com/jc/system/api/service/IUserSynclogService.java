package com.jc.system.api.service;

import com.jc.foundation.service.IBaseService;
import com.jc.system.api.domain.UserSynclog;
import com.jc.foundation.exception.CustomException;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserSynclogService extends IBaseService<UserSynclog>{
	/**
	 * 根据主键删除多条记录方法
	 * @param userSync
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(UserSynclog userSync) throws CustomException;
}