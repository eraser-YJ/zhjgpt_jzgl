package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class Shortcut extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	/***当前登陆人id*/
	private String curLoginUserId;
	/***当前登陆人部门id*/
	private String curLoginDeptId;
	/***当前登陆人机构id*/
	private String curLoginOrgId;
	public void setCurLoginDeptId(String curLoginDeptId) {
		this.curLoginDeptId = curLoginDeptId;
	}
	public void setCurLoginOrgId(String curLoginOrgId) {
		this.curLoginOrgId = curLoginOrgId;
	}
	public String getCurLoginUserId() {
		return curLoginUserId;
	}
	public void setCurLoginUserId(String curLoginUserId) {
		this.curLoginUserId = curLoginUserId;
	}
	public String getCurLoginDeptId() {
		return curLoginDeptId;
	}
	public String getCurLoginOrgId() {
		return curLoginOrgId;
	}
	/**快捷方式名称*/
	private String name;
	/**图标类型名称*/
	private String icon;
	/**子系统ID*/
	private String subsystemid;
	/**子系统NAME*/
	private String subsystemName;
	/**子系统ID*/
	private String subsystemUrl;
	/**菜单ID*/
	private String menuid;
	/**菜单NAME*/
	private String menuName;
	/**菜单ID*/
	private String menuUrl;
	/**排序*/
	private Integer queue;
	/**可见标识*/
	private Integer isShow;
	/**权限标识*/
	private String permission;
	/**默认快捷方式*/
	private String defaultType;
	/**菜单打开方式*/
	private String openType;
	/**临时字段，判断快捷方式被选择*/
	private Integer isChecked;
	/**临时字段，用户快捷方式排序*/
	private Integer userOrder; 
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getIcon(){
		return icon;
	}
	public void setIcon(String icon){
		this.icon = icon;
	}
	public String getSubsystemid(){
		return subsystemid;
	}
	public void setSubsystemid(String subsystemid){
		this.subsystemid = subsystemid;
	}
	public String getSubsystemUrl() {
		return subsystemUrl;
	}
	public void setSubsystemUrl(String subsystemUrl) {
		this.subsystemUrl = subsystemUrl;
	}
	public String getMenuid(){
		return menuid;
	}
	public void setMenuid(String menuid){
		this.menuid = menuid;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public Integer getQueue(){
		return queue;
	}
	public void setQueue(Integer queue){
		this.queue = queue;
	}
	public Integer getIsShow(){
		return isShow;
	}
	public void setIsShow(Integer isShow){
		this.isShow = isShow;
	}
	public String getPermission(){
		return permission;
	}
	public void setPermission(String permission){
		this.permission = permission;
	}
	public String getDefaultType(){
		return defaultType;
	}
	public void setDefaultType(String defaultType){
		this.defaultType = defaultType;
	}
	public String getOpenType(){
		return openType;
	}
	public void setOpenType(String openType){
		this.openType = openType;
	}
	public Integer getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	public Integer getUserOrder() {
		return userOrder;
	}
	public void setUserOrder(Integer userOrder) {
		this.userOrder = userOrder;
	}
	public String getSubsystemName() {
		return subsystemName;
	}
	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}