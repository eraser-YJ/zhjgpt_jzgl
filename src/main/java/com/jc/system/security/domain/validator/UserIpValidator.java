package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.UserIp;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserIpValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return UserIp.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		UserIp v =  (UserIp)arg0;
		if(v.getUserStartIp()!=null&&v.getUserStartIp().length()>64){
			arg1.reject("userStartIp", "UserStartIp is too long");
		}
		if(v.getUserEndIp()!=null&&v.getUserEndIp().length()>64){
			arg1.reject("userEndIp", "UserEndIp is too long");
		}
		if(v.getUserIpDesc()!=null&&v.getUserIpDesc().length()>255){
			arg1.reject("userIpDesc", "UserIpDesc is too long");
		}
	}
}
