package com.jc.supervise.basic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 施工许可
 * @Author 常鹏
 * @Date 2020/7/22 9:54
 * @Version 1.0
 */
public class SuperviseBasicConstructionPermit extends BaseBean {
    private static final long serialVersionUID = 1L;
    public SuperviseBasicConstructionPermit(){}
    /**序号*/
    private Integer no;
    /**施工许可证号*/
    private String code;
    /**项目名称*/
    private String projectName;
    /**建设单位*/
    private String firstPartyDeptId;
    /**施工单位*/
    private String secondPartyDeptId;
    /**发证类型*/
    private String cardType;
    /**发证日期*/
    private Date sendDate;
    private Date sendDateBegin;
    private Date sendDateEnd;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getFirstPartyDeptId() {
        return firstPartyDeptId;
    }

    public void setFirstPartyDeptId(String firstPartyDeptId) {
        this.firstPartyDeptId = firstPartyDeptId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getSendDateBegin() {
        return sendDateBegin;
    }

    public void setSendDateBegin(Date approvalDateBegin) {
        this.sendDateBegin = sendDateBegin;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
    public Date getSendDateEnd() {
        return sendDateEnd;
    }

    public void setSendDateEnd(Date approvalDateEnd) {
        this.sendDateEnd = DateUtils.fillTime(sendDateEnd);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecondPartyDeptId() {
        return secondPartyDeptId;
    }

    public void setSecondPartyDeptId(String secondPartyDeptId) {
        this.secondPartyDeptId = secondPartyDeptId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
