package com.jc.csmp.ptProjectZtb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;

import java.math.BigDecimal;
import java.util.Date;

public class WinbiddingVo extends BaseBean {

    private String biddingDljgmc;
    private BigDecimal kcCc;
    private BigDecimal sjCc;
    private BigDecimal sgCc;
    private BigDecimal jlCc;
    private BigDecimal kcsjCc;
    private BigDecimal hwclCc;
    private BigDecimal total;
    private String projectNum;
    private String projectName;
    private String jdType;
    private Date jdTime;
    private Date jdTimeBegin;
    private Date jdTimeEnd;
    private String jdOrg;
    private String jdRemark;


    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getJdType() {
        return jdType;
    }
    public String getJdTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.jdType, this.getJdType());
    }
    public void setJdType(String jdType) {
        this.jdType = jdType;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getJdTime() {
        return jdTime;
    }

    public void setJdTime(Date jdTime) {
        this.jdTime = jdTime;
    }

    public String getJdOrg() {
        return jdOrg;
    }

    public void setJdOrg(String jdOrg) {
        this.jdOrg = jdOrg;
    }

    public String getJdRemark() {
        return jdRemark;
    }

    public void setJdRemark(String jdRemark) {
        this.jdRemark = jdRemark;
    }


    public String getBiddingDljgmc() {
        return biddingDljgmc;
    }

    public void setBiddingDljgmc(String biddingDljgmc) {
        this.biddingDljgmc = biddingDljgmc;
    }

    public BigDecimal getKcCc() {
        return kcCc;
    }

    public void setKcCc(BigDecimal kcCc) {
        this.kcCc = kcCc;
    }

    public BigDecimal getSjCc() {
        return sjCc;
    }

    public void setSjCc(BigDecimal sjCc) {
        this.sjCc = sjCc;
    }

    public BigDecimal getSgCc() {
        return sgCc;
    }

    public void setSgCc(BigDecimal sgCc) {
        this.sgCc = sgCc;
    }

    public BigDecimal getJlCc() {
        return jlCc;
    }

    public void setJlCc(BigDecimal jlCc) {
        this.jlCc = jlCc;
    }

    public BigDecimal getKcsjCc() {
        return kcsjCc;
    }

    public void setKcsjCc(BigDecimal kcsjCc) {
        this.kcsjCc = kcsjCc;
    }

    public BigDecimal getHwclCc() {
        return hwclCc;
    }

    public void setHwclCc(BigDecimal hwclCc) {
        this.hwclCc = hwclCc;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getJdTimeBegin() {
        return jdTimeBegin;
    }

    public void setJdTimeBegin(Date jdTimeBegin) {
        this.jdTimeBegin = jdTimeBegin;
    }

    public Date getJdTimeEnd() {
        return jdTimeEnd;
    }

    public void setJdTimeEnd(Date jdTimeEnd) {
        this.jdTimeEnd = jdTimeEnd;
    }
}
