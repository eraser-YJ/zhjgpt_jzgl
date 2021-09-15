package com.jc.csmp.common;

import java.io.Serializable;

public class WorkflowVarVO implements Serializable {

	private String privilege;

	private String formName;

	private String columnName;
	private String itemName;
	private CustomPropertyVo customProperty;//流程必填属性
	private String type;

	private String custom;

	/**
	 * 返回显示登记
	 * 
	 * @return must 必输；edit 编辑；readonly 显示；hidden 隐藏；
	 */
	public String getAttributeAccessCtrl() {
		if ("must".equalsIgnoreCase(custom) && "edit".equalsIgnoreCase(privilege)) {
			return "must";
		} else if ("edit".equalsIgnoreCase(privilege)) {
			return "edit";
		} else if ("hidden".equalsIgnoreCase(privilege)) {
			return "hidden";
		}
		return "readonly";
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public CustomPropertyVo getCustomProperty() {
		return customProperty;
	}

	public void setCustomProperty(CustomPropertyVo customProperty) {
		this.customProperty = customProperty;
	}
}
