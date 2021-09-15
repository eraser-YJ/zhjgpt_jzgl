package com.jc.system.security.check;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jc.foundation.util.RsaUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserPasswordException;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.utils.RSASetting;

/**
 * 登录验证处理
 * 类初始化时加载规则
 * 可使用passVals参数进行检查跳过
 * @author Administrator
 * @date 2020-07-01
 */
public class CheckFactory {
	
	private static Map<String,CheckVal> vals = new HashMap<String, CheckVal>();
	
	static {
		vals.put("checkForOnlineCount",new CheckForOnlineCount());
		vals.put("checkForUserDisabled",new CheckForUserDisabled());
		vals.put("checkForUserIp",new CheckForUserIp());
		vals.put("checkForUserPassword",new CheckForUserPassword());
		vals.put("checkForUnknownAccount",new CheckForUnknownAccount());
		vals.put("checkForKickOut",new CheckForKickOut());
	}
	
	public static void check(User user,UserToken token) throws Exception{
		for(CheckVal val:vals.values()) {
			val.check(user,token);
		}
	}
	
	public static void checkChioce(User user,UserToken token,String checkVals) throws Exception{
		if(checkVals!=null) {
			for(Entry<String, CheckVal> val:vals.entrySet()) {
				if(checkVals.indexOf(val.getKey())!=-1) {
					val.getValue().check(user,token);
				}
			}
		}
	}
	
	public static void check(User user,UserToken token,String passVals) throws Exception{
		if(passVals!=null) {
			for(Entry<String, CheckVal> val:vals.entrySet()) {
				if(passVals.indexOf(val.getKey())==-1) {
					val.getValue().check(user,token);
				}
			}
		}else {
			check(user, token);
		}
	}
	
	/**
	 * rsa加密处理的密码 登录处理
	 * @param username
	 * @param rsaPassword
	 * @throws Exception
	 */
	public static User checkForRsaPassword(String username,String rsaPassword) throws Exception{
		User user = getSystemService().get(username);
		rsaPassword = RSASetting.decryptByPrivateKey(rsaPassword);
		if(!SystemSecurityUtils.validatePassword(rsaPassword, user.getPassword(),user.getKeyCode())){
			int count = SystemSecurityUtils.passwordError(user.getId(),"");
			throw new UserPasswordException(String.valueOf(count));
		}
		return user;
	}

	public static User checkForPassword(String username,String rsaPassword) throws Exception{
		User user = getSystemService().get(username);
		if(!SystemSecurityUtils.validatePassword(rsaPassword, user.getPassword(),user.getKeyCode())){
			int count = SystemSecurityUtils.passwordError(user.getId(),"");
			throw new UserPasswordException(String.valueOf(count));
		}
		return user;
	}
	
	private static ISystemService systemService;
	
	/**
	 * 获取系统业务对象
	 */
	public static ISystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(ISystemService.class);
		}
		return systemService;
	}

}
