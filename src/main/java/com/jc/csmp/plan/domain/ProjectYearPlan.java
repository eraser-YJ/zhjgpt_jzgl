package com.jc.csmp.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.plan.kit.PlanStatusUtil;
import com.jc.csmp.plan.kit.ReqType;
import com.jc.foundation.domain.BaseBean;
import com.jc.workflow.external.WorkflowBean;

import java.util.Date;

/**
 * @author
 * @version 2020-07-09
 * @title 临时项目
 */

public class ProjectYearPlan extends BaseBean {

    private static final long serialVersionUID = 1L;

    public ProjectYearPlan() {
    }

    private String piId;
    private Integer planYear;
    private Integer planSeqno;
    private String planSeqnoeqnoname;
    private Integer planStatus;
    private String planType;
    private String planName;
    private String planAreaCode;
    private String planAreaName;
    private String itemListContent;
    private String remark;
    private String roleCode;
    private String canChange = "N";
    private String ower;
    private String itemChange;

    private Date chanageDateBegin;
    private Date chanageDateEnd;
    //待办人查询
    private String curUserId;
    //查询
    private Integer queryCanChangeSeqNo;
    private String[] specialIds;
    private String instanceId;
    private String definitionId;
    private String canSearch;

    private WorkflowBean workflowBean = new WorkflowBean();

    public void setPiId(String piId) {
        this.piId = piId;
    }

    public String getPiId() {
        return piId;
    }

    public void setPlanYear(Integer planYear) {
        this.planYear = planYear;
    }

    public Integer getPlanLastYear() {
        return planYear - 1;
    }

    public Integer getPlanSeqno() {
        return planSeqno;
    }

    public void setPlanSeqno(Integer planSeqno) {
        this.planSeqno = planSeqno;
    }

    public String getPlanSeqnoeqnoname() {
        return planSeqnoeqnoname;
    }

    public String getPlanSeqnonameValue() {
        return ReqType.getByReqTypeValue(planSeqno);
    }

    public void setPlanSeqnoeqnoname(String planSeqnoeqnoname) {
        this.planSeqnoeqnoname = planSeqnoeqnoname;
    }

    public Integer getPlanYear() {
        return planYear;
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

    public Integer getPlanStatus() {
        return planStatus;
    }

    public String getPlanStatusValue() {
        return PlanStatusUtil.getStatusValue(planStatus);
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getItemListContent() {
        return itemListContent;
    }

    public void setItemListContent(String itemListContent) {
        this.itemListContent = itemListContent;
    }

    public String getItemChange() {
        return itemChange;
    }

    public void setItemChange(String itemChange) {
        this.itemChange = itemChange;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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


    public Integer getQueryCanChangeSeqNo() {
        return queryCanChangeSeqNo;
    }

    public void setQueryCanChangeSeqNo(Integer queryCanChangeSeqNo) {
        this.queryCanChangeSeqNo = queryCanChangeSeqNo;
    }

    public String[] getSpecialIds() {
        return specialIds;
    }

    public void setSpecialIds(String[] specialIds) {
        this.specialIds = specialIds;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getChanageDateBegin() {
        return chanageDateBegin;
    }

    public void setChanageDateBegin(Date chanageDateBegin) {
        this.chanageDateBegin = chanageDateBegin;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getChanageDateEnd() {
        return chanageDateEnd;
    }

    public void setChanageDateEnd(Date chanageDateEnd) {
        this.chanageDateEnd = chanageDateEnd;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getCanSearch() {
        return canSearch;
    }

    public void setCanSearch(String canSearch) {
        this.canSearch = canSearch;
    }
}