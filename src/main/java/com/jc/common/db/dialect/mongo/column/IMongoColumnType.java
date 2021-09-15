package com.jc.common.db.dialect.mongo.column;

import java.sql.PreparedStatement;
import java.util.Map;

public abstract class IMongoColumnType {

	public abstract String getCondOnPk(String key, Object value);

	public abstract String getCondOnQuery(String oper, String key, Object value);

	public abstract Map<String, Map<String, Object>> getCondOnDisplay(String key, String type);

	public String getDisplayValue(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}

	public abstract String getSqlOnCreate(String key, String len) throws Exception;

	public abstract void setStatementParam(PreparedStatement ps, int index, Object value) throws Exception;
}
