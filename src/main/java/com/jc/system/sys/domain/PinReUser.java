package com.jc.system.sys.domain;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public class PinReUser{

	public PinReUser() {
	}
	private String id;
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
	private String deptId;
	private Integer orderNo;
	private String isCheck;
	/**职位id*/
	private String dutyId;
	/**设置条件拼装类型*/
	private String searchType;
	/**用户名称*/
	private String text;
	/**特殊权重判断标识*/
	private String weightRole;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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
	public String getWeightRole() {
		return weightRole;
	}
	public void setWeight(String weightRole) {
		this.weightRole = weightRole;
	}
}