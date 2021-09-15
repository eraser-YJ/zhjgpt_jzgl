package com.jc.system.security.check;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserPasswordException;

/**
 * 判断登录密码
 * @author Administrator
 * @date 2020-07-01
 */
public class CheckForUserPassword extends CheckVal{

	@Override
	public void check(User user, UserToken token) throws Exception {
		if(!SystemSecurityUtils.validatePassword(new String(token.getPassword()), user.getPassword(),user.getKeyCode())){
			int count = SystemSecurityUtils.passwordError(user.getId(),token.getHost());
			throw new UserPasswordException(String.valueOf(count));
		}
	}

}
