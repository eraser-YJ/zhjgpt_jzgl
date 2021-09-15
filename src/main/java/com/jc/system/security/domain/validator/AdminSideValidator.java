package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.AdminSide;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class AdminSideValidator implements Validator{

	@Override
	public boolean supports(Class<?> arg0) {
		return AdminSide.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		AdminSide v =  (AdminSide)arg0;
		if(v.getIsChecked()!=null&&v.getIsChecked().length()>1){
			arg1.reject("isChecked", "IsChecked is too long");
		}
	}
}
