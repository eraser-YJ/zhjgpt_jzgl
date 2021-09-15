package com.jc.common.db;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlDateUtil {
	public static String getDateStr(Object obj) {
		try {
			if (obj == null || obj.toString().trim().length() == 0) {

				return null;
			}
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (obj instanceof Date) {
				return f.format((Date) obj);
			}
			if (obj instanceof Long) {
				return f.format(new Date((Long) obj));
			}
			String dateStr = obj.toString().trim();
			dateStr = dateStr.replaceAll("/", "-");
			if (dateStr.length() == 10) {
				SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
				return f.format(f1.parse(dateStr));
			}
			return dateStr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public static String getBeginDateStr(Object obj) {
		try {
			if (obj == null || obj.toString().trim().length() == 0) {
				return null;
			}
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			if (obj instanceof Date) {
				return f1.format((Date) obj) + " 00:00:00";
			}
			if (obj instanceof Long) {
				return f1.format(new Date((Long) obj)) + " 00:00:00";
			}
			String dateStr = obj.toString().trim();
			dateStr = dateStr.replaceAll("/", "-");
			if (dateStr.length() == 10) {
				return dateStr + " 00:00:00";
			}
			return dateStr.substring(0, 10) + " 00:00:00";
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String getEndDateStr(Object obj) {
		try {
			if (obj == null || obj.toString().trim().length() == 0) {
				return null;
			}
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			if (obj instanceof Date) {
				return f1.format((Date) obj) + " 23:59:59";
			}
			if (obj instanceof Long) {
				return f1.format(new Date((Long) obj)) + " 23:59:59";
			}
			String dateStr = obj.toString().trim();
			dateStr = dateStr.replaceAll("/", "-");
			if (dateStr.length() == 10) {
				return dateStr + " 23:59:59";
			}
			return dateStr.substring(0, 10) + " 23:59:59";
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
