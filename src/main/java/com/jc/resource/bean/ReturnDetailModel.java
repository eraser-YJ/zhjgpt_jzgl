package com.jc.resource.bean;

import java.util.List;

/**
 * @Author 常鹏
 * @Date 2020/7/27 16:37
 * @Version 1.0
 */
public class ReturnDetailModel {
    public ReturnDetailModel(){}
    /**0000成功*/
    private String code;
    /**消息*/
    private String message;
    private List<DetailData> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DetailData> getData() {
        return data;
    }

    public void setData(List<DetailData> data) {
        this.data = data;
    }

    public boolean check() {
        if (this.getCode() != null && this.getCode().equals("0000")) {
            return true;
        } else {
            return false;
        }
    }
}
