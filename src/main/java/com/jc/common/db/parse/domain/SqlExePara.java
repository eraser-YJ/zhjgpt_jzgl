package com.jc.common.db.parse.domain;

public class SqlExePara {
	private String sqlFragment;
	private Object value;

	public SqlExePara(String sqlFragment, Object value) {
		super();
		this.sqlFragment = sqlFragment;
		this.value = value;
	}

	public String getSqlFragment() {
		return sqlFragment;
	}

	public Object getValue() {
		return value;
	}

}
