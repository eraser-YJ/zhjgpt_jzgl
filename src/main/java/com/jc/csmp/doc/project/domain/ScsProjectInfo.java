package com.jc.csmp.doc.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class ScsProjectInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public ScsProjectInfo() {
    }

    //项目编号
    private String projectNumber;
    //项目名称
    private String projectName;
    //项目地址
    private String projectAddress;
    //项目类型
    private String projectType;
    //建设单位
    private String buildDeptId;
    //建设单位
    private String buildDept;
    //建筑面积
    private BigDecimal buildArea;
    //投资金额
    private BigDecimal projectMoney;
    //实际开工日期
    private Date realStartDate;
    //实际竣工日期
    private Date realFinishDate;
    //经度
    private String longitude;
    //纬度
    private String latitude;

    //buildType
    private String buildType;
    //类型
    private String projectCate;
    //can
    private String canPosition;

    //有视频设备
    private String hasMonitor;


    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getBuildDeptId() {
        return buildDeptId;
    }

    public void setBuildDeptId(String buildDeptId) {
        this.buildDeptId = buildDeptId;
    }

    public String getBuildDept() {
        return buildDept;
    }

    public void setBuildDept(String buildDept) {
        this.buildDept = buildDept;
    }


    public String getProjectTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.projectType, this.getProjectType());
    }

    public BigDecimal getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }

    public BigDecimal getProjectMoney() {
        return projectMoney;
    }

    public void setProjectMoney(BigDecimal projectMoney) {
        this.projectMoney = projectMoney;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    public Date getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    public Date getRealFinishDate() {
        return realFinishDate;
    }

    public void setRealFinishDate(Date realFinishDate) {
        this.realFinishDate = realFinishDate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCanPosition() {
        return canPosition;
    }

    public void setCanPosition(String canPosition) {
        this.canPosition = canPosition;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getProjectCate() {
        return projectCate;
    }

    public void setProjectCate(String projectCate) {
        this.projectCate = projectCate;
    }

    public String getHasMonitor() {
        return hasMonitor;
    }

    public void setHasMonitor(String hasMonitor) {
        this.hasMonitor = hasMonitor;
    }
}