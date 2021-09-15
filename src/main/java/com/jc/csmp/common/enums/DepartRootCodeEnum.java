package com.jc.csmp.common.enums;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.StringUtil;

/**
 * 部门根节点id
 * @Author 常鹏
 * ssfa
 * @Date 2020/7/9 10:46
 * @Version 1.0
 */
public enum DepartRootCodeEnum {
    /***/
    EMPTY("0000"),
    COMPANY(GlobalContext.getProperty("companyDepartmentCode")),
    GOVERNMENT(GlobalContext.getProperty("governmentDepartmentCode")),
    /**不能选择的企业*/
    NOCHANGECOMPANY(GlobalContext.getProperty("noChangeCompany"));

    private String value;
    DepartRootCodeEnum(String value) {
        this.value = value;
    }

    public static DepartRootCodeEnum getByCode(String code) {
        DepartRootCodeEnum result = DepartRootCodeEnum.EMPTY;
        if (StringUtil.isEmpty(code)) {
            return result;
        }
        DepartRootCodeEnum[] array = values();
        for (DepartRootCodeEnum enums : array) {
            if (enums.toString().equals(code)) {
                result = enums;
                break;
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
