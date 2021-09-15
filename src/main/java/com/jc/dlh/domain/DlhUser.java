package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @title 统一数据资源中心
 * @description 用户表 实体类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company
 * @author lc  
 * @version  2020-03-13
 */

public class DlhUser extends BaseBean {

	public DlhUser() {
	}

	/**编码*/
	private String dlhUsercode;
	/**用户*/
	private String dlhUsername;
	/**公共密码*/
	private String dlhPasswordPublic;
	/**私钥密码*/
	private String dlhPasswordPrivate;
	/**一次最大量*/
	private Integer batchNum;
	/**备注*/
	private String remark;
	/**密码*/
	private String dlhPassword;

	public String getDlhUsercode() {
		return dlhUsercode;
	}

	public void setDlhUsercode(String dlhUsercode) {
		this.dlhUsercode = dlhUsercode;
	}

	public String getDlhUsername() {
		return dlhUsername;
	}

	public void setDlhUsername(String dlhUsername) {
		this.dlhUsername = dlhUsername;
	}

	public String getDlhPasswordPublic() {
		return dlhPasswordPublic;
	}

	public void setDlhPasswordPublic(String dlhPasswordPublic) {
		this.dlhPasswordPublic = dlhPasswordPublic;
	}

	public String getDlhPasswordPrivate() {
		return dlhPasswordPrivate;
	}

	public void setDlhPasswordPrivate(String dlhPasswordPrivate) {
		this.dlhPasswordPrivate = dlhPasswordPrivate;
	}

	public Integer getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(Integer batchNum) {
		this.batchNum = batchNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDlhPassword() {
		return dlhPassword;
	}

	public void setDlhPassword(String dlhPassword) {
		this.dlhPassword = dlhPassword;
	}

}