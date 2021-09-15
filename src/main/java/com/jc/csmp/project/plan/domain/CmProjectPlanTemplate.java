package com.jc.csmp.project.plan.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 建设管理-项目计划模板管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlanTemplate extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectPlanTemplate(){}
    /**模板名称*/
    private String templateName;
    /**模板所属单位*/
    private String deptId;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
