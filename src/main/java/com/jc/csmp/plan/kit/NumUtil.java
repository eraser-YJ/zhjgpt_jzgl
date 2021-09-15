package com.jc.csmp.plan.kit;

import java.math.BigDecimal;

/**
 * 数字操作
 */
public class NumUtil {

    /**
     * 相加
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            value1 = BigDecimal.valueOf(0);
        }
        if (value2 == null) {
            value2 = BigDecimal.valueOf(0);
        }
        return scal4(value2 .add(value1));
    }

    /**
     * 保留4位，四舍五入操作
     * @param value
     */
    public static BigDecimal scal4(BigDecimal value) {
        if(value == null){
            value = BigDecimal.valueOf(0);
        }
        //四舍五入
        return value.setScale(4,BigDecimal.ROUND_HALF_UP);
    }
}
