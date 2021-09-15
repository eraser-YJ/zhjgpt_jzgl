package com.jc.common.db.parse.valuetype;

import com.jc.common.kit.DateUtil;

import java.util.Date;
import java.util.Map;

public class MonthBegin implements IValueType {
    private static final String FIELD_ATT_INTERVAL = "interval";
    @Override
    public Object getValue(Object value, Map<String, String> propertyMap) {
        String intervalStr = propertyMap.get(FIELD_ATT_INTERVAL);
        int interval = 0;
        try {
            interval = Integer.valueOf(intervalStr.trim());
        } catch (Exception ex) {
            interval = 0;
        }
        try {
            Date beginDate = DateUtil.addMonth(new Date(), interval);
            return DateUtil.monthBegin(beginDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }
}
