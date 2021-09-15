package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserExt extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public UserExt() {
	}

	/**用户ID*/
	private String userId;
	/**样式类型*/
	private String cssType;
	/**样式值*/
	private String cssValue;
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getCssType(){
		return cssType;
	}
	public void setCssType(String cssType){
		this.cssType = cssType;
	}
	public String getCssValue(){
		return cssValue;
	}
	public void setCssValue(String cssValue){
		this.cssValue = cssValue;
	}
}