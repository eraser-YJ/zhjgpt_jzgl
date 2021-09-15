package com.jc.common.kit.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 天信息
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class Day implements Serializable {

	// 星期字符串
	private static final String[] WEEKDAY_STR = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	// 当天日期
	private Date date;

	// 1(星期一)，2，3，4，5，6（星期六），0（星期天）
	private Integer dayOfWeek;

	/**
	 * 取得当天日期
	 * 
	 * @return 日期
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 设定当天日期
	 * 
	 * @param date 日期
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * XXXX-XX-XX 格式日期
	 * 
	 * @return
	 */
	public String getDateStr() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(date);
	}

	/**
	 * XXXX年XX月XX日 格式日期
	 * 
	 * @return
	 */
	public String getDateStr1() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
		return f.format(date);
	}

	/**
	 * 取得天在周的数值（1，2...6）
	 * 
	 * @return 数值
	 */
	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * 设定天在周的数值 （1，2...6）
	 * 
	 * @param dayOfWeek 数值
	 */
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * 取得天在周的汉字名称
	 * 
	 * @return
	 */
	public String getWeekday() {
		return WEEKDAY_STR[dayOfWeek];
	}

	/**
	 * 是否是周末
	 * 
	 * @return
	 */
	public boolean getWeekend() {
		return dayOfWeek == 0 || dayOfWeek == 6;
	}
}
