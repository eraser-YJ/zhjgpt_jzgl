package com.jc.sys.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubDepartment extends BaseBean{
	
	//protected transient final Logger log = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	public SubDepartment() {
	}	
	
	private String code;   /*部门编号*/
	private String name;   /*部门名称*/
	private String fullName;   /*组织全称*/
	//private Integer weight;   /*权重系数*/
	private String leaderId;   /*负责人*/
	private String parentId;   /*上级名称*/
	private String organizationId;   /*所属机构ID*/
	private Integer deptType;   /*标记是部门or机构节点
            ’0‘--部门
            ’1‘--机构*/
	private Integer queue;   /*部门排序*/
	private String deleteUser;   /*删除人ID*/
	private Date deleteDate;   /*删除时间*/
	private Date deleteDateBegin;   /*删除时间开始*/
	private Date deleteDateEnd;   /*删除时间结束*/
	private String isChecked = "1";		/*是否有权限操作	自定义属性 1:有权操作,0:无权操作*/
	private int userType = 1; // 1超管 2机构管理员 3普通用户 4无意义

	private String displayName; 	/*负责人	自定义属性*/
	private int userDelFlag;		/*负责人是否被删除标识*/
	private String parentName; 		/*上级部门	自定义属性*/
	private String deptIds;	/*根据部门ID集合行查询*/
	private String deptToken;	/*组织客户端token*/
	private List<SubUserBean> userBeanList = new ArrayList<SubUserBean>();/*部门下的人员	自定义属性*/
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getFullName(){
		return fullName;
	}
	
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
	

	public String getLeaderId(){
		return leaderId;
	}
	
	public void setLeaderId(String leaderId){
		this.leaderId = leaderId;
	}
	
	
	public String getParentId(){
		return parentId;
	}
	
	public void setParentId(String parentId){
		this.parentId = parentId;
	}
	
	
	public String getOrganizationId(){
		return organizationId;
	}
	
	public void setOrganizationId(String organizationId){
		this.organizationId = organizationId;
	}
	
	
	public Integer getDeptType(){
		return deptType;
	}
	
	public void setDeptType(Integer deptType){
		this.deptType = deptType;
	}
	
	
	public Integer getQueue(){
		return queue;
	}
	
	public void setQueue(Integer queue){
		this.queue = queue;
	}
	
	
	public String getDeleteUser(){
		return deleteUser;
	}
	
	public void setDeleteUser(String deleteUser){
		this.deleteUser = deleteUser;
	}
	
		
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getDeleteDate(){
		return deleteDate;
	}
	
	public void setDeleteDate(Date deleteDate){
		this.deleteDate = deleteDate;
	}
	
	public Date getDeleteDateBegin(){
		return deleteDateBegin;
	}
	
	public void setDeleteDateBegin(Date deleteDateBegin){
		this.deleteDateBegin = deleteDateBegin;
	}
	
	public Date getDeleteDateEnd(){
		return deleteDateEnd;
	}
	
	public void setDeleteDateEnd(Date deleteDateEnd){
		if (deleteDateEnd != null) {
			this.deleteDateEnd = DateUtils.fillTime(deleteDateEnd);
		}
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getUserDelFlag() {
		return userDelFlag;
	}

	public void setUserDelFlag(int userDelFlag) {
		this.userDelFlag = userDelFlag;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getDeptToken() {
		return deptToken;
	}

	public void setDeptToken(String deptToken) {
		this.deptToken = deptToken;
	}

	public void addUserBean(SubUserBean uBean) {
		userBeanList.add(uBean);
	}

	public List<SubUserBean> getUserBeanList() {
		return userBeanList;
	}

	public void setUserBeanList(List<SubUserBean> userBeanList) {
		this.userBeanList = userBeanList;
	}
}