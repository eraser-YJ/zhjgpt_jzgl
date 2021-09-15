package com.jc.common.kit;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NVLUtil {
	public static String nvlStr(String v) {
		String nv = nvl(v);
		if (nv == null) {
			return "null";
		}
		return "'" + nv + "'";
	}

	public static String nvlStr(Long v) {
		String nv = nvl(v);
		if (nv == null) {
			return "null";
		}
		return String.valueOf(nv);
	}

	public static String nvlDate(Date v) {
		if (v == null) {
			return "null";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "'" + f.format(v) + "'";
	}

	/**
	 * 判断值
	 * 
	 * @param value
	 * @return
	 */
	public static String nvl(String value) {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		return value.trim();
	}

	/**
	 * 判断值
	 * 
	 * @param value
	 * @return
	 */
	public static String nvl(Long value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * 判断值
	 * 
	 * @param value
	 * @return
	 */
	public static boolean empty(String value) {
		if (value == null || value.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static Timestamp convert(Date date) {
		if (date == null) {
			return null;
		}
		Timestamp t = new Timestamp(date.getTime());
		return t;
	}

	public static Timestamp convert1(Date date) {
		if (date == null) {
			return new Timestamp(System.currentTimeMillis());
		}
		Timestamp t = new Timestamp(date.getTime());
		return t;
	}
	
	public static Long toLong(String s){
		return toLong(s , null);
	}
	
	public static Long toLong(String s , Long defaultValue){
		if (s == null || "".equals(s.trim()))
			return defaultValue;
		try {
			return Long.parseLong(s.trim()); 
		}catch(NumberFormatException nfe) {
			return defaultValue; 
		}
	}
	
	/**
	 * 根据指定字符分割字符串
	 * @@param str
	 * @@param c
	 * @@return
	 */
	public static String[] splitStr(String str , char c){
		if(str == null){
			return null;
		}
		str += c;
		int n = 0;
		for(int i = 0 ; i < str.length() ; i++)	{
			if(str.charAt(i) == c){
				n++;
			}
		}
		String out[] = new String[n];
		for(int i = 0 ; i < n ; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0 , index);
			str = str.substring(index + 1 , str.length());
		}
		return out;
	}
}
