package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.Unique;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UniqueValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	
	@Override
    public boolean supports(Class<?> arg0) {
		return Unique.class.equals(arg0);
	}
	
	@Override
    public void validate(Object arg0, Errors arg1) {
		Unique v =  (Unique)arg0;
		if(v.getUuid()!=null&&v.getUuid().length()>36){
			arg1.reject("uuid", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if(v.getPurpose()!=null&&v.getPurpose().length()>50){
			arg1.reject("purpose", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getState()!=null&&v.getState().length()>1){
			arg1.reject("state", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
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
