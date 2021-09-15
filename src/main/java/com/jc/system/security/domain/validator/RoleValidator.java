package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Role;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class RoleValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return Role.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		Role v = (Role) arg0;
		if (v.getName() == null || "".equals(v.getName())) {
			arg1.reject("name", "角色名称不能为空");
		}
		if (v.getDescription() == null || "".equals(v.getDescription())) {
			arg1.reject("description", "角色描述不能为空");
		}
	}
}
