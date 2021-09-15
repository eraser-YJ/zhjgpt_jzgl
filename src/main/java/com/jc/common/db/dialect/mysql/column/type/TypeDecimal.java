package com.jc.common.db.dialect.mysql.column.type;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.jc.common.db.SQLUtil;
import com.jc.common.db.SqlDateUtil;
import com.jc.common.db.dialect.mysql.column.IMysqlColumnType;

public class TypeDecimal extends IMysqlColumnType {

	@Override
	public String getCondOnPk(String key, Object value) {
		if (value == null) {
			return key + "is null ";
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
		condLine1.put("code", key + "_Begin");
		condLine1.put("operationAction", "gt");
		condLine1.put("operationType", type);
		condLine1.put("operationKey", key);
		condList.put("begin", condLine1);
		Map<String, Object> condLine2 = new HashMap<String, Object>();
		condLine2.put("code", key + "_end");
		condLine2.put("operationAction", "lt");
		condLine2.put("operationType", type);
		condLine2.put("operationKey", key);
		condList.put("end", condLine2);
		return condList;
	}

	@Override
	public void setStatementParam(PreparedStatement ps, int index, Object value) throws Exception {
		if (value == null) {
			ps.setNull(index, Types.DECIMAL);
		} else {
			ps.setBigDecimal(index, BigDecimal.valueOf(Double.valueOf(value.toString())));
		}

	}

	@Override
	public String getSqlOnCreate(String key, String len) throws Exception {
		if (len == null || len.trim().length() <= 0) {
			len = "16,2";
		}
		return " " + key + " decimal(" + len + ") ";
	}

}
