package com.jc.supervise.basic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.bean.ResourceOperatorActionEnum;
import com.jc.resource.bean.ResourceOperatorTypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 立项bean
 * @Author 常鹏
 * @Date 2020/7/22 9:54
 * @Version 1.0
 */
public class SuperviseBasicProjectApproval extends BaseBean {
    private static final long serialVersionUID = 1L;
    public SuperviseBasicProjectApproval(){}
    /**序号*/
    private Integer no;
    /**项目编码*/
    private String projectNumber;
    /**项目名称*/
    private String projectName;
    /**立项时间*/
    private Date approvalDate;
    private Date approvalDateBegin;
    private Date approvalDateEnd;
    /**建设单位*/
    private String buildDept;
    /**投资总额*/
    private BigDecimal projectMoney;
    /**所属辖区*/
    private String region;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getBuildDept() {
        return buildDept;
    }

    public void setBuildDept(String buildDept) {
        this.buildDept = buildDept;
    }

    public BigDecimal getProjectMoney() {
        return projectMoney;
    }

    public void setProjectMoney(BigDecimal projectMoney) {
        this.projectMoney = projectMoney;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getApprovalDateBegin() {
        return approvalDateBegin;
    }

    public void setApprovalDateBegin(Date approvalDateBegin) {
        this.approvalDateBegin = approvalDateBegin;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getApprovalDateEnd() {
        return approvalDateEnd;
    }

    public void setApprovalDateEnd(Date approvalDateEnd) {
        if (this.approvalDateEnd != null) {
            this.approvalDateEnd = DateUtils.fillTime(approvalDateEnd);
        }
    }
}
