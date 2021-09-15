package com.jc.common.db.dialect.mongo.column.type;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import com.jc.common.db.SQLUtil;
import com.jc.common.db.dialect.mongo.column.IMongoColumnType;

public class TypeString extends IMongoColumnType {
	@Override
	public String getCondOnPk(String key, Object value) {
		if (value == null) {
			return key + " is null ";
		}
		return key + "='" + value + "' ";
	}

	@Override
	public String getCondOnQuery(String oper, String key, Object value) {
		if (value == null || value.toString().length() <= 0) {
			return "";
		}
		// 是否包含非法格式数据
		if (!SQLUtil.isAccessSql(value.toString())) {
			return key + " like '%" + value + "%'";
		}
		return "";
	}

	@Override
	public Map<String, Map<String, Object>> getCondOnDisplay(String key, String type) {
		Map<String, Map<String, Object>> condList = new HashMap<String, Map<String, Object>>();
		Map<String, Object> condLine1 = new HashMap<String, Object>();
		condLine1.put("code", key);
		condLine1.put("operationAction", "like");
		condLine1.put("operationType", type);
		condLine1.put("operationKey", key);
		condList.put("oper", condLine1);
		return condList;
	}

	@Override
	public void setStatementParam(PreparedStatement ps, int index, Object value) throws Exception {
		ps.setString(index, (String) value);
	}

	@Override
	public String getSqlOnCreate(String key, String len) throws Exception {
		if (len == null || len.trim().length() <= 0) {
			len = "64";
		}
		return " " + key + " varchar(" + len + ") ";

	}
}
