package com.jc.common.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleRegular implements ICheckRule {

	@Override
	public String check(String cmd, String key, Object value) {
		if (value == null || key.trim().length() <= 0) {
			return null;
		}
		if (cmd == null || cmd.trim().length() <= 0) {
			return key + "的正则表达不存在";
		}
		try {
			Pattern pattern = Pattern.compile(cmd);
			Matcher matcher = pattern.matcher(value.toString());
			if (!matcher.matches()) {
				return key + "的值(" + value + ")不合法，不符合正则表达式：" + cmd;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return key + "的检查正则表达异常：" + e.getMessage();
		}
		return null;
	}

}
