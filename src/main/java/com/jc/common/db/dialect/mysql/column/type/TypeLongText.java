package com.jc.common.db.dialect.mysql.column.type;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import com.jc.common.db.SQLUtil;
import com.jc.common.db.dialect.mysql.column.IMysqlColumnType;

public class TypeLongText extends IMysqlColumnType {
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
			if(!"isnull".equalsIgnoreCase(oper)){
				return "";
			}
		}
		// 是否包含非法格式数据
		if (!SQLUtil.isAccessSql(value.toString())) {

			if("notnull".equalsIgnoreCase(oper)){
				return " " + key + " is not null";
			}else if("isnull".equalsIgnoreCase(oper)){
				// 常鹏添加 2020-08-18，支持 is null的查询
				return " " + key + " is null";
			}else{
				return key + " like '%" + value + "%'";
			}


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
		if (value == null) {
			value = "";
		}
		ps.setString(index, value.toString());
	}

	@Override
	public String getSqlOnCreate(String key, String len) throws Exception {

		return " " + key + " longtext";

	}
}
