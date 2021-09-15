package com.jc.csmp.workflow.domain;

/**
 * @Author 常鹏
 * @Date 2020/8/21 10:49
 * @Version 1.0
 */
public class TodoExtendBean {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static TodoExtendBean create(String code, String name) {
        TodoExtendBean entity = new TodoExtendBean();
        entity.setCode(code);
        entity.setName(name);
        return entity;
    }
}
