package com.jc.common.db.parse.valuetype;

import com.jc.common.kit.DateUtil;

import java.util.Date;
import java.util.Map;

public class DayBegin implements IValueType{

    @Override
    public Object getValue(Object value, Map<String, String> propertyMap) {

        try{
            return DateUtil.begin(new Date());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return value;
    }
}
