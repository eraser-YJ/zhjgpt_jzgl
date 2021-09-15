package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Loginlog;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class LoginlogValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Loginlog.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		Loginlog v =  (Loginlog)arg0;
		if(v.getLoginName()!=null&&v.getLoginName().length()>50){
			arg1.reject("loginName", "LoginName is too long");
		}
		if(v.getDisplayName()!=null&&v.getDisplayName().length()>50){
			arg1.reject("displayName", "DisplayName is too long");
		}
		if(v.getIp()!=null&&v.getIp().length()>20){
			arg1.reject("ip", "Ip is too long");
		}
	}
}
