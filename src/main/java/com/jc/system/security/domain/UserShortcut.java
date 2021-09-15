package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserShortcut extends BaseBean{

	private static final long serialVersionUID = 1L;

	/**当前登陆人id*/
	private Long curLoginUserId;
	/**当前登陆人部门id*/
	private Long curLoginDeptId;
	/**当前登陆人机构id*/
	private Long curLoginOrgId;
	public Long getCurLoginUserId() {
		return curLoginUserId;
	}
	public Long getCurLoginDeptId() {
		return curLoginDeptId;
	}
	public Long getCurLoginOrgId() {
		return curLoginOrgId;
	}
	public void setCurLoginUserId(Long curLoginUserId) {
		this.curLoginUserId = curLoginUserId;
	}
	public void setCurLoginDeptId(Long curLoginDeptId) {
		this.curLoginDeptId = curLoginDeptId;
	}
	public void setCurLoginOrgId(Long curLoginOrgId) {
		this.curLoginOrgId = curLoginOrgId;
	}
	/**用户ID*/
	private String userid;
	/**快捷方式ID*/
	private Integer shortcutid;
	/**排序*/
	private Integer queue;
	/**可见标识*/
	private Integer isShow;
	/**权限标识*/
	private String permission;

	public String getUserid(){
		return userid;
	}
	public void setUserid(String userid){
		this.userid = userid;
	}
	public Integer getShortcutid(){
		return shortcutid;
	}
	public void setShortcutid(Integer shortcutid){
		this.shortcutid = shortcutid;
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
}