package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.RoleMenus;

/**
 * 菜单角色
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleMenusDao extends IBaseDao<RoleMenus> {

	@Override
    Integer save(RoleMenus roleMenus);

	Integer deleteByRoleId(RoleMenus roleMenus);

	List<Menu> queryMenuForRoles(String roleids);
	
}
