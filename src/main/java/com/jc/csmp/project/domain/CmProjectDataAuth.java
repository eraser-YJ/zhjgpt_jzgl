package com.jc.csmp.project.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 建设管理-项目数据权限子表
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectDataAuth extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectDataAuth(){}
    /**业务id*/
    private String businessId;
    /**业务参与组织id*/
    private String deptId;
    /**业务参与组织编码*/
    private String deptCode;
    private Integer limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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
}
