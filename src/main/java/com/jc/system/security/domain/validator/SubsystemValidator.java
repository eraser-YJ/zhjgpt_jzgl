package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.Subsystem;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SubsystemValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final String JC_SYS_010 = "JC_SYS_010";
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Subsystem.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		Subsystem v =  (Subsystem)arg0;
		if(v.getName()==null){
			arg1.reject("name", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getName()!=null&&v.getName().length()>64){
			arg1.reject("name", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getIcon()!=null&&v.getIcon().length()>64){
			arg1.reject("icon", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getUrl()==null){
			arg1.reject("url", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getUrl()!=null&&v.getUrl().length()>255){
			arg1.reject("url", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getQueue()==null){
			arg1.reject("queue", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getOpenType()!=null&&v.getOpenType().length()>200){
			arg1.reject("openType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getPermission()!=null&&v.getPermission().length()>200){
			arg1.reject("permission", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
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
