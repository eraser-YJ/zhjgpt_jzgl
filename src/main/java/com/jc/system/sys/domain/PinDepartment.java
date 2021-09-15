package com.jc.system.sys.domain;

import com.jc.foundation.domain.BaseBean;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public class PinDepartment extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public PinDepartment() {
	}

	/**部门ID*/
	private String deptId;
	/**部门名称*/
	private String deptName;
	/**拼音首字母*/
	private String deptInitials;
	/**拼音首字母缩写*/
	private String deptAbbreviate;
	/**拼音全拼*/
	private String deptFull;
	/**上级节点Id*/
	private String parentId;
	/**标记是部门or机构节点  ’0‘--部门  ’1‘--机构*/
	private Integer deptType;
	/**部门排序*/
	private Integer queue;
	/**设置条件拼装类型*/
	private String searchType; 
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	public String getDeptName(){
		return deptName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	public String getDeptInitials(){
		return deptInitials;
	}
	public void setDeptInitials(String deptInitials){
		this.deptInitials = deptInitials;
	}
	public String getDeptAbbreviate(){
		return deptAbbreviate;
	}
	public void setDeptAbbreviate(String deptAbbreviate){
		this.deptAbbreviate = deptAbbreviate;
	}
	public String getDeptFull(){
		return deptFull;
	}
	public void setDeptFull(String deptFull){
		this.deptFull = deptFull;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Integer getDeptType() {
		return deptType;
	}
	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}
	public Integer getQueue() {
		return queue;
	}
	public void setQueue(Integer queue) {
		this.queue = queue;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}