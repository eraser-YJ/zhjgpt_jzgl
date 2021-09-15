package com.jc.archive.domain;

import com.jc.foundation.domain.BaseBean;

import java.util.Date;
import java.util.List;


/**
 * @title  GOA2.0源代码
 * @description OA_文档管理/知识管理_文件夹信息 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Folder4Rtx implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String folderType;   /*文件夹类别(0-公共文档;1-我的文档;2-收藏夹;3-知识管理)*/
	private String nodeType;   /*知识管理节点类型，  0第一层 1 下层*/
	private String parentFolderId;   /*父级文件夹ID(根目录默认值为"0")*/
	private String folderName;   /*文件夹名称*/
	private String folderPath;   /*文件夹路径*/
	private Integer dmInRecycle;   /*文档管理_回收站状态(0-否;1-是级联;2-不是级联)*/



	private String permissionValue;   /*针对知识管理共三位 111 复制  打印  下载*/

	private String deptName;

	public String getDeptName(){ return deptName;}
	public void setDeptName(String deptName){ this.deptName = deptName;}
	
	/**
	 * goa1.0数据父id
	 */
	private String goa1ParentId;
	private Long currentUserId;
	private Long currentUserDeptId;
	
	private List<Folder4Rtx> subdirs;/*当前目录的直属下级目录*/
	private List<Document4Rtx> documents;/*当前目录下的文档*/
	
	private String owner;/*对应createdUser的创建者名称*/
	private String fileids;/*上传文档的附件ID*/
	
	private boolean permView;
	private boolean permNewUpDown;
	private boolean permEdit;
	private boolean permDelete;
	private boolean permCopyPaste;
	private boolean permRename;
	private boolean permCollect;
	private boolean permVersion;
	private boolean permHistory;
	private boolean permRelate;
	private Integer model;   /*0 公共文档   1 我的文档*/
	private String oldFolderPath;   /*旧文件夹路径*/
	
	private Date modifyDate;



	public Integer getModel() {
		return model;
	}

	public Long getCurrentUserDeptId() {
		return currentUserDeptId;
	}

	public void setCurrentUserDeptId(Long currentUserDeptId) {
		this.currentUserDeptId = currentUserDeptId;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public List<Folder4Rtx> getSubdirs() {
		return subdirs;
	}

	public Long getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Long currentUserId) {
		this.currentUserId = currentUserId;
	}

	public void setSubdirs(List<Folder4Rtx> subdirs) {
		this.subdirs = subdirs;
	}

	public List<Document4Rtx> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document4Rtx> documents) {
		this.documents = documents;
	}

	public String getFolderType(){
		return folderType;
	}
	
	public void setFolderType(String folderType){
		this.folderType = folderType;
	}
	
	public String getNodeType(){
		return nodeType;
	}
	
	public void setNodeType(String nodeType){
		this.nodeType = nodeType;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public String getFolderName(){
		return folderName;
	}
	
	public void setFolderName(String folderName){
		this.folderName = folderName;
	}
	
	public String getFolderPath(){
		return folderPath;
	}
	
	public void setFolderPath(String folderPath){
		this.folderPath = folderPath;
	}
	
	public Integer getDmInRecycle(){
		return dmInRecycle;
	}
	
	public void setDmInRecycle(Integer dmInRecycle){
		this.dmInRecycle = dmInRecycle;
	}
	

	

	


	

	

	

	
	public String getPermissionValue(){
		return permissionValue;
	}
	
	public void setPermissionValue(String permissionValue){
		this.permissionValue = permissionValue;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFileids() {
		return fileids;
	}

	public void setFileids(String fileids) {
		this.fileids = fileids;
	}
	
	/**
	 * 克隆出一个Id为空的对象
	 */
	@Override
	public Folder4Rtx clone() throws CloneNotSupportedException {
		Folder4Rtx folder=(Folder4Rtx)super.clone();
		folder.setId(null);
//		folder.setFolderType(this.getFolderType());
//		folder.setNodeType(this.getNodeType());
//		folder.setParentFolderId(folder.getParentFolderId());
//		folder.setFolderName(this.getFolderName());
//		folder.setFolderPath(this.getFolderPath());
//		folder.setDmInRecycle(this.getDmInRecycle());
//		folder.setKmManagerId(this.getKmManagerId());
//		folder.setKmSort(this.getKmSort());
//		folder.setKmDeadline(this.getKmDeadline());
//		folder.setWeight(this.getWeight());   /**/
//		folder.setKmStandardCount(this.getKmStandardCount());   /*知识管理_知识标准条数*/
//		folder.setKmAppFlag(this.getKmAppFlag());   /*知识管理_是否审批(0-否,1-是)*/
//		folder.setKmStandardTime(this.getKmStandardTime());   /*知识管理_知识标准时限*/
//		folder.setPermissionValue(this.getPermissionValue());  
		return folder;
	}

	public boolean isPermView() {
		return permView;
	}

	public void setPermView(boolean permView) {
		this.permView = permView;
	}

	public boolean isPermNewUpDown() {
		return permNewUpDown;
	}

	public void setPermNewUpDown(boolean permNewUpDown) {
		this.permNewUpDown = permNewUpDown;
	}

	public boolean isPermEdit() {
		return permEdit;
	}

	public void setPermEdit(boolean permEdit) {
		this.permEdit = permEdit;
	}

	public boolean isPermDelete() {
		return permDelete;
	}

	public void setPermDelete(boolean permDelete) {
		this.permDelete = permDelete;
	}

	public boolean isPermCopyPaste() {
		return permCopyPaste;
	}

	public void setPermCopyPaste(boolean permCopyPaste) {
		this.permCopyPaste = permCopyPaste;
	}

	public boolean isPermRename() {
		return permRename;
	}

	public void setPermRename(boolean permRename) {
		this.permRename = permRename;
	}

	public boolean isPermCollect() {
		return permCollect;
	}

	public void setPermCollect(boolean permCollect) {
		this.permCollect = permCollect;
	}

	public boolean isPermVersion() {
		return permVersion;
	}

	public void setPermVersion(boolean permVersion) {
		this.permVersion = permVersion;
	}

	public boolean isPermHistory() {
		return permHistory;
	}

	public void setPermHistory(boolean permHistory) {
		this.permHistory = permHistory;
	}

	public boolean isPermRelate() {
		return permRelate;
	}

	public void setPermRelate(boolean permRelate) {
		this.permRelate = permRelate;
	}

	public String getOldFolderPath() {
		return oldFolderPath;
	}

	public void setOldFolderPath(String oldFolderPath) {
		this.oldFolderPath = oldFolderPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}