package com.jc.csmp.common.enums;

/**
 * 建设管理-字典typeCode和parentCode的枚举类型
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public enum DicKeyEnum {
    /***/
    companyType("company_type", "csmp", "企业类型"),
    contractType("contract_type", "csmp", "合同类型"),
    civilizedLand("civilized_land", "csmp", "文明用地"),
    goalOfExcellence("goal_of_excellence", "csmp", "创优目标"),
    paymentMethod("payment_method", "csmp", "付款方式"),
    yesOrNo("yes_or_no", "csmp", "是否"),
    region("region", "csmp", "行政区"),
    biddingMethod("bidding_method", "csmp", "招标方式"),
    workFlowAuditStatus("workflow_audit_state", "csmp", "工作流审核状态"),
    projectChangeType("project_change_type", "csmp", "工程变更单类型"),
    projectQuestionType("project_question_type", "csmp", "工程问题类型"),
    buildProperties("build_property", "csmp", "建设性质"),
    investmentCategory("investment_category", "csmp", "投资类别"),
    projectType("project_type", "csmp", "工程类型"),
    structureType("structure_type", "csmp", "结构类型"),
    ifFinish("if_finish", "csmp", "完成情况"),
    buildType("build_type", "csmp", "建设类型"),
    completion("completion", "csmp", "在建项目"),
    highBuild("high_build", "csmp", "高层建筑"),
    pcpZblx("bidding_type", "data_center", "招标类型"),
    jdType("jd_type", "csmp", "招标监督类型"),
    safeFailureType("safe_failure_type", "csmp", "安全事故类型"),
    buildingTypes("buildingTypes", "csmp", "安全事故类型");

    private String typeCode;
    private String parentCode;
    private String value;
    DicKeyEnum(String typeCode, String parentCode, String value) {
        this.typeCode = typeCode;
        this.parentCode = parentCode;
        this.value = value;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}