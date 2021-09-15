package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SysRole extends BaseBean implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**名称*/
	private String name;
	/**描述*/
	private String description;
	/**角色所在部门id*/
	private String deptId;
	/**排序*/
	private Integer queue;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	@Override
    public Integer getDeleteFlag(){
		return deleteFlag;
	}
	@Override
    public void setDeleteFlag(Integer deleteFlag){
		this.deleteFlag = deleteFlag;
	}
	public Integer getQueue(){
		return queue;
	}
	public void setQueue(Integer queue){
		this.queue = queue;
	}
}