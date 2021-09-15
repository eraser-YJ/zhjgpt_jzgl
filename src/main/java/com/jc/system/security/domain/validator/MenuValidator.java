package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Menu;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class MenuValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Menu.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		Menu v =  (Menu)arg0;
		if(v.getName()!=null&&v.getName().length()>64){
			arg1.reject("name", "Name is too long");
		}
		if(v.getIcon()!=null&&v.getIcon().length()>64){
			arg1.reject("icon", "Icon is too long");
		}
		if(v.getActionName()!=null&&v.getActionName().length()>255){
			arg1.reject("actionName", "ActionName is too long");
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
