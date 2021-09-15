package com.jc.archive.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.util.Date;


/**
 * @title  GOA2.0源代码
 * @description OA_文档管理/知识管理_文档信息表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author
 * @version  2014-06-05
 */

public class Document4Rtx implements  java.io.Serializable{
	private String id;
	private static final long serialVersionUID = 1L;
	private String folderId;   /*文件夹ID*/
	private Date modifyDate;
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	private Date createDate;
	private String fileType;   /*文件存放类型：0文档 1 知识 2 链接 3 收藏 4归档*/
	private String contentType;   /*文档内容类型 0 未知  1  word  2 excel  3 ppt */
	private String collectId;   /*收藏人id*/
	private String dmLink;   /*链接路径:存放归档信息的URL*/
	private String dmName;   /*文档管理_文档名称*/
	private String dmAlias;   /*文档在服务器中的名称已当前时间的long值名称*/
	private String dmLockStatus;   /*文档管理_文档锁定状态(0-未锁定;1-锁定)*/
	private String physicalPath;   /*文件物理地址*/
	private String dmDir;   /*文档所在目录位置*/
	private String dmType;   /*文档管理_文档类型(字典项)*/
	private String dmSuffix;   /*文档管理_文档历史后缀名*/


	private Integer dmInRecycle;   /*文档管理_回收站状态(0-否;1-是级联;2-不是级联)*/
	private String keyWords;   /*关键字(文档/知识)*/
	private String permissionValue;   /*针对知识管理共三位 111 复制  打印  下载*/
	private String author;   /*作者*/

	private String dmSize;   /*文档大小*/
	private String seq;   /*编号*/
	private String isValid;   /*是否过期 0未过期  1 过期*/
	private String piId;   /*流程ID或其它任何能唯一确定一条业务数据的值*/


	private String collectName; /*创建者*/

	private String docId;  //公文id
	private Integer model;   /*0 公共文档   1 我的文档*/
	private String owner;/*对应createdUser的创建者名称*/






	private String createUserName;
	private String createUserDeptName;
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






	public String getCollectName() {
		return collectName;
	}

	public void setCollectName(String collectName) {
		this.collectName = collectName;
	}



	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}


	public String getDmLink(){
		return dmLink;
	}

	public void setDmLink(String dmLink){
		this.dmLink = dmLink;
	}

	public String getDmName(){
		return dmName;
	}

	public void setDmName(String dmName){
		this.dmName = dmName;
	}

	public String getDmAlias(){
		return dmAlias;
	}

	public void setDmAlias(String dmAlias){
		this.dmAlias = dmAlias;
	}

	public String getDmLockStatus(){
		return dmLockStatus;
	}

	public void setDmLockStatus(String dmLockStatus){
		this.dmLockStatus = dmLockStatus;
	}

	public String getPhysicalPath(){
		return physicalPath;
	}

	public void setPhysicalPath(String physicalPath){
		this.physicalPath = physicalPath;
	}

	public String getDmDir(){
		return dmDir;
	}

	public void setDmDir(String dmDir){
		this.dmDir = dmDir;
	}

	public String getDmType(){
		return dmType;
	}

	public void setDmType(String dmType){
		this.dmType = dmType;
	}

	public String getDmSuffix(){
		return dmSuffix;
	}

	public void setDmSuffix(String dmSuffix){
		this.dmSuffix = dmSuffix;
	}







	public String getKeyWords(){
		return keyWords;
	}

	public void setKeyWords(String keyWords){
		this.keyWords = keyWords;
	}

	public String getPermissionValue(){
		return permissionValue;
	}

	public void setPermissionValue(String permissionValue){
		this.permissionValue = permissionValue;
	}

	public String getAuthor(){
		return author;
	}

	public void setAuthor(String author){
		this.author = author;
	}



	public String getDmSize(){
		return dmSize;
	}

	public void setDmSize(String dmSize){
		this.dmSize = dmSize;
	}

	public String getSeq(){
		return seq;
	}

	public void setSeq(String seq){
		this.seq = seq;
	}

	public String getIsValid(){
		return isValid;
	}

	public void setIsValid(String isValid){
		this.isValid = isValid;
	}

	public String getPiId(){
		return piId;
	}

	public void setPiId(String piId){
		this.piId = piId;
	}










	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Integer getDmInRecycle() {
		return dmInRecycle;
	}

	public void setDmInRecycle(Integer dmInRecycle) {
		this.dmInRecycle = dmInRecycle;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}