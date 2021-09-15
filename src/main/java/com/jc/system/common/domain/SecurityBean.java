package com.jc.system.common.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.ISecurityBean;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SecurityBean extends BaseBean implements ISecurityBean {
	private static final long serialVersionUID = 8370618015406833649L;
	/** 安全系数 */
	private Integer weight;
	
	@Override
	public Integer getWeight() {
		return weight;
	}
	
	@Override
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
