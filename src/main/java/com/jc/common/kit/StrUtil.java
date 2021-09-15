package com.jc.common.kit;

public class StrUtil {

	public static boolean isTrue(String value) {
		if (value == null || value.trim().length() == 0) {
			return false;
		}
		String tempValue = value.trim();
		if ("Y".equalsIgnoreCase(tempValue)) {
			return true;
		}
		if ("Yes".equalsIgnoreCase(tempValue)) {
			return true;
		}
		if ("是".equals(tempValue)) {
			return true;
		}
		if ("*".equals(tempValue)) {
			return true;
		}
		return false;
	}

	public static String toBoolValue(String value) {
		if (isTrue(value)) {
			return "是";
		}
		return "否";
	}

	public static String trim(String value) {
		if (value == null || value.trim().length() == 0) {
			return "";
		}
		String tempValue = value.trim();
		while (tempValue.indexOf("\r") > 0) {
			tempValue = tempValue.replace("\r", "");
		}
		while (tempValue.indexOf("\n") > 0) {
			tempValue = tempValue.replace("\n", "");
		}
		return tempValue;
	}

	public static String trim2(String value) {
		String tempValue = trim(value);
		while (tempValue.indexOf(" ") > 0) {
			tempValue = tempValue.replace("[ ]+", "");
		}
		return tempValue;
	}

	public static String trim3(String value) {
		String tempValue = trim2(value);
		while (tempValue.indexOf("<br>") > 0) {
			tempValue = tempValue.replace("<br>", "");
		}
		while (tempValue.indexOf("<BR>") > 0) {
			tempValue = tempValue.replace("<BR>", "");
		}
		return tempValue;
	}

	/**
	 * 标准化数据
	 * 
	 * @param value
	 * @return
	 */
	public static String standard(String value) {
		if (value == null) {
			return "";
		}
		String tempValue = value;
		while (tempValue.indexOf("、") > 0) {
			tempValue = tempValue.replace("、", ",");
		}
		while (tempValue.indexOf("，") > 0) {
			tempValue = tempValue.replace("，", ",");
		}
		while (tempValue.indexOf("|") > 0) {
			tempValue = tempValue.replace("|", ",");
		}
		while (tempValue.indexOf("（") > 0) {
			tempValue = tempValue.replace("（", "(");
		}
		while (tempValue.indexOf("）") > 0) {
			tempValue = tempValue.replace("）", ")");
		}
		while (tempValue.indexOf("/") > 0) {
			tempValue = tempValue.replace("/", ",");
		}
		while (tempValue.indexOf(" ") > 0) {
			tempValue = tempValue.replace(" ", ",");
		}
		while (tempValue.indexOf(",,") > 0) {
			tempValue = tempValue.replace(",,", ",");
		}
		return tempValue;
	}
}
