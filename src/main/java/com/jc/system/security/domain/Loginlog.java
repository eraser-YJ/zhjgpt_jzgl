package com.jc.system.security.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.CustomDateSerializer;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class Loginlog extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**登录用户ID*/
	private String userId;
	/**登录名*/
	private String loginName;
	/**显示名*/
	private String displayName;
	/**IP地址*/
	private String ip;
	/**登录时间*/
	private Date loginTime;
	/**退出时间*/
	private Date logoutTime;
	/**登录时间--查询开始时间*/
	private Date loginTimeBegin;
	/**登录时间--查询结束时间*/
	private Date loginTimeEnd;
	/**退出时间--查询开始时间*/
	private Date logoutTimeBegin;
	/**退出时间--查询结束时间*/
	private Date logoutTimeEnd;
	/**登录状态--0登录 1退出 2全部*/
	private int loginstatus;
	/**登录设备--1PC机 2移动设备*/
	private int loginDevice;
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
	public String getIp(){
		return ip;
	}
	public void setIp(String ip){
		this.ip = ip;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "GMT+8")
	public Date getLoginTime(){
		return loginTime;
	}
	public void setLoginTime(Date loginTime){
		this.loginTime = loginTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "GMT+8")
	public Date getLogoutTime(){
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime){
		this.logoutTime = logoutTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLoginTimeBegin() {
		return loginTimeBegin;
	}
	public void setLoginTimeBegin(Date loginTimeBegin) {
		this.loginTimeBegin = loginTimeBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLoginTimeEnd() {
		return loginTimeEnd;
	}
	public void setLoginTimeEnd(Date loginTimeEnd) {
		this.loginTimeEnd = loginTimeEnd;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLogoutTimeBegin() {
		return logoutTimeBegin;
	}
	public void setLogoutTimeBegin(Date logoutTimeBegin) {
		this.logoutTimeBegin = logoutTimeBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLogoutTimeEnd() {
		return logoutTimeEnd;
	}
	public void setLogoutTimeEnd(Date logoutTimeEnd) {
		this.logoutTimeEnd = logoutTimeEnd;
	}
	public int getLoginstatus() {
		return loginstatus;
	}
	public void setLoginstatus(int loginstatus) {
		this.loginstatus = loginstatus;
	}
	public int getLoginDevice() {
		return loginDevice;
	}
	public void setLoginDevice(int loginDevice) {
		this.loginDevice = loginDevice;
	}
}