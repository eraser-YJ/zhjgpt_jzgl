package com.jc.sys.domain;

import com.jc.foundation.domain.BaseBean;


/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRoleMenu extends BaseBean{
	
//	protected transient final Logger log = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	public SubRoleMenu() {
	}	
	
	private String roleId;   /*角色ID*/
	private String menuId;   /*菜单ID*/
	private String menuName;   /*菜单名称*/
	private String menuIds; /* 菜单IDS */
	private String menuNames; /* 菜单名称 */
//	private Integer weight;   /*权重*/
	private String secret;   /*密级*/
	private String roleIds;   /*角色IDS*/
	private String userId; /* 用户ID */
	private String deptId; /* 部门ID */
	
	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}

	public String getRoleId(){
		return roleId;
	}
	
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	
	
	public String getMenuId(){
		return menuId;
	}
	
	public void setMenuId(String menuId){
		this.menuId = menuId;
	}
	
	
	public String getMenuName(){
		return menuName;
	}
	
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}
	
	public String getSecret(){
		return secret;
	}
	
	public void setSecret(String secret){
		this.secret = secret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
}