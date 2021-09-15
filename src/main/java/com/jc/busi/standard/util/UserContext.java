package com.jc.busi.standard.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jc.dlh.domain.DlhUser;

public class UserContext {
	private static ThreadLocal<Map<String, Object>> cache = new ThreadLocal<Map<String, Object>>();

	public static void init() {
		cache.remove();
		Map<String, Object> map = new HashMap<>();
		map.put("LogProcessId", UUID.randomUUID().toString().replaceAll("-", ""));
		cache.set(map);
	}

	/**
	 * @description 取得日志记录id
	 * @return
	 */
	public static String getLogProcessId() {
		Map<String, Object> map = cache.get();
		if (map == null) {
			map = new HashMap<>();
			cache.set(map);
		}
		String logProcessId = (String) map.get("LogProcessId");
		if (logProcessId == null || logProcessId.trim().length() <= 0) {
			map.put("LogProcessId", UUID.randomUUID().toString().replaceAll("-", ""));
		}
		return (String) map.get("LogProcessId");
	}

	public static DlhUser getUser() {
		Map<String, Object> map = cache.get();
		if (map == null) {
			return null;
		}
		return (DlhUser) map.get("userInfo");
	}

	public static void setUser(DlhUser user) {
		Map<String, Object> map = cache.get();
		if (map == null) {
			map = new HashMap<>();
		}
		map.put("userInfo", user);
		cache.set(map);
	}

	public static String getXML() {
		Map<String, Object> map = cache.get();
		if (map == null) {
			return null;
		}
		return (String) map.get("xmlInfo");
	}

	public static void setXML(String xml) {
		Map<String, Object> map = cache.get();
		if (map == null) {
			map = new HashMap<>();
		}
		map.put("xmlInfo", xml);
		cache.set(map);
	}
}
