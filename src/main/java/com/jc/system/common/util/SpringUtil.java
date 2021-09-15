package com.jc.system.common.util;

import com.jc.foundation.util.StringUtil;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SpringUtil {

	private SpringUtil() {
		throw new IllegalStateException("SpringUtil class");
	}

	public static final WebApplicationContext CTX = null;

	/**
	 * 根据serviceStr获得spring里面的名字
	 * @param serviceStr
	 * @return
	 */
	public static String getBeanName(String serviceStr) {
		String result = "";
		int position = serviceStr.lastIndexOf('.');
		result = serviceStr.substring(position + 1);
		result = StringUtil.firstLower(result);
		return result;
	}
}
