package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.Logo;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class LogoValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	
	@Override
    public boolean supports(Class<?> arg0) {
		return Logo.class.equals(arg0);
	}
	
	@Override
    public void validate(Object arg0, Errors arg1) {
		Logo v =  (Logo)arg0;
		if(v.getLogoName()!=null&&v.getLogoName().length()>64){
			arg1.reject("logoName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getLogoSign()!=null&&v.getLogoSign().length()>64){
			arg1.reject("logoSign", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getLogoPath()!=null&&v.getLogoPath().length()>64){
			arg1.reject("logoPath", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
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
