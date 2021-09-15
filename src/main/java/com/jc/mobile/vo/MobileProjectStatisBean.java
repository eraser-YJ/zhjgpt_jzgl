package com.jc.mobile.vo;

import java.math.BigDecimal;

/**
 * 移动端项目统计bean
 * @Author 常鹏
 * @Date 2020/8/3 9:49
 * @Version 1.0
 */
public class MobileProjectStatisBean {
    /**项目总数*/
    private Long projectCount;
    /**企业总数*/
    private Long companyCount;
    /**从业人员*/
    private Long personCount;
    /**投资总额*/
    private BigDecimal projectTotalInvestment;
    /**产值总额*/
    private BigDecimal projectOutputMoney;
    /**预警信息*/
    private Long warningCount;

    public Long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Long projectCount) {
        this.projectCount = projectCount;
    }

    public Long getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(Long companyCount) {
        this.companyCount = companyCount;
    }

    public Long getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Long personCount) {
        this.personCount = personCount;
    }

    public BigDecimal getProjectTotalInvestment() {
        return projectTotalInvestment;
    }

    public void setProjectTotalInvestment(BigDecimal projectTotalInvestment) {
        this.projectTotalInvestment = projectTotalInvestment;
    }

    public BigDecimal getProjectOutputMoney() {
        return projectOutputMoney;
    }

    public void setProjectOutputMoney(BigDecimal projectOutputMoney) {
        this.projectOutputMoney = projectOutputMoney;
    }

    public Long getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Long warningCount) {
        this.warningCount = warningCount;
    }
}
