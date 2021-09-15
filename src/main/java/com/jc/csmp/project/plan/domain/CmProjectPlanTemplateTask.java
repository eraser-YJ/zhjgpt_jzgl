package com.jc.csmp.project.plan.domain;

import com.jc.foundation.domain.BaseBean;

import java.math.BigDecimal;

/**
 * 建设管理-项目计划模板任务管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlanTemplateTask extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmProjectPlanTemplateTask(){}
    /**所属模板*/
    private String templateId;
    /**所属阶段*/
    private String stageId;
    /**任务编码*/
    private String taskNumber;
    /**任务名称*/
    private String taskName;
    /**计划日历天*/
    private Integer planDay;
    /**计划工时天*/
    private Integer planWorkDay;
    /**权重*/
    private BigDecimal selfWeight;
    /**排序*/
    private Integer queue;

    private String stageName;

    private String[] stageIds;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPlanDay() {
        return planDay;
    }

    public void setPlanDay(Integer planDay) {
        this.planDay = planDay;
    }

    public Integer getPlanWorkDay() {
        return planWorkDay;
    }

    public void setPlanWorkDay(Integer planWorkDay) {
        this.planWorkDay = planWorkDay;
    }

    public Integer getQueue() {
        return queue;
    }

    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public BigDecimal getSelfWeight() {
        return selfWeight;
    }

    public void setSelfWeight(BigDecimal selfWeight) {
        this.selfWeight = selfWeight;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String[] getStageIds() {
        return stageIds;
    }

    public void setStageIds(String[] stageIds) {
        this.stageIds = stageIds;
    }
}
