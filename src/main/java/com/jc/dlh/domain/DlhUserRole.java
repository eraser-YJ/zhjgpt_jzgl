package com.jc.dlh.domain;

import org.apache.log4j.Logger;

import com.jc.foundation.domain.BaseBean;

/**
 * @title 统一数据资源中心
 * @description 用户权限 实体类 
 * @author lc 
 * @version 2020-03-18
 */

public class DlhUserRole extends BaseBean {

	protected transient final Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	public DlhUserRole() {
	}

	private String userId; /* 用户ID */
	private String dataId; /* 数据ID */
	private String userName; /* 用户名称 */
	private String objName; /* 说明 */
	private String objUrl; /* 地址 */
	private Integer batchNum; /* 一次最大量 */
	private String isEnable; /* 是否启用 */
//	private List<DlhUserRoleTreeVo> treeList = new ArrayList<>();
	private String dataIds;
	private String dataNames;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjUrl() {
		return objUrl;
	}

	public void setObjUrl(String objUrl) {
		this.objUrl = objUrl;
	}

	public Integer getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(Integer batchNum) {
		this.batchNum = batchNum;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getDataIds() {
		return dataIds;
	}

	public void setDataIds(String dataIds) {
		this.dataIds = dataIds;
	}

	public String getDataNames() {
		return dataNames;
	}

	public void setDataNames(String dataNames) {
		this.dataNames = dataNames;
	}
}