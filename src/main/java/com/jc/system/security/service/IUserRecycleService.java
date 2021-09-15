package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.UserRecycle;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserRecycleService extends IBaseService<UserRecycle> {
	Integer deleteByIds(UserRecycle userRecycle) throws CustomException;
}