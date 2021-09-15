package com.jc.common.kit.vo;

import java.io.Serializable;

/**
 * 月份信息
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class Month implements Serializable {

	// 显示月历的开始时间 yyyy-MM-dd
	private String disStartDate;
	// 显示月历的结束时间 yyyy-MM-dd
	private String disEndDate;
	// 显示月历相差天数
	private Long diffDays;
	// 实际月份的开始时间
	private String startDate;
	// 实际月份的结束时间
	private String endDate;

	/**
	 * 取得显示月历的开始时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getDisStartDate() {
		return disStartDate;
	}

	/**
	 * 设定显示月历的开始时间 yyyy-MM-dd
	 * 
	 * @param disStartDate yyyy-MM-dd
	 */
	public void setDisStartDate(String disStartDate) {
		this.disStartDate = disStartDate;
	}

	/**
	 * 取得显示月历的结束时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getDisEndDate() {
		return disEndDate;
	}

	/**
	 * 设定显示月历的结束时间 yyyy-MM-dd
	 * 
	 * @param disEndDate yyyy-MM-dd
	 */
	public void setDisEndDate(String disEndDate) {
		this.disEndDate = disEndDate;
	}

	/**
	 * 取得显示月历相差天数
	 * 
	 * @return 天数
	 */
	public Long getDiffDays() {
		return diffDays;
	}

	/**
	 * 设定显示月历相差天数
	 * 
	 * @param diffDays 天数
	 */
	public void setDiffDays(Long diffDays) {
		this.diffDays = diffDays;
	}

	/**
	 * 取得实际月份的开始时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 设定实际月份的开始时间 yyyy-MM-dd
	 * 
	 * @param startDate yyyy-MM-dd
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 取得实际月份的结束时间 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 设定实际月份的结束时间 yyyy-MM-dd
	 * 
	 * @param endDate yyyy-MM-dd
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
