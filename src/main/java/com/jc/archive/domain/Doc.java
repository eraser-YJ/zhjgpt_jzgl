package com.jc.archive.domain;

import java.util.Date;

/**
 * @title  GOA2.0源代码
 * @description 根据公文生成XML 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2016-03-09
 */
public class Doc {
	
	private String title; /*发文标题*/
	private String type; /*类型*/
	private String keyWord; /*发文主题词*/
	private String proof; /*发文文号*/
	private String filingPeopleName; /*发件人*/
	private String writtenDate; /*发件时间*/
	private String formFileName; /*表单pdf文件名称*/
	private String contentFileName; /*正文pdf文件名称*/
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	public String getFilingPeopleName() {
		return filingPeopleName;
	}
	public void setFilingPeopleName(String filingPeopleName) {
		this.filingPeopleName = filingPeopleName;
	}
	public String getWrittenDate() {
		return writtenDate;
	}
	public void setWrittenDate(String writtenDate) {
		this.writtenDate = writtenDate;
	}
	public String getFormFileName() {
		return formFileName;
	}
	public void setFormFileName(String formFileName) {
		this.formFileName = formFileName;
	}
	public String getContentFileName() {
		return contentFileName;
	}
	public void setContentFileName(String contentFileName) {
		this.contentFileName = contentFileName;
	}
	
}
