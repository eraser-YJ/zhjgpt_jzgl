package com.jc.csmp.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;
import com.jc.workflow.external.WorkflowBean;

import java.util.Date;
import java.util.Map;

/**
 * 建设管理-消息信息
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmMessageInfo extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmMessageInfo(){}
    /**标题*/
    private String title;
    /**内容*/
    private String content;
    /**接收人*/
    private String receiveId;
    /**接收部门*/
    private String receiveDeptId;
    /**接收部门编码*/
    private String receiveDeptCode;
    /**阅读状态*/
    private String readStatus;
    /**阅读时间*/
    private Date readDate;

    private String curUserId;
    private String workflowTitle;
    private String businessCode;
    private String businessDesc;
    private Date workflowCreateTime;
    private Map<String, Object> extendData;
    /**是否是移动端*/
    private String isMobile;
    private WorkflowBean workflowBean = new WorkflowBean();

    public String getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(String curUserId) {
        this.curUserId = curUserId;
    }

    public String getWorkflowTitle() {
        return workflowTitle;
    }

    public void setWorkflowTitle(String workflowTitle) {
        this.workflowTitle = workflowTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveDeptId() {
        return receiveDeptId;
    }

    public void setReceiveDeptId(String receiveDeptId) {
        this.receiveDeptId = receiveDeptId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public String getReadStatusValue() {
        if (!StringUtil.isEmpty(this.getReadStatus()) && this.getReadStatus().equals("1")) {
            return "已读";
        }
        return "未读";
    }

    public String getReceiveDeptCode() {
        return receiveDeptCode;
    }

    public void setReceiveDeptCode(String receiveDeptCode) {
        this.receiveDeptCode = receiveDeptCode;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",  timezone = "GMT+8")
    public Date getWorkflowCreateTime() {
        return workflowCreateTime;
    }

    public void setWorkflowCreateTime(Date workflowCreateTime) {
        this.workflowCreateTime = workflowCreateTime;
    }

    public String getWorkflowTitleValue() {
        String value = WorkflowContentEnum.getByCode(this.getWorkflowTitle()).getValue();
        return value != null && value.equals("- 全部 -") ? "" : value;
    }
    public String getWorkflowMenuId() {
        return WorkflowContentEnum.getByCode(this.getWorkflowTitle()).getMenuId();
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public Map<String, Object> getExtendData() {
        return extendData;
    }

    public void setExtendData(Map<String, Object> extendData) {
        this.extendData = extendData;
    }
}



