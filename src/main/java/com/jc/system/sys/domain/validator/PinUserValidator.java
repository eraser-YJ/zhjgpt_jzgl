package com.jc.system.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.sys.domain.PinUser;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public class PinUserValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";

	@Override
    public boolean supports(Class<?> arg0) {
		return PinUser.class.equals(arg0);
	}

	@Override
    public void validate(Object arg0, Errors arg1) {
		PinUser v =  (PinUser)arg0;
		if(v.getUserName()!=null&&v.getUserName().length()>64){
			arg1.reject("userName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getUserInitials()!=null&&v.getUserInitials().length()>64){
			arg1.reject("userInitials", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getUserAbbreviate()!=null&&v.getUserAbbreviate().length()>64){
			arg1.reject("userAbbreviate", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getUserFull()!=null&&v.getUserFull().length()>64){
			arg1.reject("userFull", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getExtStr1()!=null&&v.getExtStr1().length()>200){
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr2()!=null&&v.getExtStr2().length()>200){
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr3()!=null&&v.getExtStr3().length()>200){
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr4()!=null&&v.getExtStr4().length()>200){
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr5()!=null&&v.getExtStr5().length()>200){
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
	}
}
