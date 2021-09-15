package com.jc.csmp.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.util.CmCustomDicCacheUtil;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建设管理-项目管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectInfo extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectInfo(){}
    /**项目编号*/
    private String projectNumber;
    /**项目名称*/
    private String projectName;
    /**项目地址*/
    private String projectAddress;
    /**国标行业(字典)*/
    private String projectTrade;
    /**拟投金额*/
    private BigDecimal planAmount;
    /**建筑面积*/
    private BigDecimal buildArea;
    /**用地性质(字典)*/
    private String landNature;
    /**用地面积*/
    private BigDecimal landArea;
    /**投资金额*/
    private BigDecimal investmentAmount;
    /**拟开工时间*/
    private Date planStartDate;
    /**拟建成时间*/
    private Date planEndDate;
    /**建设规模及内容*/
    private String projectContent;
    /**备注*/
    private String remark;
    /**行政区*/
    private String region;
    /**监管单位*/
    private String superviseDeptId;
    /**建设单位*/
    private String buildDeptId;
    /**新增的附件*/
    private String attachFile;
    /**要删除的附件*/
    private String deleteAttachFile;
    /**监管单位负责人*/
    private String superviseDeptPersion;
    /**监管单位负电话*/
    private  String superviseDeptPhone;
    /**建设单位负责人*/
    private String buildDeptPersion;
    /**建设单位负责人电话*/
    private String buildDeptPhone;
    /**立项日期*/
    private Date projectApprovalDate;
    /**立项编号*/
    private String approvalNumber;
    /**项目类型*/
    private String projectType;
    /**建设类型*/
    private String buildType;
    /**是否竣工*/
    private String ifFinish;
    //产值
    private BigDecimal productionTotal;
    //经度
    private String lng;
    //纬度
    private String lat;
    /**项目资源ID*/
    private String resourceDataId;

    /**机构查询条件，数据权限*/
    private String[] deptIdArray;
    /**数据权限，code查询*/
    private String deptCodeCondition;

    private String buildDeptIdValue;
    private String buildDeptIdLeaderId;
    private String superviseDeptIdValue;
    private String superviseDeptIdLeaderId;

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

    public String getProjectTrade() {
        return projectTrade;
    }

    public void setProjectTrade(String projectTrade) {
        this.projectTrade = projectTrade;
    }

    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public BigDecimal getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }

    public String getLandNature() {
        return landNature;
    }

    public void setLandNature(String landNature) {
        this.landNature = landNature;
    }

    public BigDecimal getLandArea() {
        return landArea;
    }

    public void setLandArea(BigDecimal landArea) {
        this.landArea = landArea;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
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

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProjectTradeValue() {
        CmCustomDic dic = CmCustomDicCacheUtil.getById(this.getProjectTrade());
        return dic == null ? "" : dic.getName();
    }

    public String getLandNatureValue() {
        CmCustomDic dic = CmCustomDicCacheUtil.getById(this.getLandNature());
        return dic == null ? "" : dic.getName();
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }

    public String getDeleteAttachFile() {
        return deleteAttachFile;
    }

    public void setDeleteAttachFile(String deleteAttachFile) {
        this.deleteAttachFile = deleteAttachFile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.region, this.getRegion());
    }

    public String[] getDeptIdArray() {
        return deptIdArray;
    }

    public void setDeptIdArray(String[] deptIdArray) {
        this.deptIdArray = deptIdArray;
    }

    public String getSuperviseDeptId() {
        return superviseDeptId;
    }

    public void setSuperviseDeptId(String superviseDeptId) {
        this.superviseDeptId = superviseDeptId;
        try {
            Department entity = DeptCacheUtil.getDeptById(superviseDeptId);
            if (entity != null) {
                this.setSuperviseDeptIdValue(entity.getName());
                this.setSuperviseDeptIdLeaderId(entity.getLeaderId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getBuildDeptId() {
        return buildDeptId;
    }

    public void setBuildDeptId(String buildDeptId) {
        this.buildDeptId = buildDeptId;
        try {
            Department entity = DeptCacheUtil.getDeptById(this.getBuildDeptId());
            if (entity != null) {
                this.setBuildDeptIdValue(entity.getName());
                this.setBuildDeptIdLeaderId(entity.getLeaderId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSuperviseDeptIdValue(String superviseDeptIdValue) {
        this.superviseDeptIdValue = superviseDeptIdValue;
    }

    public String getSuperviseDeptIdValue() {
        return superviseDeptIdValue;
    }

    public String getSuperviseDeptIdLeaderId() {
        return superviseDeptIdLeaderId;
    }

    public void setSuperviseDeptIdLeaderId(String superviseDeptIdLeaderId) {
        this.superviseDeptIdLeaderId = superviseDeptIdLeaderId;
    }

    public void setBuildDeptIdValue(String buildDeptIdValue) {
        this.buildDeptIdValue = buildDeptIdValue;
    }

    public String getBuildDeptIdLeaderId() {
        return buildDeptIdLeaderId;
    }

    public void setBuildDeptIdLeaderId(String buildDeptIdLeaderId) {
        this.buildDeptIdLeaderId = buildDeptIdLeaderId;
    }

    public String getBuildDeptIdValue() {
        return buildDeptIdValue;
    }

    public String getDeptCodeCondition() {
        return deptCodeCondition;
    }

    public void setDeptCodeCondition(String deptCodeCondition) {
        this.deptCodeCondition = deptCodeCondition;
    }

    public String getSuperviseDeptPersion() {
        String  superviseDeptPersion  =null;
        if (getSuperviseDeptId()!=null){
            String SuperviseLeader = DeptCacheUtil.getLeaderById(getSuperviseDeptId());
            if(SuperviseLeader!=null){
                User SuperviseUser = UserUtils.getUser(SuperviseLeader);
                if(SuperviseUser!=null){
                    superviseDeptPersion=SuperviseUser.getDisplayName();
                }
            }
        }
        return superviseDeptPersion;
    }

    public void setSuperviseDeptPersion(String superviseDeptPersion) {
        this.superviseDeptPersion = superviseDeptPersion;
    }

    public String getSuperviseDeptPhone() {
        return superviseDeptPhone;
    }

    public void setSuperviseDeptPhone(String superviseDeptPhone) {
        this.superviseDeptPhone = superviseDeptPhone;
    }

    public String getBuildDeptPersion() {
        String buildDeptPersion = null;
        if (getBuildDeptId()!=null){
            String bulidLeader = DeptCacheUtil.getLeaderById(getBuildDeptId());
            if(bulidLeader!=null){
                User buildUser = UserUtils.getUser(bulidLeader);
                if(buildUser!=null){
                    buildDeptPersion=buildUser.getDisplayName();
                }
            }
        }
        return buildDeptPersion;
    }

    public String getBuildDeptPersionMobile() {
        String buildDeptPersion = null;
        if (getBuildDeptId()!=null){
            String bulidLeader = DeptCacheUtil.getLeaderById(getBuildDeptId());
            if(bulidLeader!=null){
                User buildUser = UserUtils.getUser(bulidLeader);
                if(buildUser!=null){
                    buildDeptPersion = buildUser.getMobile();
                }
            }
        }
        return buildDeptPersion;
    }

    public void setBuildDeptPersion(String buildDeptPersion) {
        this.buildDeptPersion = buildDeptPersion;
    }

    public String getBuildDeptPhone() {
        return buildDeptPhone;
    }

    public void setBuildDeptPhone(String buildDeptPhone) {
        this.buildDeptPhone = buildDeptPhone;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getProjectApprovalDate() {
        return projectApprovalDate;
    }

    public void setProjectApprovalDate(Date projectApprovalDate) {
        this.projectApprovalDate = projectApprovalDate;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getProjectTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.projectType, this.getProjectType());
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getBuildType() {
        return buildType;
    }

    public String getBuildTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.buildType, this.getBuildType());
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getIfFinish() {
        return ifFinish;
    }

    public void setIfFinish(String ifFinish) {
        this.ifFinish = ifFinish;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public BigDecimal getProductionTotal() {
        return productionTotal;
    }

    public void setProductionTotal(BigDecimal productionTotal) {
        this.productionTotal = productionTotal;
    }

    public String getResourceDataId() {
        return resourceDataId;
    }

    public void setResourceDataId(String resourceDataId) {
        this.resourceDataId = resourceDataId;
    }
}



