package com.jc.csmp.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lc
 * @version 2020-03-10
 */
public class ProjectYearPlanItem extends BaseBean {

    private static final long serialVersionUID = 1L;

    public ProjectYearPlanItem() {
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
    private BigDecimal projectNowInvestSrc1;
    private BigDecimal projectNowInvestSrc2;
    private BigDecimal projectNowInvestSrc3;
    private BigDecimal projectNowInvestSrc4;
    private BigDecimal projectNowInvestSrc5;
    private BigDecimal projectNowInvestSrc6;
    private BigDecimal projectNowInvestSrc7;
    private BigDecimal projectNowInvestSrc8;
    private BigDecimal projectNowInvestSrc9;
    private BigDecimal projectNowInvestSrc10;
    private BigDecimal projectNowInvestSrc11;
    private BigDecimal projectNowInvestSrc12;
    private Integer projectTotalDay;
    private String projectNowDesc;
    private String remark;
    private String tempInfo;
    private Integer queryYear;
    private String queryAreaCode;
    private String emptyProjectId;

    public Integer getQueryYear() {
        return queryYear;
    }

    public void setQueryYear(Integer queryYear) {
        this.queryYear = queryYear;
    }

    public String getEmptyProjectId() {
        return emptyProjectId;
    }

    public void setEmptyProjectId(String emptyProjectId) {
        this.emptyProjectId = emptyProjectId;
    }

    public String getQueryAreaCode() {
        return queryAreaCode;
    }

    public void setQueryAreaCode(String queryAreaCode) {
        this.queryAreaCode = queryAreaCode;
    }

    public Integer getProjectTotalDay() {
        return projectTotalDay;
    }

    public void setProjectTotalDay(Integer projectTotalDay) {
        this.projectTotalDay = projectTotalDay;
    }

    public String getTempInfo() {
        return tempInfo;
    }

    public void setTempInfo(String tempInfo) {
        this.tempInfo = tempInfo;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getHeadId() {
        return headId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
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
        if (projectStartDateEnd == null) {
            return;
        }
        this.projectStartDateEnd = DateUtils.fillTime(projectStartDateEnd);
    }

    public Date getProjectStartDateEnd() {
        return projectStartDateEnd;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

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
        if (projectEndDateEnd == null) {
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

    public BigDecimal getProjectAfterInvest() {
        return projectAfterInvest;
    }

    public void setProjectAfterInvest(BigDecimal projectAfterInvest) {
        this.projectAfterInvest = projectAfterInvest;
    }

    public void setProjectNowInvestSrc1(BigDecimal projectNowInvestSrc1) {
        this.projectNowInvestSrc1 = projectNowInvestSrc1;
    }

    public BigDecimal getProjectNowInvestSrc1() {
        return projectNowInvestSrc1;
    }

    public void setProjectNowInvestSrc2(BigDecimal projectNowInvestSrc2) {
        this.projectNowInvestSrc2 = projectNowInvestSrc2;
    }

    public BigDecimal getProjectNowInvestSrc2() {
        return projectNowInvestSrc2;
    }

    public void setProjectNowInvestSrc3(BigDecimal projectNowInvestSrc3) {
        this.projectNowInvestSrc3 = projectNowInvestSrc3;
    }

    public BigDecimal getProjectNowInvestSrc3() {
        return projectNowInvestSrc3;
    }

    public void setProjectNowInvestSrc4(BigDecimal projectNowInvestSrc4) {
        this.projectNowInvestSrc4 = projectNowInvestSrc4;
    }

    public BigDecimal getProjectNowInvestSrc4() {
        return projectNowInvestSrc4;
    }

    public void setProjectNowInvestSrc5(BigDecimal projectNowInvestSrc5) {
        this.projectNowInvestSrc5 = projectNowInvestSrc5;
    }

    public BigDecimal getProjectNowInvestSrc5() {
        return projectNowInvestSrc5;
    }

    public void setProjectNowInvestSrc6(BigDecimal projectNowInvestSrc6) {
        this.projectNowInvestSrc6 = projectNowInvestSrc6;
    }

    public BigDecimal getProjectNowInvestSrc6() {
        return projectNowInvestSrc6;
    }

    public void setProjectNowInvestSrc7(BigDecimal projectNowInvestSrc7) {
        this.projectNowInvestSrc7 = projectNowInvestSrc7;
    }

    public BigDecimal getProjectNowInvestSrc7() {
        return projectNowInvestSrc7;
    }

    public void setProjectNowInvestSrc8(BigDecimal projectNowInvestSrc8) {
        this.projectNowInvestSrc8 = projectNowInvestSrc8;
    }

    public BigDecimal getProjectNowInvestSrc8() {
        return projectNowInvestSrc8;
    }

    public void setProjectNowInvestSrc9(BigDecimal projectNowInvestSrc9) {
        this.projectNowInvestSrc9 = projectNowInvestSrc9;
    }

    public BigDecimal getProjectNowInvestSrc9() {
        return projectNowInvestSrc9;
    }

    public void setProjectNowInvestSrc10(BigDecimal projectNowInvestSrc10) {
        this.projectNowInvestSrc10 = projectNowInvestSrc10;
    }

    public BigDecimal getProjectNowInvestSrc10() {
        return projectNowInvestSrc10;
    }

    public void setProjectNowInvestSrc11(BigDecimal projectNowInvestSrc11) {
        this.projectNowInvestSrc11 = projectNowInvestSrc11;
    }

    public BigDecimal getProjectNowInvestSrc11() {
        return projectNowInvestSrc11;
    }

    public void setProjectNowInvestSrc12(BigDecimal projectNowInvestSrc12) {
        this.projectNowInvestSrc12 = projectNowInvestSrc12;
    }

    public BigDecimal getProjectNowInvestSrc12() {
        return projectNowInvestSrc12;
    }

    public void setProjectNowDesc(String projectNowDesc) {
        this.projectNowDesc = projectNowDesc;
    }

    public String getProjectNowDesc() {
        return projectNowDesc;
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
}