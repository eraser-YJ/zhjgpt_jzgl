package com.jc.common.kit;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数量的相关功能
 * 
 * @author lc  liubq
 */
public class NumUtil {

	/**
	 * 四舍五入到指定位数
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static BigDecimal round(BigDecimal d, int newScale) {
		if (newScale < 0) {
			newScale = 0;
		}
		if (newScale > 8) {
			newScale = 8;
		}
		return d.setScale(newScale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 舍五入到指定位数
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static double round(double d, int newScale) {
		if (newScale < 0) {
			newScale = 0;
		}
		if (newScale > 8) {
			newScale = 8;
		}
		BigDecimal b = new BigDecimal(d);
		double nv = b.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return nv;
	}

	/**
	 * 舍五入到指定位数
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static String roundToString(double d, int newScale) {
		if (newScale < 0) {
			newScale = 0;
		}
		if (newScale > 8) {
			newScale = 8;
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(newScale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 格式化到指定位数
	 * 没有采用四舍五入方式
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static String format(double d, int newScale) {
		if (newScale < 0) {
			newScale = 0;
		}
		if (newScale > 8) {
			newScale = 8;
		}
		StringBuilder f = new StringBuilder("#");
		for (int i = 0; i < newScale; i++) {
			if (i == 0) {
				f.append(".");
			}
			f.append("0");
		}
		String value = new DecimalFormat(f.toString()).format(d);
		if (value.startsWith(".")) {
			value = "0" + value;
		}
		return value;
	}

	/**
	 * 格式化到指定位数
	 * 采用四舍五入方式
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static String format2(double d, int newScale) {
		return format(round(d, newScale), newScale);
	}
}
