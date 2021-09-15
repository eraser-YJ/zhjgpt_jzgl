package com.jc.system.api.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class UserSynclog extends BaseBean{

	private static final long serialVersionUID = 1L;
	
	public UserSynclog() {
	}

	/**登录用户ID*/
	private String userId;
	/**登录名*/
	private String loginName;
	/**显示名*/
	private String displayName;
	/**子系统标识*/
	private String subsystem;
	/**返回状态0/非0*/
	private String status;
	/**返回信息*/
	private String msg;
	/**同步时间*/
	private String syncTime;

	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getLoginName(){
		return loginName;
	}
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
	public String getDisplayName(){
		return displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	public String getSubsystem(){
		return subsystem;
	}
	public void setSubsystem(String subsystem){
		this.subsystem = subsystem;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getMsg(){
		return msg;
	}
	public void setMsg(String msg){
		this.msg = msg;
	}
	public String getSyncTime(){
		return syncTime;
	}
	public void setSyncTime(String syncTime){
		this.syncTime = syncTime;
	}
}