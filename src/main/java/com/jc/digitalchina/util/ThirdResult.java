package com.jc.digitalchina.util;

/**
 * @Author 常鹏
 * @Date 2020/9/10 15:29
 * @Version 1.0
 */
public class ThirdResult {
    private Integer code;
    private String message;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
