package com.jc.sys.domain;

import org.apache.log4j.Logger;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRole extends BaseBean {

//	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public SubRole() {
	}

	private String deptId; /* 部门ID */
	private String deptName; /* 部门名称 */
	private String deptIds; /* 部门IDS */
	private String roleName; /* 角色名称 */
	private String roleDescription; /* 角色描述 */
	private Integer status; /* 状态 */
//	private Integer weight; /* 权重 */
	private String secret; /* 密级 */

	public String getDeptName() throws CustomException {
		if (deptId != null) {
			Department department = DeptCacheUtil.getDeptById(deptId);
			if (department != null) {
				deptName = department.getName();
			}
		}
		return deptName;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}