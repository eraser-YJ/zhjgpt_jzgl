package com.jc.archive.domain;

import java.util.List;

import com.jc.foundation.domain.BaseBean;


/**
 * @title  GOA2.0源代码
 * @description OA_文档管理/知识管理_文件夹信息 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Folder extends BaseBean implements Cloneable{
	private static final long serialVersionUID = 1L;
	private String folderType;   /*文件夹类别(0-公共文档;1-我的文档;2-收藏夹;3-知识管理)*/
	private String nodeType;   /*知识管理节点类型，  0第一层 1 下层*/
	private String parentFolderId;   /*父级文件夹ID(根目录默认值为"0")*/
	private String folderName;   /*文件夹名称*/
	private String folderPath;   /*文件夹路径*/
	private Integer dmInRecycle;   /*文档管理_回收站状态(0-否;1-是级联;2-不是级联)*/
	private String kmManagerId;   /*知识管理_模块管理员ID*/
	private Integer kmSort;   /*知识管理_序号*/
	private Integer kmDeadline;   /*知识管理_最新知识时限天数*/
	private Integer weight;   /**/
	private Integer kmStandardCount;   /*知识管理_知识标准条数*/
	private String kmAppFlag;   /*知识管理_是否审批(0-否,1-是)*/
	private Integer kmStandardTime;   /*知识管理_知识标准时限*/
	private String permissionValue;   /*针对知识管理共三位 111 复制  打印  下载*/
	
	/**
	 * goa1.0a数据主键id
	 */
	private String goa1Id;
	
	/**
	 * goa1.0数据父id
	 */
	private String goa1ParentId;
	private String currentUserId;
	private String currentUserDeptId;
	
	private List<Folder> subdirs;/*当前目录的直属下级目录*/
	private List<Document> documents;/*当前目录下的文档*/
	
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
	
	public String getGoa1Id() {
		return goa1Id;
	}

	public void setGoa1Id(String goa1Id) {
		this.goa1Id = goa1Id;
	}

	public String getGoa1ParentId() {
		return goa1ParentId;
	}

	public void setGoa1ParentId(String goa1ParentId) {
		this.goa1ParentId = goa1ParentId;
	}

	public Integer getModel() {
		return model;
	}

	public String getCurrentUserDeptId() {
		return currentUserDeptId;
	}

	public void setCurrentUserDeptId(String currentUserDeptId) {
		this.currentUserDeptId = currentUserDeptId;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public List<Folder> getSubdirs() {
		return subdirs;
	}



	public void setSubdirs(List<Folder> subdirs) {
		this.subdirs = subdirs;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
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

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public String getKmManagerId() {
		return kmManagerId;
	}

	public void setKmManagerId(String kmManagerId) {
		this.kmManagerId = kmManagerId;
	}

	public Integer getKmSort(){
		return kmSort;
	}
	
	public void setKmSort(Integer kmSort){
		this.kmSort = kmSort;
	}
	
	public Integer getKmDeadline(){
		return kmDeadline;
	}
	
	public void setKmDeadline(Integer kmDeadline){
		this.kmDeadline = kmDeadline;
	}
	
	@Override
    public Integer getWeight(){
		return weight;
	}
	
	@Override
	public void setWeight(Integer weight){
		this.weight = weight;
	}
	
	public Integer getKmStandardCount(){
		return kmStandardCount;
	}
	
	public void setKmStandardCount(Integer kmStandardCount){
		this.kmStandardCount = kmStandardCount;
	}
	
	public String getKmAppFlag(){
		return kmAppFlag;
	}
	
	public void setKmAppFlag(String kmAppFlag){
		this.kmAppFlag = kmAppFlag;
	}
	
	public Integer getKmStandardTime(){
		return kmStandardTime;
	}
	
	public void setKmStandardTime(Integer kmStandardTime){
		this.kmStandardTime = kmStandardTime;
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
	public Folder clone() throws CloneNotSupportedException {
		Folder folder=(Folder)super.clone();
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

}