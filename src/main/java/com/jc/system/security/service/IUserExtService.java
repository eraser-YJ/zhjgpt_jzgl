package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.UserExt;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserExtService extends IBaseService<UserExt>{
	Integer deleteByIds(UserExt userExt) throws CustomException;
}