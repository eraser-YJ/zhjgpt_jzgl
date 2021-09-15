package com.jc.common.db.parse.domain;

import java.util.List;

public class SqlVO {
	private String sql;
	private List<Object> voList;

	public SqlVO(String sql, List<Object> voList) {
		super();
		this.sql = sql;
		this.voList = voList;
	}

	public String getSql() {
		return sql;
	}

	public List<Object> getVoList() {
		return voList;
	}

}
