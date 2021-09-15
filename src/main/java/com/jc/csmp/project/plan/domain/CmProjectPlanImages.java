package com.jc.csmp.project.plan.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;

import java.util.List;
import java.util.Map;

/**
 * 建设管理-形象进度
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPlanImages extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectPlanImages() {
	}
	/**所属项目*/
	private String projectId;
	/**所属计划*/
	private String planId;
	/**标题*/
	private String title;
	/**说明*/
	private String content;
	/**上报人*/
	private String userId;
	/**新增的附件*/
	private String attachFile;
	/**要删除的附件*/
	private String deleteAttachFile;
	/**项目名称*/
	private String projectName;
	/**所属阶段*/
	private String stageName;
	/**所属阶段*/
	private String stageId;

	private String loadAttachUrl;

	private List<Map<String, String>> attachList;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserName() {
		if (StringUtil.isEmpty(this.getUserId())) {
			return "";
		}
		User user = UserUtils.getUser(this.getUserId());
		if (user != null) {
			return user.getDisplayName();
		}
		return "";
	}

	public String getLoadAttachUrl() {
		return loadAttachUrl;
	}

	public void setLoadAttachUrl(String loadAttachUrl) {
		this.loadAttachUrl = loadAttachUrl;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public List<Map<String, String>> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<Map<String, String>> attachList) {
		this.attachList = attachList;
	}
}