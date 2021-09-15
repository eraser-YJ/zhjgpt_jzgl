package com.jc.supervise.basic.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 勘察设计监督
 * @Author 常鹏
 * @Date 2020/7/22 10:27
 * @Version 1.0
 */
public class SuperviseBasicSurvey extends BaseBean {
    private static final long serialVersionUID = 1L;
    public SuperviseBasicSurvey(){}

    /**序号*/
    private String no;
    /**编码*/
    private String code;
    /**审图项目*/
    private String projectName;
    /**建设单位*/
    private String firstPartyDeptId;
    /**设计单位*/
    private String secondPartyDeptId;
    /**审查类型*/
    private String type;
    /**所属辖区*/
    private String region;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFirstPartyDeptId() {
        return firstPartyDeptId;
    }

    public void setFirstPartyDeptId(String firstPartyDeptId) {
        this.firstPartyDeptId = firstPartyDeptId;
    }

    public String getSecondPartyDeptId() {
        return secondPartyDeptId;
    }

    public void setSecondPartyDeptId(String secondPartyDeptId) {
        this.secondPartyDeptId = secondPartyDeptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
