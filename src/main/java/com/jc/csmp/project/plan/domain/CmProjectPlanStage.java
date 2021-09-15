package com.jc.csmp.project.plan.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 建设管理-项目计划阶段管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlanStage extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectPlanStage(){}
    /**所属模板*/
    private String projectId;
    /**阶段名称*/
    private String stageName;
    /**父亲节点*/
    private String parentId;
    /**排序*/
    private Integer queue;

    private Integer cc;

    private String templateId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getQueue() {
        return queue;
    }

    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Integer getCc() {
        return cc;
    }

    public void setCc(Integer cc) {
        this.cc = cc;
    }
}
