package com.jc.csmp.equipment.info.domain;

import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.doc.common.MechType;
import com.jc.foundation.domain.BaseBean;

/**
 * @author lc
 * @version 2020-03-10
 */
public class EquipmentInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public EquipmentInfo() {
    }

    private String projectCode;
    private String projectName;
    private String useCompanyCode;
    private String useCompanyName;
    private String equipmentType;
    private String equipmentCode;
    private String equipmentName;
    private String videoCode;
    private String workPosition;
    private String workArea;
    private String workSpace;
    private String warnInfo;
    private String driver1;
    private String driver1Mobile;
    private String driver2;
    private String driver2Mobile;
    private String signalman;
    private String signalmanMobile;
    private String remark;
    //正常，报警，离线
    private String workStatus;
    //0  数据未关联使用，1 数据已经关联使用
    private String dataStatus;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //纬度
    private Integer warnNum = 0;

    private String[] equipmentCodes;

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setUseCompanyCode(String useCompany) {
        this.useCompanyCode = useCompany;
    }

    public String getUseCompanyCode() {
        return useCompanyCode;
    }

    public void setUseCompanyName(String useCompanyName) {
        this.useCompanyName = useCompanyName;
    }

    public String getUseCompanyName() {
        return useCompanyName;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentTypeValue() {
        return MechType.getByCode(equipmentType).getDisName();
    }


    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }

    public void setWarnInfo(String warnInfo) {
        this.warnInfo = warnInfo;
    }

    public String getWarnInfo() {
        return warnInfo;
    }

    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }

    public String getDriver1() {
        return driver1;
    }

    public void setDriver1Mobile(String driver1Mobile) {
        this.driver1Mobile = driver1Mobile;
    }

    public String getDriver1Mobile() {
        return driver1Mobile;
    }

    public void setDriver2(String driver2) {
        this.driver2 = driver2;
    }

    public String getDriver2() {
        return driver2;
    }

    public void setDriver2Mobile(String driver2Mobile) {
        this.driver2Mobile = driver2Mobile;
    }

    public String getDriver2Mobile() {
        return driver2Mobile;
    }

    public void setSignalman(String signalman) {
        this.signalman = signalman;
    }

    public String getSignalman() {
        return signalman;
    }

    public void setSignalmanMobile(String signalmanMobile) {
        this.signalmanMobile = signalmanMobile;
    }

    public String getSignalmanMobile() {
        return signalmanMobile;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public String getWorkStatusValue() {
        return EquiWorkStatusEnum.getByCode(this.workStatus).getDisName();
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
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

    public String[] getEquipmentCodes() {
        return equipmentCodes;
    }

    public void setEquipmentCodes(String[] equipmentCodes) {
        this.equipmentCodes = equipmentCodes;
    }


    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getWarnNum() {
        return warnNum;
    }

    public void setWarnNum(Integer warnNum) {
        this.warnNum = warnNum;
    }
}