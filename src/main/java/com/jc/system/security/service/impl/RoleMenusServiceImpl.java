package com.jc.system.security.service.impl;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IRoleMenusDao;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.RoleMenus;
import com.jc.system.security.service.IRoleMenusService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class RoleMenusServiceImpl extends BaseServiceImpl<RoleMenus> implements IRoleMenusService {
	@Autowired
	public RoleMenusServiceImpl(IRoleMenusDao roleMenusDao) {
		super(roleMenusDao);
		this.roleMenusDao= roleMenusDao;
	}

	public RoleMenusServiceImpl(){
		
	}

	private IRoleMenusDao roleMenusDao;

	public List<RoleMenus> query(RoleMenus roleMenus) {
		return roleMenusDao.queryAll(roleMenus);
	}

	@Override
	public List<Menu> queryMenuForRoles(String roleids) {
		return roleMenusDao.queryMenuForRoles(roleids);
	}

}
