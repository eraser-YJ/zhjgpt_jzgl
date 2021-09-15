package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Department;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DepartmentValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return Department.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		Department v = (Department) arg0;
		if (v.getName() != null && v.getName().length() > 765) {
			arg1.reject("message", "Name is too long");
		}
	}
}
