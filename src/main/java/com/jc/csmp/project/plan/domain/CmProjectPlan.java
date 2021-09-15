package com.jc.csmp.project.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.security.util.DeptCacheUtil;

/**
 * 建设管理-项目计划管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlan extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectPlan() {
	}
	/**项目ID*/
	private String projectId;
	/**所属阶段*/
	private String stageId;
	/**计划编码*/
	private String planCode;
	/**计划名称*/
	private String planName;
	/**任务产值*/
	private BigDecimal outputValue;
	/**责任单位*/
	private String dutyCompany;
	/**责任人*/
	private String dutyPerson;
	/**计划开始时间*/
	private Date planStartDate;
	private Date planStartDateBegin;
	private Date planStartDateEnd;
	/**计划结束时间*/
	private Date planEndDate;
	private Date planEndDateBegin;
	private Date planEndDateEnd;
	/**实际开始时间*/
	private Date actualStartDate;
	private Date actualStartDateBegin;
	private Date actualStartDateEnd;
	/**实际结束时间*/
	private Date actualEndDate;
	private Date actualEndDateBegin;
	private Date actualEndDateEnd;
	/**排序*/
	private Integer queue;
	/**备注*/
	private String remark;
	private Integer planDay;
	private Integer planWorkDay;
	private BigDecimal selfWeight;
	/**计划完成情况*/
	private String finishResult;
	private String templateId;
	private String stageName;
	private String[] stageIds;
	private String attachFile;
	private String deleteAttachFile;

	/**完成进度*/
	private Integer completionRatio;
	/**完成金额*/
	private BigDecimal completionMoney;

	/**项目计划状态 wait, ing, finish*/
	private String statusCondition;
	/**项目详细信息*/
	private String projectName;

	/**反馈*/
	private String feedback;
	/**反馈人员*/
	private String feedbackUser;
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public String getStageId() {
		return stageId;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanName() {
		return planName;
	}
	public void setOutputValue(BigDecimal outputValue) {
		this.outputValue = outputValue;
	}
	public BigDecimal getOutputValue() {
		return outputValue;
	}
	public void setDutyCompany(String dutyCompany) {
		this.dutyCompany = dutyCompany;
	}
	public String getDutyCompany() {
		return dutyCompany;
	}
	public void setDutyPerson(String dutyPerson) {
		this.dutyPerson = dutyPerson;
	}
	public String getDutyPerson() {
		return dutyPerson;
	}
	public void setPlanStartDateBegin(Date planStartDateBegin) {
		this.planStartDateBegin = planStartDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanStartDateBegin() {
		return planStartDateBegin;
	}
	public void setPlanStartDateEnd(Date planStartDateEnd) {
		if(planStartDateEnd == null){
			return;
		}
		this.planStartDateEnd = DateUtils.fillTime(planStartDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanStartDateEnd() {
		return planStartDateEnd;
	}
	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanEndDateBegin(Date planEndDateBegin) {
		this.planEndDateBegin = planEndDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanEndDateBegin() {
		return planEndDateBegin;
	}
	public void setPlanEndDateEnd(Date planEndDateEnd) {
		if(planEndDateEnd == null){
			return;
		}
		this.planEndDateEnd = DateUtils.fillTime(planEndDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanEndDateEnd() {
		return planEndDateEnd;
	}
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPlanEndDate() {
		return planEndDate;
	}
	public void setActualStartDateBegin(Date actualStartDateBegin) {
		this.actualStartDateBegin = actualStartDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualStartDateBegin() {
		return actualStartDateBegin;
	}
	public void setActualStartDateEnd(Date actualStartDateEnd) {
		if(actualStartDateEnd == null){
			return;
		}
		this.actualStartDateEnd = DateUtils.fillTime(actualStartDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualStartDateEnd() {
		return actualStartDateEnd;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualEndDateBegin(Date actualEndDateBegin) {
		this.actualEndDateBegin = actualEndDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualEndDateBegin() {
		return actualEndDateBegin;
	}
	public void setActualEndDateEnd(Date actualEndDateEnd) {
		if(actualEndDateEnd == null){
			return;
		}
		this.actualEndDateEnd = DateUtils.fillTime(actualEndDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualEndDateEnd() {
		return actualEndDateEnd;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setQueue(Integer queue) {
		this.queue = queue;
	}
	public Integer getQueue() {
		return queue;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setFinishResult(String finishResult) {
		this.finishResult = finishResult;
	}
	public String getFinishResult() {
		return finishResult;
	}
	public String getFinishResultValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.ifFinish, this.getFinishResult());
	}

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

	public String[] getStageIds() {
		return stageIds;
	}

	public void setStageIds(String[] stageIds) {
		this.stageIds = stageIds;
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

	public BigDecimal getSelfWeight() {
		return selfWeight;
	}

	public void setSelfWeight(BigDecimal selfWeight) {
		this.selfWeight = selfWeight;
	}

	public String getDutyCompanyValue() {
		return DeptCacheUtil.getNameById(this.getDutyCompany());
	}

	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}

	public String getDeleteAttachFile() {
		return deleteAttachFile;
	}

	public void setDeleteAttachFile(String deleteAttachFile) {
		this.deleteAttachFile = deleteAttachFile;
	}

	public Integer getCompletionRatio() {
		return completionRatio;
	}

	public void setCompletionRatio(Integer completionRatio) {
		this.completionRatio = completionRatio;
	}

	public String getStatus() {
		if (this.getActualStartDate() == null) {
			return "wait";
		} else {
			if (this.getActualEndDate() != null ) {
				return "finish";
			}
		}
		return "ing";
	}

	public String getStatusValue() {
		if (this.getActualStartDate() == null) {
			return "待执行";
		} else {
			if (this.getActualEndDate() != null) {
				return "已完成";
			}
		}
		return "进行中";
	}

	public BigDecimal getCompletionMoney() {
		return completionMoney;
	}

	public void setCompletionMoney(BigDecimal completionMoney) {
		this.completionMoney = completionMoney;
	}

	public String getStatusCondition() {
		return statusCondition;
	}

	public void setStatusCondition(String statusCondition) {
		this.statusCondition = statusCondition;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getFeedbackUser() {
		return feedbackUser;
	}

	public void setFeedbackUser(String feedbackUser) {
		this.feedbackUser = feedbackUser;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}