package com.jc.common.kit;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 配置文件相关
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class SysContext {

	private static Properties loader = new Properties();
	static {
		try {
			URL url = SysContext.class.getClassLoader().getResource("jcap.properties");
			FileInputStream in = new FileInputStream(url.getFile());
			loader.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取变量值
	 * 
	 * @param property 系统变量名
	 * @return
	 */
	public static String getProperty(String property) {
		return loader.getProperty(property);
	}

	/**
	 * 获取变量值
	 * 
	 * @param property 系统变量名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getProperty(String property, String defaultValue) {
		String value = loader.getProperty(property, defaultValue);
		if (value == null || value.trim().length() == 0) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获取Long类型变量值
	 * 
	 * @param property 系统变量名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static long getLongProperty(String property, long defaultValue) {
		String value = getProperty(property);
		try {
			if (value == null || value.trim().length() == 0) {
				return defaultValue;
			}
			return Long.valueOf(value.trim());
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 获取Integer类型变量值
	 * 
	 * @param property 系统变量名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int getIntegerProperty(String property, int defaultValue) {
		String value = getProperty(property);
		try {
			if (value == null || value.trim().length() == 0) {
				return defaultValue;
			}
			return Integer.valueOf(value.trim());
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}
}
