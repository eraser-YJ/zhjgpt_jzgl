package com.jc.system.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class CharConventUtils {
	
	protected static final Logger logger = Logger.getLogger(CharConventUtils.class);
	
	private CharConventUtils() {
		throw new IllegalStateException("CharConventUtils class");
	}
	
	/**
	 * @description 文件下载中文名编码转换
	 * @param fileName 要转换的中文名
	 * @return 转换后的中文名
	 */
	public static String encodingFileName(String fileName) {
		String returnFileName = "";
		try {
			returnFileName = URLEncoder.encode(fileName, "UTF-8");
			returnFileName = StringUtils.replace(returnFileName, "+", "%20");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return returnFileName;
	}
}
