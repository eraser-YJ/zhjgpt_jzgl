package com.jc.system.security.check;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserIpException;
import com.jc.system.util.SettingUtils;

/**
 * 判断IP绑定
 * @author Administrator *
 * @date 2020-7-01
 */
public class CheckForUserIp extends CheckVal{

	@Override
	public void check(User user, UserToken token) throws Exception {
		if("1".equals(SettingUtils.getSetting("useIpBanding"))){
			if(!SystemSecurityUtils.determineIp(user.getId(), token.getHost())){
				throw new UserIpException();
			}
		}
	}

}
