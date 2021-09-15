package com.jc.system.security.domain.validator;

import com.jc.system.security.domain.UserExt;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserExtValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	
	@Override
    public boolean supports(Class<?> arg0) {
		return UserExt.class.equals(arg0);
	}
	
	@Override
    public void validate(Object arg0, Errors arg1) {
		UserExt v =  (UserExt)arg0;
		if(v.getCssType()!=null&&v.getCssType().length()>64){
			arg1.reject("cssType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getCssValue()!=null&&v.getCssValue().length()>64){
			arg1.reject("cssValue", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
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
