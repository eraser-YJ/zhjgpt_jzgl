package com.jc.csmp.plan.ctrl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.plan.kit.ReqType;
import com.jc.foundation.domain.BaseBean;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;

import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class ProjectYearPlanCtrl extends BaseBean {

    private static final long serialVersionUID = 1L;

    public ProjectYearPlanCtrl() {
    }

    private Integer planYear;
    private Date requestStartDate;
    private Date requestStartDateBegin;
    private Date requestStartDateEnd;
    private Date requestEndDate;
    private Date requestEndDateBegin;
    private Date requestEndDateEnd;
    private Integer seqno;
    private String seqnoname;
    private String remark;

    public void setPlanYear(Integer planYear) {
        this.planYear = planYear;
    }

    public Integer getPlanYear() {
        return planYear;
    }

    public void setRequestStartDateBegin(Date requestStartDateBegin) {
        this.requestStartDateBegin = requestStartDateBegin;
    }

    public Date getRequestStartDateBegin() {
        return requestStartDateBegin;
    }

    public void setRequestStartDateEnd(Date requestStartDateEnd) {
        if (requestStartDateEnd == null) {
            return;
        }
        this.requestStartDateEnd = DateUtils.fillTime(requestStartDateEnd);
    }

    public Date getRequestStartDateEnd() {
        return requestStartDateEnd;
    }

    public void setRequestStartDate(Date requestStartDate) {
        this.requestStartDate = requestStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getRequestStartDate() {
        return requestStartDate;
    }

    public void setRequestEndDateBegin(Date requestEndDateBegin) {
        this.requestEndDateBegin = requestEndDateBegin;
    }

    public Date getRequestEndDateBegin() {
        return requestEndDateBegin;
    }


    public void setRequestEndDateEnd(Date requestEndDateEnd) {
        if (requestEndDateEnd == null) {
            return;
        }
        this.requestEndDateEnd = DateUtils.fillTime(requestEndDateEnd);
    }

    public Date getRequestEndDateEnd() {
        return requestEndDateEnd;
    }

    public void setRequestEndDate(Date requestEndDate) {
        this.requestEndDate = requestEndDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getRequestEndDate() {
        return requestEndDate;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqnoname(String seqnoname) {
        this.seqnoname = seqnoname;
    }

    public String getSeqnoname() {
        return seqnoname;
    }

    public String getSeqnonameValue() {
        return ReqType.getByReqTypeValue(seqno);

    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }


}