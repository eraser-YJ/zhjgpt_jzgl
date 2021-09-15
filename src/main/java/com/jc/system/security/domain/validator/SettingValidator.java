package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Setting;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SettingValidator implements Validator{

	@Override
	public boolean supports(Class<?> arg0) {
		return Setting.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		Setting v =  (Setting)arg0;
		if(v.getSettingKey()!=null&&v.getSettingKey().length()>64){
			arg1.reject("getKey", "getEky is too long");
		}
		if(v.getSettingValue()!=null&&v.getSettingValue().length()>64){
			arg1.reject("getValue", "getValue is too long");
		}
		if(v.getExtStr1()!=null&&v.getExtStr1().length()>200){
			arg1.reject("extStr1", "ExtStr1 is too long");
		}
		if(v.getExtStr2()!=null&&v.getExtStr2().length()>200){
			arg1.reject("extStr2", "ExtStr2 is too long");
		}
		if(v.getExtStr3()!=null&&v.getExtStr3().length()>200){
			arg1.reject("extStr3", "ExtStr3 is too long");
		}
		if(v.getExtStr4()!=null&&v.getExtStr4().length()>200){
			arg1.reject("extStr4", "ExtStr4 is too long");
		}
		if(v.getExtStr5()!=null&&v.getExtStr5().length()>200){
			arg1.reject("extStr5", "ExtStr5 is too long");
		}
	}
}
