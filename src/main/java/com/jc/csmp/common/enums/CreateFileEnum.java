package com.jc.csmp.common.enums;

/**
 * 归档类型枚举
 * @Author 常鹏
 * @Date 2020/8/31 16:09
 * @Version 1.0
 */
public enum CreateFileEnum {
    /***/
    change("变更单"),
    relation("联系单"),
    visa("签证单"),
    sceneQuestion("安全问题"),
    plan("项目计划");
    private String value;
    CreateFileEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
