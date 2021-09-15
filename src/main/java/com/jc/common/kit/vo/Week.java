package com.jc.common.kit.vo;

import java.io.Serializable;

/**
 * 周信息
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class Week implements Serializable {

	// 实际周的开始时间 yyyy-MM-dd
	private String startDate;
	// 实际周的结束时间 yyyy-MM-dd
	private String endDate;

	/**
	 * 取得实际周的开始时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 设定实际周的开始时间 yyyy-MM-dd
	 * 
	 * @param startDate yyyy-MM-dd
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 取得实际周的结束时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 设定实际周的结束时间 yyyy-MM-dd
	 * 
	 * @param endDate yyyy-MM-dd
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
