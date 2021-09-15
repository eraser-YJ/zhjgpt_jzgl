package com.jc.system.security.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Loginlog;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ILoginlogService extends IBaseService<Loginlog>{
	void setLoginUserInfo(User userVo,String ip,int inOrout,int loginDevice) throws CustomException;
}