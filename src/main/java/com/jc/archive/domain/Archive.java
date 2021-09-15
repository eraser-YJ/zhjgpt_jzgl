package com.jc.archive.domain;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.BaseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 *
 * @title GOA V2.0 归档接口参数类
 * @description  文档管理：归档接口所用参数
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author zhangligang
 * @version  2014年7月9日 下午2:10:15
 */
public class Archive extends BaseBean{

	private String folderId;/*归档目录*/
	private String archiveName;/*归档名称*/
	private String archiveUserName;/*归档人姓名*/
	private String fileSize;
	private String tableName;
	private Date archiveDate;/*归档时间*/
	private List<ArchiveContent> list;/*前台传递的正文信息 正文名称和正文路径*/
	private String piId;/*业务唯一标识：流程ID或其它任何能唯一确定一条业务数据的值*/
	private String formContent;/*表单内容*/
	private Map<String,String> bodyMap;/*正文文件：key,显示名称；Value：文件相对路径（带真实文件夹名,如：/upload/office/xxxxx.txt）*/
	private Map<String,String> filingMap;/*归档附件：key,显示名称；Value：文件相对路径（带真实文件夹名,如：/upload/office/xxxxx.txt）*/
	private String htmlFileName;
	private String createUserName;
	private String createUserDeptName;
	private List<Filing> fileList;
	private String docId;

	public String getCreateUserDeptName() {

		return createUserDeptName;
	}

	public void setCreateUserDeptName(String createUserDeptName) {

		this.createUserDeptName = createUserDeptName;
	}

	public String getCreateUserName() {

		return createUserName;
	}

	public void setCreateUserName(String createUserName) {

		this.createUserName = createUserName;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public List<Filing> getFileList() {
		return fileList;
	}
	public void setFileList(List<Filing> fileList) {
		this.fileList = fileList;
	}
	public String getHtmlFileName() {
		return htmlFileName;
	}
	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}
	private List<Attach> attachList = new ArrayList<Attach>();

	public List<Attach> getAttachList() {
		return attachList;
	}
	public void setAttachList(List<Attach> attachList) {
		this.attachList = attachList;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFormContent() {
		return formContent;
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public void setFormContent(String formContent) {
		this.formContent = formContent;
	}
	public Map<String, String> getBodyMap() {
		return bodyMap;
	}
	public void setBodyMap(Map<String, String> bodyMap) {
		this.bodyMap = bodyMap;
	}
	public String getArchiveName() {
		return archiveName;
	}
	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}
	public String getPiId() {
		return piId;
	}
	public void setPiId(String piId) {
		this.piId = piId;
	}
	public Map<String, String> getFilingMap() {
		return filingMap;
	}
	public void setFilingMap(Map<String, String> filingMap) {
		this.filingMap = filingMap;
	}
	public String getArchiveUserName() {
		return archiveUserName;
	}
	public void setArchiveUserName(String archiveUserName) {
		this.archiveUserName = archiveUserName;
	}
	public Date getArchiveDate() {
		return archiveDate;
	}
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}
	public List<ArchiveContent> getList() {
		return list;
	}
	public void setList(List<ArchiveContent> list) {
		this.list = list;
	}


}
