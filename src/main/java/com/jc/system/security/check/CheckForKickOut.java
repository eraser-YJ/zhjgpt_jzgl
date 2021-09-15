package com.jc.system.security.check;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.util.SettingUtils;

/**
 * 踢出用户
 * @author Administrator
 * @date 2020-07-01
 */
public class CheckForKickOut extends CheckVal{

	@Override
	public void check(User user, UserToken token) throws Exception {
		if("0".equals(SettingUtils.getSetting("loginType"))){
			SystemSecurityUtils.kickOutUser(user.getLoginName());
		}
	}

}
