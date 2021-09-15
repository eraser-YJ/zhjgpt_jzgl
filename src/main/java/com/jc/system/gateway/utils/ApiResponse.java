package com.jc.system.gateway.utils;

import com.jc.foundation.util.ResultCode;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * @author Administrator
 * @date 2020-07-01
 */
public class ApiResponse extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public ApiResponse() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static ApiResponse error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
	}
	
	public static ApiResponse error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}
	
	public static ApiResponse error(int code, String msg) {
		ApiResponse r = new ApiResponse();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static ApiResponse error(ResultCode resultCode) {
		ApiResponse r = new ApiResponse();
		r.put("code", resultCode.code());
		r.put("msg", resultCode.message());
		return r;
	}

	public static ApiResponse ok(String msg) {
		ApiResponse r = new ApiResponse();
		r.put("msg", msg);
		return r;
	}
	
	public static ApiResponse ok(Map<String, Object> map) {
		ApiResponse r = new ApiResponse();
		r.putAll(map);
		return r;
	}

	public static ApiResponse okBody(Object data) {
		ApiResponse r = new ApiResponse();
		r.put("body", data);
		return r;
	}
	
	public static ApiResponse ok() {
		return new ApiResponse();
	}

	@Override
	public ApiResponse put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
