package com.jc.csmp.item.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.jc.foundation.util.DateUtils;

/**
 * @author lc 
 * @version 2020-03-10
 */
public class ItemClassify extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ItemClassify() {
	}
	private String itemClassify;
	private String itemCode;
	private List<ItemClassifyAttach>  itemClassifyAttachList;

	public void setItemClassify(String itemClassify) {
		this.itemClassify = itemClassify;
	}
	public String getItemClassify() {
		return itemClassify;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemCode() {
		return itemCode;
	}

	public List<ItemClassifyAttach> getItemClassifyAttachList() {
		return itemClassifyAttachList;
	}

	public void setItemClassifyAttachList(List<ItemClassifyAttach> itemClassifyAttachList) {
		this.itemClassifyAttachList = itemClassifyAttachList;
	}
}