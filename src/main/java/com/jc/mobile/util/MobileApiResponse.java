package com.jc.mobile.util;

import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;

import java.util.HashMap;

/**
 * 移动端返回数据
 * @Author 常鹏
 * @Date 2020/7/31 9:16
 * @Version 1.0
 */
public class MobileApiResponse extends HashMap<String, Object> {
    public final static int INTERNAL_SERVER_ERROR = 500;
    public MobileApiResponse() {
        put("code", 0);
        put("msg", "success");
    }

    public static MobileApiResponse error() {
        return error(INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static MobileApiResponse error(String msg) {
        return error(INTERNAL_SERVER_ERROR, msg);
    }

    public static MobileApiResponse error(int code, String msg) {
        MobileApiResponse r = new MobileApiResponse();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static MobileApiResponse error(ResultCode resultCode) {
        MobileApiResponse r = new MobileApiResponse();
        r.put("code", resultCode.code());
        r.put("msg", resultCode.message());
        return r;
    }

    public static MobileApiResponse ok() {
        return new MobileApiResponse();
    }

    public static MobileApiResponse okMsg(String msg) {
        MobileApiResponse r = new MobileApiResponse();
        r.put("msg", msg);
        return r;
    }

    public static MobileApiResponse ok(Object body) {
        MobileApiResponse r = new MobileApiResponse();
        r.put("body", body);
        return r;
    }

    public static MobileApiResponse fromResult(Result res) {
        MobileApiResponse r = new MobileApiResponse();
        r.put("code", res.getCode());
        r.put("msg", res.getMsg());
        r.put("body", res.getData());
        return r;
    }

    public boolean isSuccess() {
        if (((Integer)get("code")).intValue() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 使用请注意，只有获取用户id时才能使用
     * @return
     */
    public String getUserId() {
        return (String) get("body");
    }

    @Override
    public MobileApiResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
