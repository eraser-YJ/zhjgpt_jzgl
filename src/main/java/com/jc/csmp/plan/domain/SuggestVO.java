package com.jc.csmp.plan.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SuggestVO implements Serializable {
	private String message;
	private String userName;
	private Date createDate;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateDateDesc() {
		if (createDate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f.format(createDate);
		}
		return "";
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
