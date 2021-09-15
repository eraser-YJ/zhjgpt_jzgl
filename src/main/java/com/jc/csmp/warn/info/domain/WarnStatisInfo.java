package com.jc.csmp.warn.info.domain;

import com.jc.csmp.doc.common.MechType;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lc
 * @version 2020-03-10
 */
public class WarnStatisInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public WarnStatisInfo() {
    }

    private String projectCode;
    private String equipmentCode;
    private String projectName;
    private String useCompany;
    private String useCompanyName;
    private String equipmentType;
    private BigDecimal processedNum;
    private BigDecimal initNum;
    private Integer warnNum;
    private Date warnTimeBegin;
    private Date warnTimeEnd;
    private String[] equipmentCodes;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUseCompany() {
        return useCompany;
    }

    public void setUseCompany(String useCompany) {
        this.useCompany = useCompany;
    }

    public String getUseCompanyName() {
        return useCompanyName;
    }

    public void setUseCompanyName(String useCompanyName) {
        this.useCompanyName = useCompanyName;
    }


    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentTypeValue() {
        return MechType.getByCode(equipmentType).getDisName();
    }

    public BigDecimal getProcessedNum() {
        return processedNum;
    }

    public void setProcessedNum(BigDecimal processedNum) {
        this.processedNum = processedNum;
    }

    public BigDecimal getInitNum() {
        return initNum;
    }

    public void setInitNum(BigDecimal initNum) {
        this.initNum = initNum;
    }


    public Date getWarnTimeBegin() {
        return warnTimeBegin;
    }

    public void setWarnTimeBegin(Date warnTimeBegin) {
        this.warnTimeBegin = warnTimeBegin;
    }

    public Date getWarnTimeEnd() {
        return warnTimeEnd;
    }

    public void setWarnTimeEnd(Date warnTimeEnd) {
        if (warnTimeEnd == null) {
            return;
        }
        this.warnTimeEnd = DateUtils.fillTime(warnTimeEnd);
    }

    public String[] getEquipmentCodes() {
        return equipmentCodes;
    }

    public void setEquipmentCodes(String[] equipmentCodes) {
        this.equipmentCodes = equipmentCodes;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Integer getWarnNum() {
        return warnNum;
    }

    public void setWarnNum(Integer warnNum) {
        this.warnNum = warnNum;
    }
}