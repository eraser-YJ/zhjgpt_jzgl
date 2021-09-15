package com.jc.csmp.project.plan.domain;

import com.jc.foundation.domain.BaseBean;

import java.util.List;

/**
 * 建设管理-项目计划模板阶段管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlanTemplateStage extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectPlanTemplateStage(){}
    /**所属模板*/
    private String templateId;
    /**阶段名称*/
    private String stageName;
    /**父亲节点*/
    private String parentId;
    /**排序*/
    private Integer queue;

    private List<CmProjectPlanTemplateStage> childList;
    private List<CmProjectPlanTemplateTask> taskList;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public List<CmProjectPlanTemplateTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<CmProjectPlanTemplateTask> taskList) {
        this.taskList = taskList;
    }

    public List<CmProjectPlanTemplateStage> getChildList() {
        return childList;
    }

    public void setChildList(List<CmProjectPlanTemplateStage> childList) {
        this.childList = childList;
    }
}
