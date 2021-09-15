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
public class SubDepartmentRoleGroup extends BaseBean {

//	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public SubDepartmentRoleGroup() {
	}

	private String deptId; /* 部门ID */
	private String deptName; /* 部门名称 */
	private String roleGroupId; /* 角色组ID */
	private String roleGroupName; /* 角色组名称 */
	//private Integer weight; /* 权重 */
	private String secret; /* 密级 */
	private String roleGroupIds; /* 角色组IDS */
	private String roleGroupNames; /* 角色组Names */


	private String deptIds;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRoleGroupIds() {
		return roleGroupIds;
	}

	public void setRoleGroupIds(String roleGroupIds) {
		this.roleGroupIds = roleGroupIds;
	}

	public String getRoleGroupNames() {
		return roleGroupNames;
	}

	public void setRoleGroupNames(String roleGroupNames) {
		this.roleGroupNames = roleGroupNames;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRoleGroupId() {
		return roleGroupId;
	}

	public void setRoleGroupId(String roleGroupId) {
		this.roleGroupId = roleGroupId;
	}

	public String getRoleGroupName() {
		return roleGroupName;
	}

	public void setRoleGroupName(String roleGroupName) {
		this.roleGroupName = roleGroupName;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
}