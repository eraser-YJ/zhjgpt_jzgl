package com.jc.csmp.item.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;

/**
 * @author lc 
 * @version 2020-03-10
 */
public class ItemClassifyAttach extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ItemClassifyAttach() {
	}
	private String itemAttach;
	private String itemId;
	private String itemName;
	private Integer isRequired;
	private Integer isCheckbox;
	private String itemCode;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setItemAttach(String itemAttach) {
		this.itemAttach = itemAttach;
	}
	public String getItemAttach() {
		return itemAttach;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}
	public Integer getIsRequired() {
		return isRequired;
	}
	public void setIsCheckbox(Integer isCheckbox) {
		this.isCheckbox = isCheckbox;
	}
	public Integer getIsCheckbox() {
		return isCheckbox;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}