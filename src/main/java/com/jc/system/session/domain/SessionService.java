package com.jc.system.session.domain;

import com.jc.foundation.domain.BaseBean;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SessionService extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public SessionService() {
	}	
	
	/**用户ID*/
	private String userId;   
	/**部门名称*/
	private String deptName;   
	/**session序号*/
	private String sessionId;   
	/**子系统标识*/
	private String subsystem;
	/**secret*/
	private String secret;
	/**用户名字*/
	private String userName;
	/**ip地址*/
	private String userIp;
	/**用户登录名字*/
	private String loginName;
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getDeptName(){
		return deptName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	public String getSessionId(){
		return sessionId;
	}
	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}
	public String getSubsystem(){
		return subsystem;
	}
	public void setSubsystem(String subsystem){
		this.subsystem = subsystem;
	}
	public String getSecret(){
		return secret;
	}
	public void setSecret(String secret){
		this.secret = secret;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}