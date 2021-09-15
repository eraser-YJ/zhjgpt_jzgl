package com.jc.system.security.dao.impl;

import com.jc.system.security.domain.User;
import org.springframework.stereotype.Repository;

import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.dao.ISysUserRoleDao;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class SysUserRoleDaoImpl extends BaseServerDaoImpl<SysUserRole> implements ISysUserRoleDao{

	@Override
	public int deleteSysUserRole(SysUserRole sysUserRole) {
		return getTemplate().update(getNameSpace(sysUserRole)+".deleteSysUserRole", sysUserRole);
	}

	@Override
	public Integer deleteBack(SysUserRole sysUserRole) {
		return getTemplate().update(getNameSpace(sysUserRole) + ".deleteBackByIds", sysUserRole);
	}

	@Override
	public List<User> queryUserByRole(SysUserRole sysUserRole) {
		List<User> userList = this.getTemplate().selectList("com.jc.system.security.domain.SysUserRole.queryUserByRole", sysUserRole);
		return userList;
	}

}