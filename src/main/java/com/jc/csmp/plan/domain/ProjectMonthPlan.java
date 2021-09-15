package com.jc.csmp.plan.domain;

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
public class ProjectMonthPlan extends BaseBean {

    private static final long serialVersionUID = 1L;

    public ProjectMonthPlan() {
    }

    private String planType;
    private Integer planYear;
    private Integer planMonth;
    private String planName;
    private String planAreaCode;
    private String planAreaName;
    private String itemChange;
    private String itemListContent;
    private String remark;
    private String roleCode;
    private String canChange = "N";
    private String ower;
    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanYear(Integer planYear) {
        this.planYear = planYear;
    }

    public Integer getPlanYear() {
        return planYear;
    }

    public void setPlanMonth(Integer planMonth) {
        this.planMonth = planMonth;
    }

    public Integer getPlanMonth() {
        return planMonth;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanAreaCode(String planAreaCode) {
        this.planAreaCode = planAreaCode;
    }

    public String getPlanAreaCode() {
        return planAreaCode;
    }

    public void setPlanAreaName(String planAreaName) {
        this.planAreaName = planAreaName;
    }

    public String getPlanAreaName() {
        return planAreaName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public String getItemChange() {
        return itemChange;
    }

    public void setItemChange(String itemChange) {
        this.itemChange = itemChange;
    }

    public String getItemListContent() {
        return itemListContent;
    }

    public void setItemListContent(String itemListContent) {
        this.itemListContent = itemListContent;
    }

    public String getCanChange() {
        return canChange;
    }

    public void setCanChange(String canChange) {
        this.canChange = canChange;
    }

    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}