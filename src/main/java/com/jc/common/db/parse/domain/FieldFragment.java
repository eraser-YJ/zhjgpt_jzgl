package com.jc.common.db.parse.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jc.common.db.parse.ValueTypeEnum;
import com.jc.common.kit.DateUtil;

public class FieldFragment implements IFragment {
	private static final String FIELD_ATT_JDBCTYPE = "jdbctype";

	private static final String FIELD_ATT_JDBCTYPE_DOUBLE = "DOUBLE";

	private static final String FIELD_ATT_JDBCTYPE_INT = "INT";

	private static final String FIELD_ATT_JDBCTYPE_LONG = "LONG";

	private static final String FIELD_ATT_JDBCTYPE_BIGINT = "BIGINT";

	private static final String FIELD_ATT_JDBCTYPE_DATE = "DATE";

	private static final String FIELD_ATT_JDBCTYPE_DATETIME = "DATETIME";

	private static final String FIELD_ATT_JDBCTYPE_FUNCTION = "function";

	private String oriSql;

	private String fieldName;

	private boolean direct;

	private Map<String, String> propertyMap = new HashMap<>();

	public FieldFragment(String inSql, boolean direct) {
		this.oriSql = inSql;
		this.direct = direct;
		String tempSql = oriSql.trim();
		String fieldStr = tempSql.substring(2, tempSql.length() - 1);
		fieldStr = fieldStr.trim();
		fieldStr = fieldStr.replace("\"","");
		fieldStr = fieldStr.replace("\'","");
		String[] names = fieldStr.split(",");
		fieldName = names[0].trim();
		if (names.length > 1) {
			String att;
			String[] attKeyValue;
			for (int i = 1; i < names.length; i++) {
				att = names[i].trim();
				attKeyValue = att.split("=");
				if (attKeyValue.length > 1) {
					propertyMap.put(attKeyValue[0].trim().toLowerCase(), attKeyValue[1].trim());
				} else {
					propertyMap.put(attKeyValue[0].trim().toLowerCase(), "");
				}

			}
		}
	}

	private Object getValue(Object value) {
		String type = propertyMap.get(FIELD_ATT_JDBCTYPE);
		if (FIELD_ATT_JDBCTYPE_FUNCTION.equalsIgnoreCase(type)){
			return ValueTypeEnum.getValue(fieldName,value,propertyMap);
		}

		if (value == null) {
			return null;
		}
		String tempValue = value.toString().trim();
		if (tempValue.length() <= 0) {
			return null;
		}
		if (type == null || type.trim().length() == 0) {
			return tempValue;
		}

		if (FIELD_ATT_JDBCTYPE_DOUBLE.equalsIgnoreCase(type)) {
			return Double.valueOf(tempValue);
		} else if (FIELD_ATT_JDBCTYPE_INT.equalsIgnoreCase(type) || FIELD_ATT_JDBCTYPE_LONG.equalsIgnoreCase(type) || FIELD_ATT_JDBCTYPE_BIGINT.equalsIgnoreCase(type)) {
			return Long.valueOf(tempValue);
		} else if (FIELD_ATT_JDBCTYPE_DATE.equalsIgnoreCase(type)) {
			return DateUtil.extractDate(tempValue);
		} else if (FIELD_ATT_JDBCTYPE_DATETIME.equalsIgnoreCase(type)) {
			return DateUtil.extractDateTime(tempValue);
		}  else {
			return tempValue;
		}
	}

	@Override
	public List<SqlExePara> boundSql(Map<String, Object> paraMap) {
		Object value = paraMap.get(fieldName);
		Object newValue = getValue(value);
		if (newValue == null) {
			return Arrays.asList(new SqlExePara(oriSql, null));
		}
		if (direct) {
			return Arrays.asList(new SqlExePara(newValue.toString(), null));
		}
		return Arrays.asList(new SqlExePara(" ? ", getValue(value)));
	}

}
