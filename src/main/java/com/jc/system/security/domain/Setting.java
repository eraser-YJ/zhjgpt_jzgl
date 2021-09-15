package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 设置
 * @author Administrator
 * @date 2020-06-30
 */
public class Setting extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**系统参数key*/
	private String settingKey;
	/**系统参数value*/
	private String settingValue;
	/**系统参数描述*/
	private String settingComment;
	/**系统参数输入框类型*/
	private String settingInputType;
	/**系统参数输入最大长度*/
	private String settingInputSize;
	/**系统参数输入框默认值*/
	private String settingInputDefault;
	/**系统参数输入框备注*/
	private String settingInputRemark;
	/**系统参数输入框验证*/
	private String settingInputValidate;
	public String getSettingKey() {
		return settingKey;
	}
	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	public String getSettingComment() {
		return settingComment;
	}
	public void setSettingComment(String settingComment) {
		this.settingComment = settingComment;
	}
	public String getSettingInputType() {
		return settingInputType;
	}
	public void setSettingInputType(String settingInputType) {
		this.settingInputType = settingInputType;
	}
	public String getSettingInputSize() {
		return settingInputSize;
	}
	public void setSettingInputSize(String settingInputSize) {
		this.settingInputSize = settingInputSize;
	}
	public String getSettingInputDefault() {
		return settingInputDefault;
	}
	public void setSettingInputDefault(String settingInputDefault) {
		this.settingInputDefault = settingInputDefault;
	}
	public String getSettingInputRemark() {
		return settingInputRemark;
	}
	public void setSettingInputRemark(String settingInputRemark) {
		this.settingInputRemark = settingInputRemark;
	}
	public String getSettingInputValidate() {
		return settingInputValidate;
	}
	public void setSettingInputValidate(String settingInputValidate) {
		this.settingInputValidate = settingInputValidate;
	}
	public String[][] getInputDefaultValue(){
		String[] defaultValueAndName = settingInputDefault.split(",");
		String[][] returnvalue = new String[defaultValueAndName.length][2];
		for(int i=0;i<defaultValueAndName.length;i++){
			returnvalue[i][0] = defaultValueAndName[i].split("&")[0];
			returnvalue[i][1] = defaultValueAndName[i].split("&")[1];
		}
		return returnvalue;
	}
}