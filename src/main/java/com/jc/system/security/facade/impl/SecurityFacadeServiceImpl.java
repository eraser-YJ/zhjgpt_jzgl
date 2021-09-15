package com.jc.system.security.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.User;
import com.jc.system.security.facade.ISecurityFacadeService;
import com.jc.system.security.service.IRoleService;
import com.jc.system.security.service.IUserService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class SecurityFacadeServiceImpl implements ISecurityFacadeService {
	@Autowired
	private IUserService userService;
	
	
	public SecurityFacadeServiceImpl() {
	}

	@Override
	public List<User> getDeptUsers(User user) {
		return userService.queryUserByDeptId(user);
	}

	@Override
	public List<Role> getUserRoles(User user) {
		return null;
	}
	
}
