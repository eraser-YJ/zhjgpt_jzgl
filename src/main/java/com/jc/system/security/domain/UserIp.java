package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserIp extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**用户ID*/
	private String userId;
	/**绑定类型*/
	private Integer bindType;
	/**用户要绑定的开始IP*/
	private String userStartIp;
	/**用户要绑定的结束IP*/
	private String userEndIp;
	/**描述*/
	private String userIpDesc;
	private String userName;
	private String deptName;
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public Integer getBindType(){
		return bindType;
	}
	public void setBindType(Integer bindType){
		this.bindType = bindType;
	}
	public String getUserStartIp(){
		return userStartIp;
	}
	public void setUserStartIp(String userStartIp){
		this.userStartIp = userStartIp;
	}
	public String getUserEndIp(){
		return userEndIp;
	}
	public void setUserEndIp(String userEndIp){
		this.userEndIp = userEndIp;
	}
	public String getUserIpDesc(){
		return userIpDesc;
	}
	public void setUserIpDesc(String userIpDesc){
		this.userIpDesc = userIpDesc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}