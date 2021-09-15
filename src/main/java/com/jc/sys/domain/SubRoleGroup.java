package com.jc.sys.domain;

import org.apache.log4j.Logger;

import com.jc.foundation.domain.BaseBean;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRoleGroup extends BaseBean {

//	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public SubRoleGroup() {
	}

	private String groupName; /* 角色组名称 */
	private String groupDescription; /* 角色组描述 */
	private Integer status; /* 状态 */
//	private Integer weight; /* 权重 */
	private String secret; /* 密级 */

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}