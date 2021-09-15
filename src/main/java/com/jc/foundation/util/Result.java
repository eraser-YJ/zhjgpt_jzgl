package com.jc.foundation.util;

import java.io.Serializable;

/**
 * 结果通用格式
 * @author Administrator
 * @date 2020-06-30
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    private Object data;

    private String type;

    private Long ts;

    public Result() {}

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.ts = System.currentTimeMillis();
    }

    public Result(Integer code, String msg, Long ts) {
        this.code = code;
        this.msg = msg;
        this.ts = ts;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result success(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.setResultCode(0, msg);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setResultCode(0, msg);
        result.setData(data);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result failure(Integer code, String msg) {
        Result result = new Result();
        result.setResultCode(code, msg);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public static Result failure(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setResultCode(code, msg);
        result.setData(data);
        result.setTs(System.currentTimeMillis());
        return result;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void setResultCode(Integer code, String msg) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    this.code = code;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    this.msg = msg;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public boolean isSuccess() {
        if (this.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
            return true;
        }
        return false;
    }

    public String getType() {
        type = "fail";
        if (this.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
            type = "success";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getSuccess() {
        return isSuccess();
    }

}
