package com.jc.common.db.dialect.mysql;

import java.util.List;

import com.jc.common.db.NameUtil;
import com.jc.common.db.dialect.mysql.column.MysqlColumn;

public class MysqlTableVO {
	private List<MysqlColumn> cols;
	private String table;
	private String[] pks;
	private String pk = null;
	private String pkName = null;

	public String getKey() {
		if (pk == null) {
			if (pks == null || pks.length == 0) {
				pk = cols.get(0).getName().toUpperCase();
			} else {
				pk = pks[0].toUpperCase();
			}
		}
		return pk;
	}

	public String getKeyJavaName() {
		if (pkName == null) {
			String key = this.getKey();
			return NameUtil.fieldName(key);
		}
		return pk;

	}

	public List<MysqlColumn> getCols() {
		return cols;
	}

	public void setCols(List<MysqlColumn> cols) {
		this.cols = cols;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

}
