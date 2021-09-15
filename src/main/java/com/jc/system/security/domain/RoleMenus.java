package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 角色菜单
 * @author Administrator
 * @date 2020-06-30
 */
public class RoleMenus extends BaseBean{
	private static final long serialVersionUID = 1L;
	/** 角色ID */
	private String roleId;
	/** 菜单ID */
	private String menuId;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
