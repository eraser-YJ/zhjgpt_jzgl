package com.jc.resource.bean;

/**
 * @Author 常鹏
 * @Date 2020/7/27 14:28
 * @Version 1.0
 */
public enum ResourceOperatorTypeEnum {
    /***/
    varchar("varchar", "字符"),
    longtext("longtext", "长字段"),
    datetime("datetime", "日期"),
    intEx("int", "整型"),
    bigint("bigint", "长整型"),
    decimal("decimal", "浮点型"),
    dic("dic", "字典");

    private String type;
    private String desc;

    ResourceOperatorTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
