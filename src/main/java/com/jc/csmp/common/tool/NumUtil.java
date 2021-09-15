package com.jc.csmp.common.tool;

import java.math.BigDecimal;

public class NumUtil {

    public static int between(Object value, Object minValue, Object maxValue) {
        try {
            BigDecimal nowValue1 = BigDecimal.valueOf(Double.valueOf(value.toString()));
            BigDecimal maxValue1 = BigDecimal.valueOf(Double.valueOf(maxValue.toString()));
            BigDecimal minValue1 = BigDecimal.valueOf(Double.valueOf(minValue.toString()));
            if (nowValue1.compareTo(minValue1) < 0) {
                return -1;
            } else if (nowValue1.compareTo(maxValue1) > 0) {
                return 1;
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static BigDecimal valueBigDecimalOf(Object value) {
        try {
            BigDecimal nowValue1 = BigDecimal.valueOf(Double.valueOf(value.toString()));
            return nowValue1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
