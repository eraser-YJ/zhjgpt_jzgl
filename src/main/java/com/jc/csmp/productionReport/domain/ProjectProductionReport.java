package com.jc.csmp.productionReport.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class ProjectProductionReport extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ProjectProductionReport() {
	}
	private String projectNumber;
	private String projectName;
	private String yearMon;
	private BigDecimal completedInvestmentAmount;
	private BigDecimal plannedInvestmentAmount;
	private BigDecimal completedInvestmentTotal;

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setYearMon(String yearMon) {
		this.yearMon = yearMon;
	}
	public String getYearMon() {
		return yearMon;
	}
	public void setCompletedInvestmentAmount(BigDecimal completedInvestmentAmount) {
		this.completedInvestmentAmount = completedInvestmentAmount;
	}
	public BigDecimal getCompletedInvestmentAmount() {
		return completedInvestmentAmount;
	}
	public void setPlannedInvestmentAmount(BigDecimal plannedInvestmentAmount) {
		this.plannedInvestmentAmount = plannedInvestmentAmount;
	}
	public BigDecimal getPlannedInvestmentAmount() {
		return plannedInvestmentAmount;
	}
	public void setCompletedInvestmentTotal(BigDecimal completedInvestmentTotal) {
		this.completedInvestmentTotal = completedInvestmentTotal;
	}
	public BigDecimal getCompletedInvestmentTotal() {
		return completedInvestmentTotal;
	}
}