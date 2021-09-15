package com.jc.csmp.common.enums;

import com.jc.foundation.util.StringUtil;

/**
 * 工作流审核状态
 * @author changpeng
 * @version 2020/7/11
 */
public enum WorkflowAuditStatusEnum {
    /***/
    empty(""),
    ing("审核中"),
    fail("审核未通过"),
    finish("审核完成");
    private String value;
    WorkflowAuditStatusEnum(String value) {
        this.value = value;
    }

    public static WorkflowAuditStatusEnum getByCode(String code) {
        WorkflowAuditStatusEnum result = WorkflowAuditStatusEnum.empty;
        if (StringUtil.isEmpty(code)) {
            return result;
        }
        WorkflowAuditStatusEnum[] array = values();
        for (WorkflowAuditStatusEnum enums : array) {
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
