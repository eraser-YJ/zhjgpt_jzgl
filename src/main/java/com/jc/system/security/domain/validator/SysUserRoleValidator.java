package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.SysUserRole;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SysUserRoleValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return SysUserRole.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		SysUserRole v =  (SysUserRole)arg0;
		if(v.getExtStr1()!=null&&v.getExtStr1().length()>200){
			arg1.reject("ExtStr1", "ExtStr1 is too long");
		}
		if(v.getExtStr2()!=null&&v.getExtStr2().length()>200){
			arg1.reject("ExtStr2", "ExtStr2 is too long");
		}
		if(v.getExtStr3()!=null&&v.getExtStr3().length()>200){
			arg1.reject("ExtStr3", "ExtStr3 is too long");
		}
		if(v.getExtStr4()!=null&&v.getExtStr4().length()>200){
			arg1.reject("ExtStr4", "ExtStr4 is too long");
		}
		if(v.getExtStr5()!=null&&v.getExtStr5().length()>200){
			arg1.reject("ExtStr5", "ExtStr5 is too long");
		}
	}
}
