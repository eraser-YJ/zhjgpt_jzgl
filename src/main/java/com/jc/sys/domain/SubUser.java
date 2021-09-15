package com.jc.sys.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.apache.log4j.Logger;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubUser extends BaseBean{
	
//	protected transient final Logger log = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	public SubUser() {
	}	
	
	private String code;   /*用户编号*/
	private String displayName;   /*姓名*/
	private String loginName;   /*用户登录名*/
	private String sex;   /*用户性别*/
	private String dutyId;   /*用户职务*/
	private String deptId;   /*所在部门*/
	private String status;   /*组织状态
            ’0‘--启用 
            ’1‘--禁用 
            ’2’--锁定 
            ‘3’--删除*/
	private String leaderId;   /*直接领导*/
	private String deptLeader;   /*部门领导*/
	private String chargeLeader;   /*分管领导*/
//	private Integer weight;   /*权重系数*/
	private Integer orderNo;   /*排序号*/
	private String theme;   /*样式主题*/
	private String deptName;
	private String deptIds;


	//树控件弹出显示字段
	private String dutyIdValue;  /*用户职务*/
	private String mobile; /**/
	private String officeTel; /**/
	private String email; /**/

	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	
	public String getDisplayName(){
		return displayName;
	}
	
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	public String getLoginName(){
		return loginName;
	}
	
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
	
	
	public String getSex(){
		return sex;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSexValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		return dicManager.getDic("sex","user", this.getSex()) == null ? "" : dicManager.getDic("sex","user", this.getSex()).getValue();
	}
	
	public String getDutyId(){
		return dutyId;
	}
	
	public void setDutyId(String dutyId){
		this.dutyId = dutyId;
	}
	
	
	public String getDeptId(){
		return deptId;
	}
	
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	
	public String getLeaderId(){
		return leaderId;
	}
	
	public void setLeaderId(String leaderId){
		this.leaderId = leaderId;
	}
	
	public String getLeaderIdValue(){
		if(leaderId != null && !"".equals(leaderId)){
			User user = UserUtils.getUser(leaderId);
			if(user == null){
				return "";
			}
			return user.getDisplayName();
		} else {
			return "";
		}
	}	
	public String getDeptLeader(){
		return deptLeader;
	}
	
	public void setDeptLeader(String deptLeader){
		this.deptLeader = deptLeader;
	}
	
	
	public String getChargeLeader(){
		return chargeLeader;
	}
	
	public void setChargeLeader(String chargeLeader){
		this.chargeLeader = chargeLeader;
	}
	
	public Integer getOrderNo(){
		return orderNo;
	}
	
	public void setOrderNo(Integer orderNo){
		this.orderNo = orderNo;
	}
	
	
	public String getTheme(){
		return theme;
	}
	
	public void setTheme(String theme){
		this.theme = theme;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getDutyIdValue() {
		return dutyIdValue;
	}

	public void setDutyIdValue(String dutyIdValue) {
		this.dutyIdValue = dutyIdValue;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}