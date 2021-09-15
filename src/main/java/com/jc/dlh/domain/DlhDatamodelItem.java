package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.system.dic.domain.Dic;

import java.util.List;

/**
 * @title 统一数据资源中心
 * @description 数据模型属性表 实体类 
 * @author lc 
 * @version 2020-03-10
 */

public class DlhDatamodelItem extends BaseBean {

	private static final long serialVersionUID = 1L;

	public DlhDatamodelItem() {
	}

	/** 模型主键 */
	private String modelId;
	/** 列名 */
	private String itemName;
	/** 列描述 */
	private String itemComment;
	/** 列类型 */
	private String itemType;
	/** 字典类型 */
	private String dicCode;
	/** 列长度 */
	private String itemLen;
	/** 是否主键 */
	private String itemKey;
	/** 排序 */
	private Integer itemSeq;
	/** 检查 */
	private String itemCheck;

	private List<Dic> dicList ;

	public List<Dic> getDicList() {
		return dicList;
	}

	public void setDicList(List<Dic> dicList) {
		this.dicList = dicList;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemComment() {
		return itemComment;
	}

	public void setItemComment(String itemComment) {
		this.itemComment = itemComment;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getItemLen() {
		return itemLen;
	}

	public void setItemLen(String itemLen) {
		this.itemLen = itemLen;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public Integer getItemSeq() {
		return itemSeq;
	}

	public void setItemSeq(Integer itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getItemCheck() {
		return itemCheck;
	}

	public void setItemCheck(String itemCheck) {
		this.itemCheck = itemCheck;
	}

}