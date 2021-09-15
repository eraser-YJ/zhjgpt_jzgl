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
public class SubUserRole extends BaseBean {

//	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public SubUserRole() {
	}

	private String userId; /* 用户ID */
	private String roleId; /* 角色ID */
	private String roleName; /* 角色名称 */
//	private Integer weight; /* 权重 */
	private String secret; /* 密级 */
	private String roleIds; /* 角色IDS */
	private String roleNames; /* 角色名称S */

	private String deptId; /* 部门ID */

	private String selDeptId; /*角色所在部门*/

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getSelDeptId() {
		return selDeptId;
	}

	public void setSelDeptId(String selDeptId) {
		this.selDeptId = selDeptId;
	}
}