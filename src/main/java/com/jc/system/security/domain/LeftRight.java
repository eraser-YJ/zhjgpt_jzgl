package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class LeftRight extends BaseBean {
	
	private static final long serialVersionUID = 1396039427853446822L;
	/**选择项标志*/
	private String code;
	/**选择项显示内容*/
	private String text;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
