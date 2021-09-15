package com.jc.common.db.parse.domain;

import java.util.List;
import java.util.Map;

public interface ISegment {

	public List<SqlExePara> boundSql(Map<String, Object> paraMap) throws Exception;

	public static ISegment buildString(String fragment) throws Exception {
		return build("string", fragment);
	}

	public static ISegment build(String code, String fragment) throws Exception {
		if ("if".equalsIgnoreCase(code)) {
			return new IFSegment(fragment);
		} else if ("foreach".equalsIgnoreCase(code)) {
			return new ForSegment(fragment);
		} else if ("string".equalsIgnoreCase(code)) {
			return new StringSegment(fragment);
		}
		return null;
	}

	public static boolean isSupportSegment(String code) throws Exception {
		if ("if".equalsIgnoreCase(code)) {
			return true;
		} else if ("foreach".equalsIgnoreCase(code)) {
			return true;
		}
		return false;
	}

}
