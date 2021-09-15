package com.jc.common.db.dialect.mysql.column;

public class MysqlColumn {

	private String dataType;
	private String name;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDate() {
		if ("DATE".equalsIgnoreCase(dataType)) {
			return true;
		}
		if ("TIMESTAMP".equalsIgnoreCase(dataType)) {
			return true;
		}
		if ("DATETIME".equalsIgnoreCase(dataType)) {
			return true;
		}
		return false;
	}

	public boolean isNum() {
		if ("NUMBER".equalsIgnoreCase(dataType)) {
			return true;
		}
		if ("BIGINT".equalsIgnoreCase(dataType) || "INT".equalsIgnoreCase(dataType) || "TINYINT".equalsIgnoreCase(dataType) || "SMALLINT".equalsIgnoreCase(dataType)) {
			return true;
		}
		return false;
	}

	public String getJavaType() {
		if (this.isDate()) {
			return "Date";
		} else if (this.isNum()) {
			return "Long";
		}
		return "String";
	}
}
