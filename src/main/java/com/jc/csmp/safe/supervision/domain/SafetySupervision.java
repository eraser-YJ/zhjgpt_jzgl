package com.jc.csmp.safe.supervision.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.item.domain.ItemClassify;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.external.WorkflowBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lc
 * @version 2020-03-10
 */
public class SafetySupervision extends BaseBean {

    private static final long serialVersionUID = 1L;

    public SafetySupervision() {
    }

    private String piId;
    private String projectId;
    private String projectName;
    private String projectAddress;
    private String buildProperties;
    private String investmentCategory;
    private BigDecimal buildArea;
    private BigDecimal investmentAmount;
    private String projectType;
    private String structureType;
    private Date planStartDate;
    private Date planStartDateBegin;
    private Date planStartDateEnd;
    private Date planEndDate;
    private Date planEndDateBegin;
    private Date planEndDateEnd;
    private List<SafetyUnit> safetyUnitList;
    private String projectNumber;
    /**附件信息*/
    private String attachFile;
    private String deleteAttachFile;

    private String itemCode;

    private  String adviceFileName;
    private  String adviceOldName;
    private BigDecimal isAdvice;

    private String buildUnit;
    private String supervisionUnit;
    private ItemClassify itemClassify;

    private String projectTypeValue;
    private String structureTypeValue;
    private String buildPropertiesValue;


    /*private */

    //待办人查询
    private String curUserId;

    private WorkflowBean workflowBean = new WorkflowBean();

    public void setPiId(String piId) {
        this.piId = piId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setBuildProperties(String buildProperties) {
        this.buildProperties = buildProperties;
    }

    public String getBuildProperties() {
        return buildProperties;
    }

    public String getBuildPropertiesValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.buildProperties, this.getBuildProperties());
    }

    public void setInvestmentCategory(String investmentCategory) {
        this.investmentCategory = investmentCategory;
    }

    public String getInvestmentCategory() {
        return investmentCategory;
    }

    public String getInvestmentCategoryValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.investmentCategory, this.getInvestmentCategory());
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }

    public BigDecimal getBuildArea() {
        return buildArea;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getProjectTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.projectType, this.getProjectType());
    }



    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getStructureType() {
        return structureType;
    }

    public String getStructureTypeValue() {
        return GlobalUtil.getDicValue(DicKeyEnum.structureType, this.getStructureType());
    }


    public void setPlanStartDateBegin(Date planStartDateBegin) {
        this.planStartDateBegin = planStartDateBegin;
    }

    public Date getPlanStartDateBegin() {
        return planStartDateBegin;
    }

    public void setPlanStartDateEnd(Date planStartDateEnd) {
        if (planStartDateEnd == null) {
            return;
        }
        this.planStartDateEnd = DateUtils.fillTime(planStartDateEnd);
    }

    public Date getPlanStartDateEnd() {
        return planStartDateEnd;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanEndDateBegin(Date planEndDateBegin) {
        this.planEndDateBegin = planEndDateBegin;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanEndDateBegin() {
        return planEndDateBegin;
    }

    public void setPlanEndDateEnd(Date planEndDateEnd) {
        if (planEndDateEnd == null) {
            return;
        }
        this.planEndDateEnd = DateUtils.fillTime(planEndDateEnd);
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanEndDateEnd() {
        return planEndDateEnd;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getPlanEndDate() {
        return planEndDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getExtDate1() {
        return extDate1;
    }
    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(String curUserId) {
        this.curUserId = curUserId;
    }

    public List<SafetyUnit> getSafetyUnitList() {
        return safetyUnitList;
    }

    public void setSafetyUnitList(List<SafetyUnit> safetyUnitList) {
        this.safetyUnitList = safetyUnitList;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAdviceFileName() {
        return adviceFileName;
    }

    public void setAdviceFileName(String adviceFileName) {
        this.adviceFileName = adviceFileName;
    }

    public String getAdviceOldName() {
        return adviceOldName;
    }

    public void setAdviceOldName(String adviceOldName) {
        this.adviceOldName = adviceOldName;
    }

    public BigDecimal getIsAdvice() {
        return isAdvice;
    }

    public void setIsAdvice(BigDecimal isAdvice) {
        this.isAdvice = isAdvice;
    }

    public String getBuildUnit() {
        return buildUnit;
    }

    public String getBuildUnitName() throws CustomException {
        if(null!=this.getBuildUnit()){
            Department entity = DeptCacheUtil.getDeptById(this.getBuildUnit());
            if(null!=entity){
                return entity.getName();
            }
            return "";

        }else{
            return "";
        }

    }
    public void setBuildUnit(String buildUnit) {
        this.buildUnit = buildUnit;
    }

    public String getSupervisionUnit() {
        return supervisionUnit;
    }
    public String getSupervisionUnitName() throws CustomException {
        if(null!=this.getSupervisionUnit()) {
            Department entity = DeptCacheUtil.getDeptById(this.getSupervisionUnit());
            if (null != entity) {
                return entity.getName();
            }
            return "";
        }else{
            return "";
        }

    }

    public void setSupervisionUnit(String supervisionUnit) {
        this.supervisionUnit = supervisionUnit;
    }


    public ItemClassify getItemClassify() {
        return itemClassify;
    }

    public void setItemClassify(ItemClassify itemClassify) {
        this.itemClassify = itemClassify;
    }


    public String getPiId() {
        return piId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }
}