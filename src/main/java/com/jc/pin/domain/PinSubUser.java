package com.jc.pin.domain;

import com.jc.foundation.domain.BaseBean;

/***
 * 自定义部门拼音表
 * @author Administrator
 * @date 2020-06-30
 */
public class PinSubUser extends BaseBean {	
	private static final long serialVersionUID = 1L;
	
	public PinSubUser() {}
	/**部门ID*/
	private String deptId;
	/**用户ID*/
	private String userId;
	/**用户名称*/
	private String userName;
	/**拼音首字母*/
	private String userInitials;
	/**拼音首字母缩写*/
	private String userAbbreviate;
	/**拼音全拼*/
	private String userFull;
	private Integer orderNo;
	private String isCheck;
	/**职位id*/
	private String dutyId;
	/**设置条件拼装类型*/
	private String searchType;
	/**用户名称*/
	private String text;
	private boolean isSubSystem;
	public boolean isSubSystem() {
		return isSubSystem;
	}
	public void setSubSystem(boolean subSystem) {
		isSubSystem = subSystem;
	}
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserInitials(){
		return userInitials;
	}
	public void setUserInitials(String userInitials){
		this.userInitials = userInitials;
	}
	public String getUserAbbreviate(){
		return userAbbreviate;
	}
	public void setUserAbbreviate(String userAbbreviate){
		this.userAbbreviate = userAbbreviate;
	}
	public String getUserFull(){
		return userFull;
	}
	public void setUserFull(String userFull){
		this.userFull = userFull;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getDutyId() {
		return dutyId;
	}
	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}