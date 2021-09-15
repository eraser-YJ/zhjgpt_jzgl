package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class RoleBlocks extends BaseBean{
	private static final long serialVersionUID = 6113055312108186455L;
	/** 角色Id */
	private String roleId;
	/** 部门Id */
	private String deptId;
	/** 是否真正点选 */
	private String isChecked;
	private String[] roleIds;
	private String[] deptIds;
	/** 部门还是人员(部门为true)*/
	private Boolean flag = true;

	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public String[] getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String[] deptIds) {
		this.deptIds = deptIds;
	}
	@Override
	public int hashCode() {
		if(deptId!=null) {
			return deptId.hashCode()+flag.hashCode();
		}
	    return super.hashCode();
	}
	@Override 
	public boolean equals(Object obj) { 
		if (this == obj) { 
			return true; 
		}
		if (obj == null) { 
			return false; 
		}
		if (getClass() != obj.getClass()) { 
			return false; 
		}
		RoleBlocks other = (RoleBlocks) obj; 
		if (deptId == null) { 
			if (other.deptId != null) { 
				return false; 
			} 
		} else if (!deptId.equals(other.deptId)) { 
			return false;  
		} else if(!flag){
			return false;
		} else if(!other.flag){
			return false;
		}
		return true; 
	}
}
