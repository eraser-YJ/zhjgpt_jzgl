package com.jc.system.common.domain;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class CascadeBusinessSetting {
	public CascadeBusinessSetting() {}
	private CascadeSetting[] function;
	public CascadeSetting[] getFunction() {
		return function;
	}
	public void setFunction(CascadeSetting[] function) {
		this.function = function;
	}
}
