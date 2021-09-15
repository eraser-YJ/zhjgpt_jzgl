package com.jc.system.api.domain;

import com.jc.foundation.domain.BaseBean;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class DeptSynclog extends BaseBean{

	private static final long serialVersionUID = 1L;

	public DeptSynclog() {
	}

	/**登录用户ID*/
	private String deptId;
	/**登录名*/
	private String deptName;
	/**显示名*/
	private String deptType;
	/**子系统标识*/
	private String subsystem;
	/**返回状态0/非0*/
	private String status;
	/**返回信息*/
	private String msg;
	/**同步时间*/
	private String syncTime;

	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String userId){
		this.deptId = userId;
	}
	public String getDeptName(){
		return deptName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	public String getDeptType(){
		return deptType;
	}
	public void setDeptType(String deptType){
		this.deptType = deptType;
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