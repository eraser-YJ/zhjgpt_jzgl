package com.jc.system.security.check;

import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserDisabledException;
import com.jc.system.security.exception.UserLockedException;
import com.jc.system.security.service.IUserService;
import com.jc.system.util.SettingUtils;

/**
 * 判断锁定
 * @author Administrator
 * @date 2020-07-01
 */
public class CheckForUserDisabled extends CheckVal{

	@Override
	public void check(User user, UserToken token) throws Exception {
		if(user.getStatus().equals(GlobalContext.USER_STATUS_1)){
			throw new UserDisabledException();
		}  else if(user.getStatus().equals(GlobalContext.USER_STATUS_2)){
			//管理员锁定
			if(user.getLockType() == null || user.getLockType() == 1){
				throw new UserLockedException();
			} else {
				//密码错误次数锁定
				int minute = Integer.parseInt(SettingUtils.getSetting("lockTime").toString());
				if(DateUtils.subtractionMinute(user.getLockStartDate(), DateUtils.getSysDate()) < minute){
					throw new UserLockedException();
				} else {
					User u = new User();
					u.setStatus(GlobalContext.USER_STATUS_0);
					u.setId(user.getId());
					getUserService().update2(u);
				}
			}
		}
	}
	
	private IUserService userService;
	
	/**
	 * 获取系统业务对象
	 */
	public IUserService getUserService() {
		if (userService == null) {
			userService = SpringContextHolder.getBean(IUserService.class);
		}
		return userService;
	}

}
