package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @title 统一数据资源中心
 * @description 数据模型表 实体类 Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved Company 长春奕迅
 * @author lc 
 * @version 2020-03-10
 */

public class DlhDatamodel extends BaseBean {

	private static final long serialVersionUID = 1L;

	public DlhDatamodel() {
	}

	/** 数据源 */
	private String dbSource;
	/** 数据源类型 */
	private String dbType;
	/** 表名 */
	private String tableCode;
	/** 描述 */
	private String tableName;
	/** 备注 */
	private String remark;

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDbSource() {
		return dbSource;
	}

	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

}