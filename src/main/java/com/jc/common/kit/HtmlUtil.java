package com.jc.common.kit;

public class HtmlUtil {
	/**
	 * 删除所有的HTML标签
	 * 
	 * @param source 需要进行除HTML的文本
	 * @return
	 */
	public static String deleteAllHTMLTag(String source) {
		if (source == null) {
			return "";
		}
		String s = source;
		/** 删除普通标签 */
		s = s.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
		/** 删除转义字符 */
		s = s.replaceAll("&.{2,6}?;", "");
		return s;
	}

}
