package com.jc.common.check;

public class RuleRequired implements ICheckRule {

	@Override
	public String check(String cmd, String key, Object value) {
		if ("true".equalsIgnoreCase(cmd)) {
			if (value == null || key.trim().length() <= 0) {
				return key + "属性不能为空";
			}
		}
		return null;
	}

}
