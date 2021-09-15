package com.jc.system.security.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.excel.annotation.ExcelField;
import com.jc.excel.converter.Dic2FieldConverter;
import com.jc.excel.converter.Field2DicConverter;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.util.DeptCacheUtil;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * 用户实体bean
 * @author Administrator
 * @date 2020-06-30
 */
public class User extends BaseBean{
    protected transient final Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	@ExcelField(title = "用户编号", order = 1)
	private String code;
	@ExcelField(title = "姓名", order = 3)
	private String displayName;
	@ExcelField(title = "登录名称", order = 2)
	private String loginName;
	private String password;
	private String newPassword;
	@ExcelField(title = "性别", order = 4, dicName = "sex", parentCode = "user", readConverter = Dic2FieldConverter.class, writeConverter = Field2DicConverter.class)
	private String sex;
	@ExcelField(title = "用户性质", order = 5, dicName = "kind", parentCode = "user", readConverter = Dic2FieldConverter.class, writeConverter = Field2DicConverter.class)
	private String kind;
	private String dutyId;
	private String level;
	@ExcelField(title = "部门", order = 6)
	private String deptId;
	private String status;
	@ExcelField(title = "入职时间", order = 9)
	private Date entryDate;

	private String leaderId;
	private String deptLeader;
	private String chargeLeader;
	private String political;
	@ExcelField(title = "身份证号码", order = 7)
	private String cardNo;
	private Integer weight;
	@ExcelField(title = "用户排序", order = 8)
	private Integer orderNo;
	private String jobTitle;
	private String photo;
	private String mobile;
	private String officeTel;
	private String email;
	private Date entryPoliticalDate;
	@ExcelField(title = "出生日期", order = 10)
	private Date birthday;
	@ExcelField(title = "民族", order = 11, dicName = "ethnic", parentCode = "user", readConverter = Dic2FieldConverter.class, writeConverter = Field2DicConverter.class)
	private String ethnic;
	@ExcelField(title = "籍贯", order = 13)
	private String hometown;
	@ExcelField(title = "学历", order = 14, dicName = "degree", parentCode = "user", readConverter = Dic2FieldConverter.class, writeConverter = Field2DicConverter.class)
	private String degree;
	private String cername;
	@ExcelField(title = "婚姻状况", order = 12, dicName = "maritalStatus", parentCode = "user", readConverter = Dic2FieldConverter.class, writeConverter = Field2DicConverter.class)
	private String maritalStatus;
	private Integer isAdmin;
	private String payCardNo;
	private String cardBank;
	private String cardName;
	private Date lastLonginDate;
	private Integer wrongCount;
	private Date lockStartDate;
	private String officeAddress;
	private Integer isDriver;
	private Integer isLeader;
	private String answer;
	private String question;
	private String openCale;
	private String isCheck;
	private Integer lockType;
	private String groupTel;
	private int modifyPwdFlag;
	private String theme;
	private String fileids;
	private String userStatus;
	private String userLevel;

	/**机构ID*/
	private String orgId;
	/**机构名称*/
	private String orgName;
	/**部门查询条件*/
	private String deptIds;
	private String deptName;
	private String userType;
	private Integer deptWeight;
	/**其他部门职位*/
	private List<SysOtherDepts> otherDepts;
	/**角色*/
	private List<SysUserRole> sysUserRole;
	/**管理员部门*/
	private List<AdminSide> adminSide;
	/**用户树属性*/
	private String name;
	private Long parentId;
	private int rowNo;
	/**兼职部门标志，用于人员选择树数据标志，0-正常用户 1-兼职部门对应用户对象*/
	private int otherDeptFlag;
	private int isSw;
	private int isUsing;
	private String levels;
	private String leaderIdValue;
	private String deptLeaderValue;
	private String chargeLeaderValue;
	/**扩展用户样式*/
	private String color;
	private String font;
	/**用户角色id集合*/
	private String roleids;
	private String attachId;
	private String keyCode;
	/**机构的id转code查询条件*/
	private String deptIdToCode;
	/**资源库id*/
	private String resourceId;
	/**人员企业类型*/
	private String personCompanyType;
	public String getDeptIdToCode() {
		return deptIdToCode;
	}
	public void setDeptIdToCode(String deptIdToCode) {
		this.deptIdToCode = deptIdToCode;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public String getRoleids() {
		return roleids;
	}
	public void setRoleids(String roleids) {
		this.roleids = roleids;
	}
	/**
	 * 判断是否是超级管理员
	 * @return
	 */
	public boolean getIsSystemAdmin(){
		if(GlobalContext.ADMIN_NAME.equals(this.loginName)){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否是系统管理员
	 */
	public boolean getIsSystemManager(){
		if(GlobalContext.MANAGER_NAME.equals(this.loginName)){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否是安全保密员
	 * @return
	 */
	public boolean getIsSystemSecurity(){
		if(GlobalContext.SECURITY_NAME.equals(this.loginName)){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否是安全审计员
	 * @return
	 */
	public boolean getIsSystemAudit(){
		if(GlobalContext.AUDIT_NAME.equals(this.loginName)){
			return true;
		} else {
			return false;
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public String getSexValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("sex","user", this.getSex());
		return dic == null ? "" : dic.getValue();
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getDutyId() {
		return dutyId;
	}
	public String getDutyIdValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("dutyId", "user",this.getDutyId());
		return dic == null ? "" : dic.getValue();
	}
	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLevelValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		return dicManager.getDic("level","user", level) == null ? "" : dicManager.getDic("level","user", level).getValue();
	}
	public String getExtStr1Value() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("secret_type", "send_manage",this.getExtStr1());
		return dic == null ? "" : dic.getValue();
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getStatus() {
		return status;
	}
	public String getStatusValue() {
		return this.getStatus() == null ? "" : "status_0".equals(this.status) ? "启用" : "status_1".equals(this.status) ? "禁用" : "status_2".equals(this.status) ? "锁定" : "status_3".equals(this.status) ? "删除" : "";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getDeptLeader() {
		return deptLeader;
	}
	public void setDeptLeader(String deptLeader) {
		this.deptLeader = deptLeader;
	}
	public String getChargeLeader() {
		return chargeLeader;
	}
	public void setChargeLeader(String chargeLeader) {
		this.chargeLeader = chargeLeader;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Override
    public Integer getWeight() {
		return weight;
	}
	@Override
    public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public String getJobTitleValue(){
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("jobTitle","user", jobTitle);
		return dic == null ? "" : dic.getValue();
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getEntryPoliticalDate() {
		return entryPoliticalDate;
	}
	public void setEntryPoliticalDate(Date entryPoliticalDate) {
		this.entryPoliticalDate = entryPoliticalDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getEthnic() {
		return ethnic;
	}
	public String getEthnicValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("ethnic","user", this.getEthnic());
		return dic == null ? "" : dic.getValue();
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getDegree() {
		return degree;
	}
	public String getDegreeValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("degree","user", this.getDegree());
		return dic == null ? "" : dic.getValue();
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getCername() {
		return cername;
	}
	public void setCername(String cername) {
		this.cername = cername;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public String getMaritalStatusValue() {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic("maritalStatus","user", this.getMaritalStatus());
		return dic == null ? "" : dic.getValue();
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public Integer getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getPayCardNo() {
		return payCardNo;
	}
	public void setPayCardNo(String payCardNo) {
		this.payCardNo = payCardNo;
	}
	public String getCardBank() {
		return cardBank;
	}
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLastLonginDate() {
		return lastLonginDate;
	}
	public void setLastLonginDate(Date lastLonginDate) {
		this.lastLonginDate = lastLonginDate;
	}
	public Integer getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLockStartDate() {
		return lockStartDate;
	}
	public void setLockStartDate(Date lockStartDate) {
		this.lockStartDate = lockStartDate;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public Integer getIsDriver() {
		return isDriver;
	}
	public void setIsDriver(Integer isDriver) {
		this.isDriver = isDriver;
	}
	public Integer getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getDeptName() {
		if(getDeptId() == null){
			return deptName;
		}
		try {
			Department department = DeptCacheUtil.getDeptById(getDeptId());
			if(department != null){
				return department.getName();
			} else {
				return "";
			}
		} catch (CustomException e) {
			logger.error(e);
		}
		return null;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getOpenCale() {
		return openCale;
	}
	public void setOpenCale(String openCale) {
		this.openCale = openCale;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public List<SysUserRole> getSysUserRole() {
		return sysUserRole;
	}
	public void setSysUserRole(List<SysUserRole> sysUserRole) {
		this.sysUserRole = sysUserRole;
	}
	public List<AdminSide> getAdminSide() {
		return adminSide;
	}
	public void setAdminSide(List<AdminSide> adminSide) {
		this.adminSide = adminSide;
	}
	public List<SysOtherDepts> getOtherDepts() {
		return otherDepts;
	}
	public void setOtherDepts(List<SysOtherDepts> otherDepts) {
		this.otherDepts = otherDepts;
	}
	public String getOrgId() {
		if(getDeptId() == null){
			return orgId;
		}
		try {
			Department department = DeptCacheUtil.getDeptById(getDeptId());
			if(department != null){
				return department.getOrgId();
			} else {
				return "-1";
			}
		} catch (CustomException e) {
			logger.error(e,e);
		}
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getLockType() {
		return lockType;
	}
	public void setLockType(Integer lockType) {
		this.lockType = lockType;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupTel() {
		return groupTel;
	}
	public void setGroupTel(String groupTel) {
		this.groupTel = groupTel;
	}
	public int getModifyPwdFlag() {
		return modifyPwdFlag;
	}
	public void setModifyPwdFlag(int modifyPwdFlag) {
		this.modifyPwdFlag = modifyPwdFlag;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String getFileids() {
		return fileids;
	}
	public void setFileids(String fileids) {
		this.fileids = fileids;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getOtherDeptFlag() {
		return otherDeptFlag;
	}
	public void setOtherDeptFlag(int otherDeptFlag) {
		this.otherDeptFlag = otherDeptFlag;
	}
	public int getIsSw() {
		return isSw;
	}
	public void setIsSw(int isSw) {
		this.isSw = isSw;
	}
	public int getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(int isUsing) {
		this.isUsing = isUsing;
	}
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getLeaderIdValue() {
		return leaderIdValue;
	}
	public void setLeaderIdValue(String leaderIdValue) {
		this.leaderIdValue = leaderIdValue;
	}
	public String getDeptLeaderValue() {
		return deptLeaderValue;
	}
	public void setDeptLeaderValue(String deptLeaderValue) {
		this.deptLeaderValue = deptLeaderValue;
	}
	public String getChargeLeaderValue() {
		return chargeLeaderValue;
	}
	public void setChargeLeaderValue(String chargeLeaderValue) {
		this.chargeLeaderValue = chargeLeaderValue;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public Integer getDeptWeight() {
		return deptWeight;
	}
	public void setDeptWeight(Integer deptWeight) {
		this.deptWeight = deptWeight;
	}
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getPersonCompanyType() {
		return personCompanyType;
	}

	public void setPersonCompanyType(String personCompanyType) {
		this.personCompanyType = personCompanyType;
	}
}