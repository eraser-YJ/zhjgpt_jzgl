package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.SysRole;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SysRoleValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return SysRole.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		SysRole v =  (SysRole)arg0;
		if(v.getName()!=null&&v.getName().length()>64){
			arg1.reject("message", "Name is too long");
		}
		if(v.getDescription()!=null&&v.getDescription().length()>255){
			arg1.reject("message", "Description is too long");
		}
	}
}
