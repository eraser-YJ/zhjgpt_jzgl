package com.jc.resource.enums;

import com.jc.foundation.util.StringUtil;
import com.jc.resource.enums.service.IResourceService;
import com.jc.resource.enums.service.impl.CompanyInfoResourceServiceImpl;
import com.jc.resource.enums.service.impl.PersonInfoServiceImpl;
import com.jc.resource.enums.service.impl.ProjectApprovalResourceServiceImpl;
import com.jc.resource.enums.service.impl.ProjectInfoResourceServiceImpl;
import com.jc.resource.enums.service.impl.*;

/**
 * @Author 常鹏
 * @Date 2020/7/27 14:42
 * @Version 1.0
 */
public enum ResourceEnums {
    /***/
    pt_attach_info("id", "", "附件信息", null, new AttachResourceServiceImpl()),
    pt_company_info("id", "", "企业资源", null, new CompanyInfoResourceServiceImpl()),
    pt_project_approval("projectNumber", "projectNumber", "立项信息", "项目立项监管", new ProjectApprovalResourceServiceImpl()),
    pt_project_design("designNumber", "projectNumber", "勘察设计信息", "勘察设计监管", new ProjectDesignResourceServiceImpl()),
    pt_project_construction("constructionNumber", "projectNumber", "施工许可信息", "施工许可监管", new ProjectConstructionResourceServiceImpl()),
    pt_project_finish("projectNumber", "projectNumber", "竣工信息", "竣工备案监管", new ProjectFinishResourceServiceImpl()),
    pt_project_info("projectNumber", "projectNumber", "项目基本信息", "项目动态监管", new ProjectInfoResourceServiceImpl()),
    pt_person_info("id", "", "人员信息库", null, new PersonInfoServiceImpl()),
    pt_company_projects_ztb("id", "pcp_project_num","招投标", "招投标监管", new ProjectZtbResourceServiceImpl()),
    pt_company_projects_sgxk("id", "pcp_project_num", "施工许可证信息", "施工许可证监管", new ProjectSgxkResourceServiceImpl()),
    pt_company_projects_htba("id", "pcp_project_num", "合同备案", "合同备案监管", new ProjectHtbaResourceServiceImpl()),
    pt_project_quality("id", "quality_project", "质量问题", "质量监督监管", new QuestionQualityResourceServiceImpl()),
    pt_project_safe("id", "quality_project", "安全问题", "安全监督监管", new QuestionSafeResourceServiceImpl());
    private String pkColumn;
    private String value;
    private String project;
    private String supervisePageTitle;
    private IResourceService resourceService;

    ResourceEnums(String pkColumn, String project, String value, String supervisePageTitle, IResourceService resourceService) {
        this.pkColumn = pkColumn;
        this.value = value;
        this.project = project;
        this.supervisePageTitle = supervisePageTitle;
        this.resourceService = resourceService;
    }

    public static ResourceEnums getByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return null;
        }
        ResourceEnums[] array = values();
        ResourceEnums result = null;
        for (ResourceEnums resourceEnums : array) {
            if (resourceEnums.toString().equals(code)) {
                result = resourceEnums;
                break;
            }
        }
        return result;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getSupervisePageTitle() {
        return supervisePageTitle;
    }

    public void setSupervisePageTitle(String supervisePageTitle) {
        this.supervisePageTitle = supervisePageTitle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
