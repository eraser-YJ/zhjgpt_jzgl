package com.jc.csmp.project.vo;

import java.math.BigDecimal;

/**
 * @Author 常鹏
 * @Date 2020/8/26 9:02
 * @Version 1.0
 */
public class CmProjectInfoVo {
    private String id;
    private Integer tableRowNo;
    private String projectNumber;
    private String projectName;
    private String regionValue;
    private String projectTypeValue;
    private String buildDeptIdValue;
    private BigDecimal landArea;
    private BigDecimal investmentAmount;
    private String superviseDeptIdValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTableRowNo() {
        return tableRowNo;
    }

    public void setTableRowNo(Integer tableRowNo) {
        this.tableRowNo = tableRowNo;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRegionValue() {
        return regionValue;
    }

    public void setRegionValue(String regionValue) {
        this.regionValue = regionValue;
    }

    public String getProjectTypeValue() {
        return projectTypeValue;
    }

    public void setProjectTypeValue(String projectTypeValue) {
        this.projectTypeValue = projectTypeValue;
    }

    public String getBuildDeptIdValue() {
        return buildDeptIdValue;
    }

    public void setBuildDeptIdValue(String buildDeptIdValue) {
        this.buildDeptIdValue = buildDeptIdValue;
    }

    public BigDecimal getLandArea() {
        return landArea;
    }

    public void setLandArea(BigDecimal landArea) {
        this.landArea = landArea;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getSuperviseDeptIdValue() {
        return superviseDeptIdValue;
    }

    public void setSuperviseDeptIdValue(String superviseDeptIdValue) {
        this.superviseDeptIdValue = superviseDeptIdValue;
    }
}
