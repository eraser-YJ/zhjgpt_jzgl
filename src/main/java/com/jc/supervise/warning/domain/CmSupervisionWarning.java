package com.jc.supervise.warning.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;
import com.jc.supervise.warning.enums.SupervisionWarningStatusEnums;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;

import java.util.Date;

/**
 * 建设管理-监管预警
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmSupervisionWarning extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmSupervisionWarning(){}
    /**所属项目*/
    private String projectId;
    /**监测点*/
    private String supervisionPointId;
    /**处理结果*/
    private String disploseResult;
    /**处理人*/
    private String disploseUserId;
    /**处理时间*/
    private Date disploseDate;
    /**状态(no:未处理 reopen:重新打开, finish:结束)*/
    private String status;
    /**监测时间*/
    private Date supervisionDate;
    /**监管单位ID*/
    private String supervisionDeptId;
    /**监管单位编码*/
    private String supervisionDeptCode;
    /**建设单位ID*/
    private String buildDeptId;
    /**建设单位编码*/
    private String buildDeptCode;
    /**检测点名*/
    private String supervisionPointName;
    /**预警内容*/
    private String warningContent;

    /**** param ****/
    private String deptCondition;
    private String[] statusArray;
    private String projectNumber;
    private String projectName;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSupervisionPointId() {
        return supervisionPointId;
    }

    public void setSupervisionPointId(String supervisionPointId) {
        this.supervisionPointId = supervisionPointId;
    }

    public String getDisploseResult() {
        return disploseResult;
    }

    public void setDisploseResult(String disploseResult) {
        this.disploseResult = disploseResult;
    }

    public String getDisploseUserId() {
        return disploseUserId;
    }

    public void setDisploseUserId(String disploseUserId) {
        this.disploseUserId = disploseUserId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getDisploseDate() {
        return disploseDate;
    }

    public void setDisploseDate(Date disploseDate) {
        this.disploseDate = disploseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getSupervisionDate() {
        return supervisionDate;
    }

    public void setSupervisionDate(Date supervisionDate) {
        this.supervisionDate = supervisionDate;
    }

    public String getSupervisionPointName() {
        return supervisionPointName;
    }

    public void setSupervisionPointName(String supervisionPointName) {
        this.supervisionPointName = supervisionPointName;
    }

    public String getSupervisionDeptId() {
        return supervisionDeptId;
    }

    public void setSupervisionDeptId(String supervisionDeptId) {
        this.supervisionDeptId = supervisionDeptId;
    }

    public String getSupervisionDeptCode() {
        return supervisionDeptCode;
    }

    public void setSupervisionDeptCode(String supervisionDeptCode) {
        this.supervisionDeptCode = supervisionDeptCode;
    }

    public String getBuildDeptId() {
        return buildDeptId;
    }

    public void setBuildDeptId(String buildDeptId) {
        this.buildDeptId = buildDeptId;
    }

    public String getBuildDeptCode() {
        return buildDeptCode;
    }

    public void setBuildDeptCode(String buildDeptCode) {
        this.buildDeptCode = buildDeptCode;
    }

    public String getStatusValue() {
        return SupervisionWarningStatusEnums.getByCode(this.getStatus()).getValue();
    }

    public String getDisploseUserName() {
        if (StringUtil.isEmpty(this.getDisploseUserId())) {
            return "";
        }
        User user = UserUtils.getUser(this.getDisploseUserId());
        if (user != null) {
            return user.getDisplayName();
        }
        return "";
    }

    public String getDeptCondition() {
        return deptCondition;
    }

    public void setDeptCondition(String deptCondition) {
        this.deptCondition = deptCondition;
    }

    public String[] getStatusArray() {
        return statusArray;
    }

    public void setStatusArray(String[] statusArray) {
        this.statusArray = statusArray;
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

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }
}



