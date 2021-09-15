package com.jc.csmp.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建设管理-周报事项
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectWeeklyItem extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectWeeklyItem(){}
    /**周报id*/
    private String weeklyId;
    /**计划id*/
    private String planId;
    /**完成比例*/
    private Integer finishRatio;
    /**完成金额*/
    private BigDecimal finishMoney;
    /**备注*/
    private String remark;

    private String planName;
    private String stageName;
    private String planCode;
    private Date planStartDate;
    private Date planEndDate;
    private BigDecimal outputValue;
    private String dutyPerson;

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public BigDecimal getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(BigDecimal outputValue) {
        this.outputValue = outputValue;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getWeeklyId() {
        return weeklyId;
    }

    public void setWeeklyId(String weeklyId) {
        this.weeklyId = weeklyId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Integer getFinishRatio() {
        return finishRatio;
    }

    public void setFinishRatio(Integer finishRatio) {
        this.finishRatio = finishRatio;
    }

    public BigDecimal getFinishMoney() {
        return finishMoney;
    }

    public void setFinishMoney(BigDecimal finishMoney) {
        this.finishMoney = finishMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
}



