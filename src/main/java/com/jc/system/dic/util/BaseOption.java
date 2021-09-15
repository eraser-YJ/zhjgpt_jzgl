package com.jc.system.dic.util;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class BaseOption implements Serializable{

	private static final long serialVersionUID = 4479619410838279867L;
	
	 //fields
    private java.lang.String optionType;
    private java.lang.String optionName;
    private java.lang.String optionValue;
    private java.lang.Integer displayOrder;

    //default constructor
    public BaseOption() {
    }

    //constructor with arguments
    public BaseOption(java.lang.String optionName, java.lang.String optionType) {
        this.optionName = optionName;
        this.optionType = optionType;
    }

    //getter
    public java.lang.String getOptionType() {
        return optionType;
    }

    public java.lang.String getOptionName() {
        return optionName;
    }

    public java.lang.String getOptionValue() {
        return optionValue;
    }

    public java.lang.Integer getDisplayOrder() {
        return displayOrder;
    }

    //setter
    public void setOptionType(java.lang.String optionType) {
        this.optionType = optionType;
    }

    public void setOptionName(java.lang.String optionName) {
        this.optionName = optionName;
    }

    public void setOptionValue(java.lang.String optionValue) {
        this.optionValue = optionValue;
    }

    public void setDisplayOrder(java.lang.Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    //equals method
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BaseOption)) {
            return false;
        }

        final BaseOption baseOption = (BaseOption) other;
        if (!this.optionName.equals(baseOption.getOptionName()) ||
                !this.optionType.equals(baseOption.getOptionType())) {
            return false;
        }

        return true;
    }

    //hashCode method
    @Override
    public int hashCode() {
        StringBuffer keys = new StringBuffer();
        keys.append(optionName).append(",");
        keys.append(optionType).append(",");
        if (keys.length() > 0) {
            keys.deleteCharAt(keys.length() - 1);
        }
        return keys.toString().hashCode();
    }

    //toString method
    @Override
    public String toString() {
        return new StringBuilder("BaseOption[")
                .append("optionType=").append(optionType).append(", ")
                .append("optionName=").append(optionName).append(", ")
                .append("optionValue=").append(optionValue).append(", ")
                .append("displayOrder=").append(displayOrder).append("]").toString();
    }
}
