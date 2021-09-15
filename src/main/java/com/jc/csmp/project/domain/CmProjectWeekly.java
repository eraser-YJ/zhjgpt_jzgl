package com.jc.csmp.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;

/**
 * 周报管理
 * @author lc 
 * @version 2020-03-10
 */
public class CmProjectWeekly extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectWeekly() {
	}
	/**项目id*/
	private String projectId;
	/**周报名称*/
	private String reportName;
	/**周报信息*/
	private String reportRemark;
	/**事项安排*/
	private String arrangements;
	/**反馈*/
	private String feedback;
	/**反馈人*/
	private String feedbackUser;
	/**用户id*/
	private String userId;
	/**部门id*/
	private String deptId;
	/**是否反馈 0：未反馈 1：已反馈*/
	private Integer status;
	/**新增的附件*/
	private String attachFile;
	/**要删除的附件*/
	private String deleteAttachFile;
	/**周报事项*/
	private String weeklyItemIds;
	/***/
	private String projectName;
	private String projectNumber;
	private Date releaseDate;
	private Date releaseDateBegin;
	private Date releaseDateEnd;
	/**点击了发布=1*/
	private Integer clickRelease;

	private String region;
	private String projectType;
	private String buildType;
	/**是否发布*/
	private Integer ifRelease;
	private String deptCodeCondition;
	private List<CmProjectWeeklyItem> weeklyItemList;
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}
	public String getReportRemark() {
		return reportRemark;
	}
	public void setArrangements(String arrangements) {
		this.arrangements = arrangements;
	}
	public String getArrangements() {
		return arrangements;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStatus() {
		return status;
	}
	public String getStatusValue() {
		if (this.getStatus() == null) {
			return "";
		}
		if (this.getStatus().intValue() == 1) {
			return "已反馈";
		}
		return "未反馈";
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public Integer getIfRelease() {
		return ifRelease;
	}

	public void setIfRelease(Integer ifRelease) {
		this.ifRelease = ifRelease;
	}

	public String getIfReleaseValue() {
		if (this.getIfRelease() == null) {
			return "";
		}
		if (this.getIfRelease().intValue() == 1) {
			return "已发布";
		}
		return "未发布";
	}

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
	}

	public String getUserName() {
		User user = UserUtils.getUser(this.getUserId());
		if (user != null) {
			return user.getDisplayName();
		}
		return "";
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getReleaseDateBegin() {
		return releaseDateBegin;
	}

	public void setReleaseDateBegin(Date releaseDateBegin) {
		this.releaseDateBegin = releaseDateBegin;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getReleaseDateEnd() {
		return releaseDateEnd;
	}

	public void setReleaseDateEnd(Date releaseDateEnd) {
		this.releaseDateEnd = releaseDateEnd;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public Integer getClickRelease() {
		return clickRelease;
	}

	public void setClickRelease(Integer clickRelease) {
		this.clickRelease = clickRelease;
	}

	public String getWeeklyItemIds() {
		return weeklyItemIds;
	}

	public void setWeeklyItemIds(String weeklyItemIds) {
		this.weeklyItemIds = weeklyItemIds;
	}

	public List<CmProjectWeeklyItem> getWeeklyItemList() {
		return weeklyItemList;
	}

	public void setWeeklyItemList(List<CmProjectWeeklyItem> weeklyItemList) {
		this.weeklyItemList = weeklyItemList;
	}

	public String getFeedbackUser() {
		return feedbackUser;
	}

	public void setFeedbackUser(String feedbackUser) {
		this.feedbackUser = feedbackUser;
	}

	public String getFeedbackUserName() {
		User user = UserUtils.getUser(this.getFeedbackUser());
		if (user != null) {
			return user.getDisplayName();
		}
		return "";
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
}