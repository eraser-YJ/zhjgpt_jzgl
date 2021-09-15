package com.jc.csmp.plan.domain;

public class StatisVO {
    private Integer planYear;
    private String projectType;
    private String projectTypeEx;
    private String planAreaCode;
    public String getProjectTypeEx() {
        return projectTypeEx;
    }

    public void setProjectTypeEx(String projectTypeEx) {
        this.projectTypeEx = projectTypeEx;
    }
    public Integer getPlanYear() {
        return planYear;
    }

    public Integer getPlanLastYear() {
        return planYear - 1;
    }

    public void setPlanYear(Integer planYear) {
        this.planYear = planYear;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getPlanAreaCode() {
        return planAreaCode;
    }

    public void setPlanAreaCode(String planAreaCode) {
        this.planAreaCode = planAreaCode;
    }
}
