package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class RoleExts extends BaseBean{
	private static final long serialVersionUID = -364141066998872285L;
	/** 角色ID */
	private String roleId;
	/** 访问权限类型 */
	private Integer permissionType;
	/** 安全系数 */
	private Integer weight;
	/** 安全系数规则 */
	private Integer weightRule;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Integer getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}
	@Override
    public Integer getWeight() {
		return weight;
	}
	@Override
    public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getWeightRule() {
		return weightRule;
	}
	public void setWeightRule(Integer weightRule) {
		this.weightRule = weightRule;
	}
}
