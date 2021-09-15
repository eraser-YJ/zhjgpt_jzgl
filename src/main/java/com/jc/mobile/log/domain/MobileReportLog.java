package com.jc.mobile.log.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.mobile.log.enums.MobileReportLogEnum;

/**
 * 建设管理-移动端上报日志
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class MobileReportLog extends BaseBean {
    private static final long serialVersionUID = 1L;
    public MobileReportLog(){}
    /**日志类型*/
    private String businessType;
    /**业务id*/
    private String businessId;
    /**上报人*/
    private String userId;
    /**上报人所属部门code*/
    private String deptId;
    /**上报人所属部门*/
    private String deptCode;
    /**内容*/
    private String content;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBusinessTypeValue() {
        return MobileReportLogEnum.getByCode(this.getBusinessType()).getValue();
    }
}



