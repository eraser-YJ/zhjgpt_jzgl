package com.jc.system.security.check;

import org.apache.shiro.authc.UnknownAccountException;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;


/**
 * 判断唯一标识
 * @author Administrator
 * @date 2020-07-01
 */
public class CheckForUnknownAccount extends CheckVal{

	@Override
	public void check(User user, UserToken token) throws Exception {
		if(SystemSecurityUtils.getUniqueForCode(user.getCode(),user.getLoginName()) != 1){
			throw new UnknownAccountException();
		}
	}

}
