package com.jc.supervise.warning.enums;

import com.jc.foundation.util.StringUtil;

/**
 * @Author 常鹏
 * @Date 2020/8/11 15:36
 * @Version 1.0
 */
public enum SupervisionWarningStatusEnums {
    /***/
    empty(""),
    no("未处理"),
    reopen("重新打开"),
    finish("已处理");

    private String value;
    SupervisionWarningStatusEnums(String value) {
        this.value = value;
    }

    public static SupervisionWarningStatusEnums getByCode(String code) {
        SupervisionWarningStatusEnums result = SupervisionWarningStatusEnums.empty;
        if (!StringUtil.isEmpty(code)) {
            SupervisionWarningStatusEnums[] array = values();
            for (SupervisionWarningStatusEnums enums : array) {
                if (enums.toString().equals(code)) {
                    result = enums;
                    break;
                }
            }
        }
        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
