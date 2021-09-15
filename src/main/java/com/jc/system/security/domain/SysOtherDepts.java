package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SysOtherDepts extends BaseBean implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**用户ID*/
	private String userId;
	/**用户所在部门*/
	private String deptId;
	/**用户职务*/
	private String dutyId;
	/**排序*/
	private Integer queue;
	private String deptName;
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	public String getDutyId(){
		return dutyId;
	}
	public void setDutyId(String dutyId){
		this.dutyId = dutyId;
	}
	public Integer getQueue(){
		return queue;
	}
	public void setQueue(Integer queue){
		this.queue = queue;
	}
	@Override
    public Integer getDeleteFlag(){
		return deleteFlag;
	}
	@Override
    public void setDeleteFlag(Integer deleteFlag){
		this.deleteFlag = deleteFlag;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}