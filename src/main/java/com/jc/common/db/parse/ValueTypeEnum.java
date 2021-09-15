package com.jc.common.db.parse;

import com.jc.common.db.parse.valuetype.DayBegin;
import com.jc.common.db.parse.valuetype.IValueType;
import com.jc.common.db.parse.valuetype.MonthBegin;

import java.util.Map;

public enum  ValueTypeEnum {
    monthBegin(new MonthBegin()),
    dayBegin(new DayBegin());
    private IValueType server;

    public IValueType getServer() {
        return server;
    }

    ValueTypeEnum(IValueType myType){
        server = myType;
    }

    public static Object getValue(String type, Object value, Map<String, String> propertyMap){
        try {
            ValueTypeEnum typeEnum = ValueTypeEnum.valueOf(type);
            if(typeEnum == null){
                return value;
            }
            return typeEnum.getServer().getValue(value,propertyMap);
        } catch(Exception ex){
            return value;
        }
    }

}
