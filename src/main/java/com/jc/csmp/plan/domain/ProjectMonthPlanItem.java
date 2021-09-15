package com.jc.csmp.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ProjectMonthPlanItem extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ProjectMonthPlanItem() {
	}
	private String headId;
	private String projectType;
	private String projectTypeName;
	private String projectId;
	private String projectName;
	private String dutyCompany;
	private String dutyPerson;
	private String govDutyPerson;
	private String projectDesc;
	private String contractCompany;
	private String contractPerson;
	private Date projectStartDate;
	private Date projectStartDateBegin;
	private Date projectStartDateEnd;
	private Date projectEndDate;
	private Date projectEndDateBegin;
	private Date projectEndDateEnd;
	private BigDecimal projectTotalInvest;
	private BigDecimal projectUsedInvest;
	private BigDecimal projectNowInvest;
	private BigDecimal projectAfterInvest;
	private BigDecimal projectMonthPlanInvest;
	private BigDecimal projectMonthActInvest;
	private Integer projectTotalDay;
	private String xxjd;
	private String xxjdAttchList;
	private String solveProblemType;
	private String solveProblem;
	private String tudiCard;
	private String ydghxkCard;
	private String gcghxkCard;
	private String kgxkCard;
	private String xmxzyjs;
	private String remark;
	private String queryMonth;
	private String[] attachList;

	public Integer getProjectTotalDay() {
		return projectTotalDay;
	}

	public void setProjectTotalDay(Integer projectTotalDay) {
		this.projectTotalDay = projectTotalDay;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getHeadId() {
		return headId;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}
	public String getProjectTypeName() {
		return projectTypeName;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setDutyCompany(String dutyCompany) {
		this.dutyCompany = dutyCompany;
	}
	public String getDutyCompany() {
		return dutyCompany;
	}
	public void setDutyPerson(String dutyPerson) {
		this.dutyPerson = dutyPerson;
	}
	public String getDutyPerson() {
		return dutyPerson;
	}
	public void setGovDutyPerson(String govDutyPerson) {
		this.govDutyPerson = govDutyPerson;
	}
	public String getGovDutyPerson() {
		return govDutyPerson;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setContractCompany(String contractCompany) {
		this.contractCompany = contractCompany;
	}
	public String getContractCompany() {
		return contractCompany;
	}
	public void setContractPerson(String contractPerson) {
		this.contractPerson = contractPerson;
	}
	public String getContractPerson() {
		return contractPerson;
	}
	public void setProjectStartDateBegin(Date projectStartDateBegin) {
		this.projectStartDateBegin = projectStartDateBegin;
	}
	public Date getProjectStartDateBegin() {
		return projectStartDateBegin;
	}
	public void setProjectStartDateEnd(Date projectStartDateEnd) {
		if(projectStartDateEnd == null){
			return;
		}
		this.projectStartDateEnd = DateUtils.fillTime(projectStartDateEnd);
	}
	public Date getProjectStartDateEnd() {
		return projectStartDateEnd;
	}
	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getProjectStartDate() {
		return projectStartDate;
	}
	public void setProjectEndDateBegin(Date projectEndDateBegin) {
		this.projectEndDateBegin = projectEndDateBegin;
	}
	public Date getProjectEndDateBegin() {
		return projectEndDateBegin;
	}
	public void setProjectEndDateEnd(Date projectEndDateEnd) {
		if(projectEndDateEnd == null){
			return;
		}
		this.projectEndDateEnd = DateUtils.fillTime(projectEndDateEnd);
	}
	public Date getProjectEndDateEnd() {
		return projectEndDateEnd;
	}
	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectTotalInvest(BigDecimal projectTotalInvest) {
		this.projectTotalInvest = projectTotalInvest;
	}
	public BigDecimal getProjectTotalInvest() {
		return projectTotalInvest;
	}
	public void setProjectUsedInvest(BigDecimal projectUsedInvest) {
		this.projectUsedInvest = projectUsedInvest;
	}
	public BigDecimal getProjectUsedInvest() {
		return projectUsedInvest;
	}
	public void setProjectNowInvest(BigDecimal projectNowInvest) {
		this.projectNowInvest = projectNowInvest;
	}
	public BigDecimal getProjectNowInvest() {
		return projectNowInvest;
	}
	public void setProjectAfterInvest(BigDecimal projectAfterInvest) {
		this.projectAfterInvest = projectAfterInvest;
	}
	public BigDecimal getProjectAfterInvest() {
		return projectAfterInvest;
	}
	public void setProjectMonthPlanInvest(BigDecimal projectMonthPlanInvest) {
		this.projectMonthPlanInvest = projectMonthPlanInvest;
	}
	public BigDecimal getProjectMonthPlanInvest() {
		return projectMonthPlanInvest;
	}
	public void setProjectMonthActInvest(BigDecimal projectMonthActInvest) {
		this.projectMonthActInvest = projectMonthActInvest;
	}
	public BigDecimal getProjectMonthActInvest() {
		return projectMonthActInvest;
	}
	public void setXxjd(String xxjd) {
		this.xxjd = xxjd;
	}
	public String getXxjd() {
		return xxjd;
	}
	public void setSolveProblem(String solveProblem) {
		this.solveProblem = solveProblem;
	}
	public String getSolveProblem() {
		return solveProblem;
	}
	public void setTudiCard(String tudiCard) {
		this.tudiCard = tudiCard;
	}
	public String getTudiCard() {
		return tudiCard;
	}
	public String getTudiCardValue() {
		if("Y".equalsIgnoreCase(tudiCard)){
			return "是";
		} else {
			return "否";
		}
	}
	public void setYdghxkCard(String ydghxkCard) {
		this.ydghxkCard = ydghxkCard;
	}
	public String getYdghxkCard() {
		return ydghxkCard;
	}
	public String getYdghxkCardValue() {
		if("Y".equalsIgnoreCase(ydghxkCard)){
			return "是";
		} else {
			return "否";
		}
	}
	public void setGcghxkCard(String gcghxkCard) {
		this.gcghxkCard = gcghxkCard;
	}
	public String getGcghxkCard() {
		return gcghxkCard;
	}
	public String getGcghxkCardValue() {
		if("Y".equalsIgnoreCase(gcghxkCard)){
			return "是";
		} else {
			return "否";
		}
	}
	public void setKgxkCard(String kgxkCard) {
		this.kgxkCard = kgxkCard;
	}
	public String getKgxkCard() {
		return kgxkCard;
	}
	public String getKgxkCardValue() {
		if("Y".equalsIgnoreCase(kgxkCard)){
			return "是";
		} else {
			return "否";
		}
	}
	public void setXmxzyjs(String xmxzyjs) {
		this.xmxzyjs = xmxzyjs;
	}
	public String getXmxzyjs() {
		return xmxzyjs;
	}
	public String getXmxzyjsValue() {
		if("Y".equalsIgnoreCase(xmxzyjs)){
			return "是";
		} else {
			return "否";
		}
	}

	public String getXxjdAttchList() {
		return xxjdAttchList;
	}

	public void setXxjdAttchList(String xxjdAttchList) {
		this.xxjdAttchList = xxjdAttchList;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setItemId(String id) {
		this.setId(id);
	}

	public String getItemId() {
		return this.getId();
	}

	public String[] getAttachList() {
		return attachList;
	}

	public void setAttachList(String[] attachList) {
		this.attachList = attachList;
	}

	public String getSolveProblemType() {
		return solveProblemType;
	}

	public void setSolveProblemType(String solveProblemType) {
		this.solveProblemType = solveProblemType;
	}

	public String getQueryMonth() {
		return queryMonth;
	}

	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}
}