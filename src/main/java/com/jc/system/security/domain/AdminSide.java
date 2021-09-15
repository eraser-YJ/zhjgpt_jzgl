package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class AdminSide extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**用户id*/
	private String userId;
	/**部门id*/
	private String deptId;
	/**父节点id*/
	private String parentId;
	/**是否选中*/
	private String isChecked;
	/**机构属性*/
	private String name;
	private Integer deptType;
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	public String getParentId(){
		return parentId;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	}
	public String getIsChecked(){
		return isChecked;
	}
	public void setIsChecked(String isChecked){
		this.isChecked = isChecked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDeptType() {
		return deptType;
	}
	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}
}