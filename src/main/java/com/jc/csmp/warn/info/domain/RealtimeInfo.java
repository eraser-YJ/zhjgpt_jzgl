package com.jc.csmp.warn.info.domain;

import com.jc.csmp.doc.common.MechType;
import com.jc.foundation.domain.BaseBean;

import java.math.BigDecimal;

/**
 * @author lc
 * @version 2020-03-10
 */
public class RealtimeInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public RealtimeInfo() {
    }

    private String projectCode;
    private String projectName;
    private String useCompany;
    private String useCompanyName;
    private String equipmentType;
    private Integer warnNum;
    private Integer normarlNum;
    private Integer outLineNum;

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

    public Integer getWarnNum() {
        return warnNum;
    }

    public void setWarnNum(Integer warnNum) {
        this.warnNum = warnNum;
    }

    public Integer getNormarlNum() {
        return normarlNum;
    }

    public void setNormarlNum(Integer normarlNum) {
        this.normarlNum = normarlNum;
    }

    public Integer getOutLineNum() {
        return outLineNum;
    }

    public void setOutLineNum(Integer outLineNum) {
        this.outLineNum = outLineNum;
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
}