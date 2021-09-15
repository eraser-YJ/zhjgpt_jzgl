package com.jc.archive.domain;

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

public class Permission extends BaseBean{
	private static final long serialVersionUID = 1L;
	private String folderId;   /*文件夹ID*/
	private String category;   /*0 人 1 部门*/
	private String userId;   /*人员id*/
	private String deptId;   /*部门id*/
	private String orgId;   /*组织id*/
	private String permissionValue;   /*针对文档管理
            共7位
            
            1新建上传
            2在线编辑，版本更新、重命名、锁定
            3 删除、移动（剪切到）
            4 下载
            5 复制到
            6 浏览
            7 收藏
            */
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private Integer weight;   /**/
	
	private String permView;
	private String permNewUpDown;
	private String permEdit;
	private String permDelete;
	private String permCopyPaste;
	private String permRename;
	private String permCollect;
	private String permVersion;
	private String permHistory;
	private String permRelate;
	
	public String getPermView() {
		return permView;
	}

	public void setPermView(String permView) {
		this.permView = permView;
	}

	public String getPermNewUpDown() {
		return permNewUpDown;
	}

	public void setPermNewUpDown(String permNewUpDown) {
		this.permNewUpDown = permNewUpDown;
	}

	public String getPermEdit() {
		return permEdit;
	}

	public void setPermEdit(String permEdit) {
		this.permEdit = permEdit;
	}

	public String getPermDelete() {
		return permDelete;
	}

	public void setPermDelete(String permDelete) {
		this.permDelete = permDelete;
	}

	public String getPermCopyPaste() {
		return permCopyPaste;
	}

	public void setPermCopyPaste(String permCopyPaste) {
		this.permCopyPaste = permCopyPaste;
	}

	public String getPermRename() {
		return permRename;
	}

	public void setPermRename(String permRename) {
		this.permRename = permRename;
	}

	public String getPermCollect() {
		return permCollect;
	}

	public void setPermCollect(String permCollect) {
		this.permCollect = permCollect;
	}

	public String getPermVersion() {
		return permVersion;
	}

	public void setPermVersion(String permVersion) {
		this.permVersion = permVersion;
	}

	public String getPermHistory() {
		return permHistory;
	}

	public void setPermHistory(String permHistory) {
		this.permHistory = permHistory;
	}

	public String getPermRelate() {
		return permRelate;
	}

	public void setPermRelate(String permRelate) {
		this.permRelate = permRelate;
	}


	
	public String getCategory(){
		return category;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	/*
	public Long getUserId(){
		return userId;
	}
	
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	public Long getDeptId(){
		return deptId;
	}
	
	public void setDeptId(Long deptId){
		this.deptId = deptId;
	}
	*/
	public String getPermissionValue(){
		return permissionValue;
	}
	
	public void setPermissionValue(String permissionValue){
		this.permissionValue = permissionValue;
	}
	
	/*public Integer getWeight(){
		return weight;
	}
	
	public void setWeight(Integer weight){
		this.weight = weight;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}*/

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Override
	public Integer getWeight() {
		return weight;
	}

	@Override
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}