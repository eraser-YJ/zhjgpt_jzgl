package com.jc.common.db.parse.valuetype;

import java.util.Map;

public interface IValueType {
    Object getValue(Object value, Map<String, String> propertyMap);
}
