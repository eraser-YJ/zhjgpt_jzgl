package com.jc.common.db.dialect.mysql.column.type;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.jc.common.db.SQLUtil;
import com.jc.common.db.SqlDateUtil;
import com.jc.common.db.dialect.mysql.column.IMysqlColumnType;

public class TypeBigint extends IMysqlColumnType {

	@Override
	public String getCondOnPk(String key, Object value) {
		if (value == null) {
			return key + " is null ";
		}
		return key + "=" + value;
	}

	@Override
	public String getCondOnQuery(String oper, String key, Object value) {
		if (value == null || value.toString().length() <= 0) {
			if(!"isnull".equalsIgnoreCase(oper)){
				return "";
			}

		}
		if (!SQLUtil.isAccessSql(value.toString())) {
			if ("gt".equalsIgnoreCase(oper)) {
				return " " + key + " >=  " + value;
			} else if ("lt".equalsIgnoreCase(oper)) {
				return " " + key + " <  " + value;
			}else if("notnull".equalsIgnoreCase(oper)){
				return " " + key + " is not null";
			}else if("isnull".equalsIgnoreCase(oper)){
				// 常鹏添加 2020-08-18，支持 is null的查询
				return " " + key + " is null";
			}else{
				return " " + key + " =  " + value;
			}
		}
		return "";
	}

	@Override
	public Map<String, Map<String, Object>> getCondOnDisplay(String key, String type) {
		Map<String, Map<String, Object>> condList = new HashMap<String, Map<String, Object>>();
		Map<String, Object> condLine1 = new HashMap<String, Object>();
		condLine1.put("code", key);
		condLine1.put("operationAction", "eq");
		condLine1.put("operationType", type);
		condLine1.put("operationKey", key);
		condList.put("oper", condLine1);
		return condList;
	}

	@Override
	public void setStatementParam(PreparedStatement ps, int index, Object value) throws Exception {
		if (value == null) {
			ps.setNull(index, Types.BIGINT);
		} else {
			ps.setLong(index, Long.valueOf(value.toString()));
		}
	}

	@Override
	public String getSqlOnCreate(String key, String len) throws Exception {
		if (len == null || len.trim().length() <= 0) {
			len = "20";
		}
		return " " + key + " bigint(" + len + ") ";
	}

}
