package com.jc.supervise.warning.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;

import java.util.Date;

/**
 * 建设管理-督办
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmSupervisionMessage extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmSupervisionMessage(){}
    /**预警id*/
    private String warningId;
    /**督办标题*/
    private String title;
    /**内容*/
    private String content;
    /**发送人*/
    private String senderId;
    /**发送机构*/
    private String senderDeptId;
    /**发送机构编码*/
    private String senderDeptCode;
    /**接收人*/
    private String receiveId;
    /**接收部门*/
    private String receiveDeptId;
    /**接收部门编号*/
    private String receiveDeptCode;
    /**办理结果*/
    private String handleResult;
    /**办理状态*/
    private String handleStatus;
    /**办理时间*/
    private Date handleDate;

    public String getWarningId() {
        return warningId;
    }

    public void setWarningId(String warningId) {
        this.warningId = warningId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderDeptId() {
        return senderDeptId;
    }

    public void setSenderDeptId(String senderDeptId) {
        this.senderDeptId = senderDeptId;
    }

    public String getSenderDeptCode() {
        return senderDeptCode;
    }

    public void setSenderDeptCode(String senderDeptCode) {
        this.senderDeptCode = senderDeptCode;
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

    public String getReceiveDeptCode() {
        return receiveDeptCode;
    }

    public void setReceiveDeptCode(String receiveDeptCode) {
        this.receiveDeptCode = receiveDeptCode;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "GMT+8")
    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHandleStatusValue() {
        if (!StringUtil.isEmpty(this.getHandleStatus()) && this.getHandleStatus().equals("1")) {
            return "已办理";
        }
        return "未办理";
    }

    /**发送人姓名*/
    public String getSenderName() {
        if (StringUtil.isEmpty(this.getSenderId())) {
            return "";
        }
        User user = UserUtils.getUser(this.getSenderId());
        if (user != null) {
            return user.getDisplayName();
        }
        return "";
    }

    /**发送人所在部门名*/
    public String getSenderDeptName() {
        if (StringUtil.isEmpty(this.getSenderDeptId())) {
            return "";
        }
        try {
            Department department = DeptCacheUtil.getDeptById(this.getSenderDeptId());
            if (department != null) {
                return department.getName();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**接收人姓名*/
    public String getReceiveName() {
        if (StringUtil.isEmpty(this.getReceiveId())) {
            return "";
        }
        User user = UserUtils.getUser(this.getReceiveId());
        if (user != null) {
            return user.getDisplayName();
        }
        return "";
    }

    /**接收人所在部门名*/
    public String getReceiveDeptName() {
        if (StringUtil.isEmpty(this.getReceiveDeptId())) {
            return "";
        }
        try {
            Department department = DeptCacheUtil.getDeptById(this.getReceiveDeptId());
            if (department != null) {
                return department.getName();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}



