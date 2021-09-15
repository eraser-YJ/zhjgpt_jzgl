package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IRoleMenusDao;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.RoleMenus;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class RoleMenusDaoImpl extends BaseServerDaoImpl<RoleMenus> implements
		IRoleMenusDao {

	@Override
    public Integer save(RoleMenus roleMenus) {
		return getTemplate().insert(getNameSpace(roleMenus)+".insert", roleMenus);
	}

	@Override
    public Integer deleteByRoleId(RoleMenus roleMenus) {
		return getTemplate().update(getNameSpace(roleMenus)+".deleteByIds", roleMenus);
	}

	@Override
	public List<Menu> queryMenuForRoles(String roleids) {
		return getTemplate().selectList(getNameSpace(new RoleMenus())+".queryMenuForRoles", roleids);
	}
}
