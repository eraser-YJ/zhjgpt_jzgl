package com.jc.csmp.project.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;

/**
 * 建设管理-人员坐标上报
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPersonLnglat extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectPersonLnglat() {
	}
	/**项目编号*/
	private String projectNumber;
	private String projectName;
	/**人员id*/
	private String userId;
	/**lng*/
	private String lng;
	/**lat*/
	private String lat;

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getUserName() {
		try {
			User user = UserUtils.getUser(this.getUserId());
			if (user != null) {
				return user.getDisplayName();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getJuli() {
		return this.getExtStr1() == null ? "" : this.getExtStr1();
	}
}