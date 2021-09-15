package com.jc.resource.vo;

import com.jc.foundation.domain.BaseBean;

import java.util.Date;

/**
 * 资源项目信息
 * @Author 常鹏
 * @Date 2020/7/29 13:05
 * @Version 1.0
 */
public class ResourceProjectInfo extends BaseBean {
    private String projectNumber;
    private String projectName;
    private String projectAddress;
    private String buildDept;
    private String projectType;
    private String buildArea;
    private String projectMoney;
    private Date realStartDate;
    private Date realFinishDate;

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

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getBuildDept() {
        return buildDept;
    }

    public void setBuildDept(String buildDept) {
        this.buildDept = buildDept;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(String buildArea) {
        this.buildArea = buildArea;
    }

    public String getProjectMoney() {
        return projectMoney;
    }

    public void setProjectMoney(String projectMoney) {
        this.projectMoney = projectMoney;
    }

    public Date getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    public Date getRealFinishDate() {
        return realFinishDate;
    }

    public void setRealFinishDate(Date realFinishDate) {
        this.realFinishDate = realFinishDate;
    }
}
