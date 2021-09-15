package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @title 统一数据资源中心
 * @description 数据模型表 实体类
 * @author lc 
 * @version 2020-03-10
 */

public class DlhDataobject extends BaseBean {

	private static final long serialVersionUID = 1L;

	public DlhDataobject() {
	}

	/** 模型编码 */
	private String modelId;
	/** 数据源 */
	private String dbSource;
	/** 数据源类型 */
	private String dbType;
	/** 表名 */
	private String tableCode;
	/** 地址 */
	private String objUrl;
	/** 说明 */
	private String objName;
	/** 列表样式 */
	private String disListStyle;
	/** 表单样式 */
	private String disFormStyle;
	/** 备注 */
	private String remark;


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

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getObjUrl() {
		return objUrl;
	}

	public void setObjUrl(String objUrl) {
		this.objUrl = objUrl;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getDisListStyle() {
		return disListStyle;
	}

	public void setDisListStyle(String disListStyle) {
		this.disListStyle = disListStyle;
	}

	public String getDisFormStyle() {
		return disFormStyle;
	}

	public void setDisFormStyle(String disFormStyle) {
		this.disFormStyle = disFormStyle;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}