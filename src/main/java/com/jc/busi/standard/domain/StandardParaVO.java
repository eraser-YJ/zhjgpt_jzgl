package com.jc.busi.standard.domain;

import java.util.List;
import java.util.Map;

public class StandardParaVO {
	private List<Map<String, Object>> info;
	private Integer rows;

	public List<Map<String, Object>> getInfo() {
		return info;
	}

	public void setInfo(List<Map<String, Object>> info) {
		this.info = info;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

}
