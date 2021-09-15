package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;

/**
 * @author lc 
 * @version 2020-03-10
 */
public class DlhTableMap extends BaseBean {

	private static final long serialVersionUID = 1L;
	public DlhTableMap() {
	}
	private String objUrlK;
	private String objUrlV;
	private String tableNameK;
	private String tableNameV;
	private String columnNameK;
	private String columnNameV;
	private String columnType;
	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setObjUrlK(String objUrlK) {
		this.objUrlK = objUrlK;
	}
	public String getObjUrlK() {
		return objUrlK;
	}
	public void setObjUrlV(String objUrlV) {
		this.objUrlV = objUrlV;
	}
	public String getObjUrlV() {
		return objUrlV;
	}
	public void setTableNameK(String tableNameK) {
		this.tableNameK = tableNameK;
	}
	public String getTableNameK() {
		return tableNameK;
	}
	public void setTableNameV(String tableNameV) {
		this.tableNameV = tableNameV;
	}
	public String getTableNameV() {
		return tableNameV;
	}
	public void setColumnNameK(String columnNameK) {
		this.columnNameK = columnNameK;
	}
	public String getColumnNameK() {
		return columnNameK;
	}
	public void setColumnNameV(String columnNameV) {
		this.columnNameV = columnNameV;
	}
	public String getColumnNameV() {
		return columnNameV;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getColumnType() {
		return columnType;
	}
}