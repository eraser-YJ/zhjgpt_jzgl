package com.jc.common.db.dialect.mysql.column.type;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.jc.common.db.SqlDateUtil;
import com.jc.common.db.dialect.mysql.column.IMysqlColumnType;
import com.jc.common.kit.StringUtil;

public class TypeDatetime extends IMysqlColumnType {

	@Override
	public String getCondOnPk(String key, Object value) {
		String strValue = SqlDateUtil.getDateStr(value);
		if (strValue == null) {
			return key + " is null ";
		}
		return key + "=str_to_date(" + strValue + ",'%Y-%m-%d %H:%i:%s') ";
	}

	@Override
	public String getCondOnQuery(String oper, String key, Object value) {
		if ("gt".equalsIgnoreCase(oper)) {
			return " " + key + " >= str_to_date('" + SqlDateUtil.getBeginDateStr(value) + "','%Y-%m-%d %H:%i:%s') ";
		} else if ("lt".equalsIgnoreCase(oper)) {
			return " " + key + " < str_to_date('" + SqlDateUtil.getEndDateStr(value) + "','%Y-%m-%d %H:%i:%s') ";
		} else if("between".equalsIgnoreCase(oper)){
			String[] values = value.toString().split(",");
			return " " + key + " >= str_to_date('" + SqlDateUtil.getBeginDateStr(values[0]) + "','%Y-%m-%d %H:%i:%s') "
					+" and " + key + " <= str_to_date('" + SqlDateUtil.getBeginDateStr(values[1]) + "','%Y-%m-%d %H:%i:%s') ";
		}else if("notnull".equalsIgnoreCase(oper)){
			return " " + key + " is not null";
		}else if("isnull".equalsIgnoreCase(oper)){
			// 常鹏添加 2020-08-18，支持 is null的查询
			return " " + key + " is null";
		}else{
			return " " + key + " = str_to_date('" + SqlDateUtil.getDateStr(value) + "','%Y-%m-%d %H:%i:%s') ";
		}

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
		String strValue = SqlDateUtil.getDateStr(value);
		if (strValue == null) {
			ps.setNull(index, Types.DATE);
		} else {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			ps.setDate(index, new java.sql.Date(f.parse(strValue).getTime()));
		}

	}

	@Override
	public String getSqlOnCreate(String key, String len) throws Exception {
		return " " + key + " datetime ";
	}

	@Override
    public String getDisplayValue(Object value) {
		if (value == null) {
			return "";
		}
		return SqlDateUtil.getDateStr(value);
	}

}
