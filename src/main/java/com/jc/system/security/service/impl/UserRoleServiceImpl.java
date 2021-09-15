package com.jc.system.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUserRoleDao;
import com.jc.system.security.domain.UserRole;
import com.jc.system.security.service.IUserRoleService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements IUserRoleService {
	@Autowired
	public UserRoleServiceImpl(IUserRoleDao userRoleDao) {
		super(userRoleDao);
	}
	public UserRoleServiceImpl(){
		
	}
}
