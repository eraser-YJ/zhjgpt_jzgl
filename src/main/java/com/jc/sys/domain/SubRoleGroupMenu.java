package com.jc.sys.domain;

import org.apache.log4j.Logger;

import com.jc.foundation.domain.BaseBean;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRoleGroupMenu extends BaseBean {

//	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public SubRoleGroupMenu() {
	}

	private String roleGroupId; /* 角色组ID */
	private String menuId; /* 菜单ID */
	private String menuName; /* 菜单名称 */
	private String menuIds; /* 菜单IDS */
	private String menuNames; /* 菜单名称 */
//	private Integer weight; /* 权重 */
	private String secret; /* 密级 */
	private String deptId; /*部门id*/
	private String roleGroupIds; /* 角色组IDS */

	public String getRoleGroupIds() {
		return roleGroupIds;
	}

	public void setRoleGroupIds(String roleGroupIds) {
		this.roleGroupIds = roleGroupIds;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getRoleGroupId() {
		return roleGroupId;
	}

	public void setRoleGroupId(String roleGroupId) {
		this.roleGroupId = roleGroupId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
}