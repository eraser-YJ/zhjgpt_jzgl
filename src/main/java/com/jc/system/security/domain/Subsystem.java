package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class Subsystem extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public Subsystem() {

	}
	/**当前登陆人id*/
	private String curLoginUserId;
	/**当前登陆人部门id*/
	private String curLoginDeptId;
	/**当前登陆人机构id*/
	private String curLoginOrgId;
	public void setCurLoginUserId(String curLoginUserId) {
		this.curLoginUserId = curLoginUserId;
	}
	public String getCurLoginUserId() {
		return curLoginUserId;
	}
	public String getCurLoginDeptId() {
		return curLoginDeptId;
	}
	public String getCurLoginOrgId() {
		return curLoginOrgId;
	}
	/**子系统名称*/
	private String name;
	/**图标类型名称*/
	private String icon;
	/**子系统地址*/
	private String url;
	/**子系统首页*/
	private String firstUrl;
	/**菜单ID*/
	private String menuid;
	/**排序*/
	private Integer queue;
	/**可见标识*/
	private Integer isShow;
	/**可见标识*/
	private String openType;
	/**权限标识*/
	private String permission;
	/**用户同步地址*/
	private String userSyncUrl;
	
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
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getFirstUrl(){
		return firstUrl;
	}
	public void setFirstUrl(String firstUrl){
		this.firstUrl = firstUrl;
	}
	public String getMenuid(){
		return menuid;
	}
	public void setMenuid(String menuid){
		this.menuid = menuid;
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
	public String getOpenType(){
		return openType;
	}
	public void setOpenType(String openType){
		this.openType = openType;
	}
	public String getPermission(){
		return permission;
	}
	public void setPermission(String permission){
		this.permission = permission;
	}
	public String getUserSyncUrl() {
		return userSyncUrl;
	}
	public void setUserSyncUrl(String userSyncUrl) {
		this.userSyncUrl = userSyncUrl;
	}
}