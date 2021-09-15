package com.jc.archive.domain;

import com.jc.foundation.domain.BaseBean;


/**
 * @title  GOA2.0源代码
 * @description  实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-19
 */

public class SubPermission extends BaseBean{
	private static final long serialVersionUID = 1L;
	private String permissionId;   /**/
	private Integer dataType;   /*0 用户，  1 部门*/
	private String controlId;   /*如果选择的是用户，存用户id，如果选择的是部门存部门id*/
	private String controlName;   /*如果选择的是用户，存用户名称，如果选择的是部门存部门名称*/

	private String folderId;

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getDataType(){
		return dataType;
	}
	
	public void setDataType(Integer dataType){
		this.dataType = dataType;
	}


	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public String getControlName(){
		return controlName;
	}
	
	public void setControlName(String controlName){
		this.controlName = controlName;
	}
	
}