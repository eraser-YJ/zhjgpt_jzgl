package com.jc.archive.domain;

import java.util.Date;

import com.jc.foundation.domain.BaseBean;


/**
 * @title  GOA2.0源代码
 * @description 该表执行物理删除
更新时删除原有记录，添加新记录 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-19
 */

public class Recycle extends BaseBean{
	private static final long serialVersionUID = 1L;
	private String dmName;   /*文档管理_文档名称*/
	private String contentType;   /*文档内容类型 0 未知  1  word  2 excel  3 ppt */
	private String dmSize;   /*文档大小*/
	private String type;   /*0：文件夹  1：文件*/
	private String dmSuffix;   /*文档管理_文档历史后缀名*/
	public String getDmSuffix() {
		return dmSuffix;
	}
	public void setDmSuffix(String dmSuffix) {
		this.dmSuffix = dmSuffix;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDmName() {
		return dmName;
	}
	public void setDmName(String dmName) {
		this.dmName = dmName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDmSize() {
		return dmSize;
	}
	public void setDmSize(String dmSize) {
		this.dmSize = dmSize;
	}
	@Override
    public Date getModifyDate() {
		return modifyDate;
	}
	@Override
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}