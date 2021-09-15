package com.jc.system.security.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.security.util.DeptCacheUtil;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserRecycle extends BaseBean{
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	/**用户编号*/
	private String code;
	/**姓名*/
	private String displayName;
	/**用户登录名*/
	private String loginName;
	/**密码*/
	private String password;
	/**用户性别 '0'--男 ‘1’--女 ‘2’--未知*/
	private String sex;
	/**‘0’--正式 ‘1‘--临时 ‘2’--合同*/
	private String kind;
	/**用户职务 字典表 */
	private String dutyId;
	/**用户级别*/
	private String level;
	/**用户所在部门*/
	private String deptId;
	/**组织状态 ’0‘--启用 ’1‘--禁用 ’2’--锁定 ‘3’--删除*/
	private String status;
	/**入职时间*/
	private Date entryDate;
	/**入职时间开始*/
	private Date entryDateBegin;
	/**入职时间结束*/
	private Date entryDateEnd;
	/**用户直接领导*/
	private String leaderId;
	/**部门领导*/
	private String deptLeader;
	/**分管领导*/
	private String chargeLeader;
	/**用户政治面貌 数据字典 */
	private String political;
	/**身份证号码*/
	private String cardNo;
	/**安全系数*/
	private Integer weight;
	/**排序号*/
	private Integer orderNo;
	/**用户职称 字典表 */
	private String jobTitle;
	/**用户手机号码*/
	private String mobile;
	/**办公电话*/
	private String officeTel;
	/**集团号码*/
	private String groupTel;
	/**用户邮箱*/
	private String email;
	/**用户入党日期*/
	private Date entryPoliticalDate;
	/**用户入党日期开始*/
	private Date entryPoliticalDateBegin;
	/**用户入党日期结束*/
	private Date entryPoliticalDateEnd;
	/**出生日期*/
	private Date birthday;
	/**出生日期开始*/
	private Date birthdayBegin;
	/**出生日期结束*/
	private Date birthdayEnd;
	/**用户民族 数据字典 */
	private String ethnic;
	/**用户籍贯*/
	private String hometown;
	/**用户学历 字典表*/
	private String degree;
	/**用户证书信息*/
	private String cername;
	/**婚姻状况 字典表*/
	private String maritalStatus;
	/**银行卡号*/
	private String payCardNo;
	/**开户银行*/
	private String cardBank;
	/**开户人姓名*/
	private String cardName;
	/**最后登录时间*/
	private Date lastLonginDate;
	/**最后登录时间开始*/
	private Date lastLonginDateBegin;
	/**最后登录时间结束*/
	private Date lastLonginDateEnd;
	/**错误登录次数*/
	private Integer wrongCount;
	/**开始锁定时间*/
	private Date lockStartDate;
	/**开始锁定时间开始*/
	private Date lockStartDateBegin;
	/**开始锁定时间结束*/
	private Date lockStartDateEnd;
	/**办公地点*/
	private String officeAddress;
	/**是否是司机 ‘0’--否 ‘1’--是*/
	private String isDriver;
	/**是否是领导 ‘0’--否 '1'--是*/
	private String isLeader;
	/**是否考勤 0-不考勤 1-考勤*/
	private String isCheck;
	/**是否公开日程  0-不公开，1-公开*/
	private String openCale;
	/**密码提示问题*/
	private String question;
	/**密码答案*/
	private String answer;
	/**照片*/
	private String photo;
	/**是否是管理员*/
	private Integer isAdmin;
	/**锁定方式(0-系统锁定 1-管理员锁定）*/
	private String lockType;
	/**强制修改标志*/
	private Integer modifyPwdFlag;
	/**样式主题*/
	private String theme;
	private String deptName;
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
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex = sex;
	}
	public String getKind(){
		return kind;
	}
	public void setKind(String kind){
		this.kind = kind;
	}
	public String getDutyId(){
		return dutyId;
	}
	public void setDutyId(String dutyId){
		this.dutyId = dutyId;
	}
	public String getLevel(){
		return level;
	}
	public void setLevel(String level){
		this.level = level;
	}
	public String getDeptId(){
		return deptId;
	}
	public void setDeptId(String deptId){
		this.deptId = deptId;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getEntryDate(){
		return entryDate;
	}
	public void setEntryDate(Date entryDate){
		this.entryDate = entryDate;
	}
	public Date getEntryDateBegin(){
		return entryDateBegin;
	}
	public void setEntryDateBegin(Date entryDateBegin){
		this.entryDateBegin = entryDateBegin;
	}
	public Date getEntryDateEnd(){
		return entryDateEnd;
	}
	public void setEntryDateEnd(Date entryDateEnd){
		this.entryDateEnd = DateUtils.fillTime(entryDateEnd);
	}
	public String getLeaderId(){
		return leaderId;
	}
	public void setLeaderId(String leaderId){
		this.leaderId = leaderId;
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
	public String getPolitical(){
		return political;
	}
	public void setPolitical(String political){
		this.political = political;
	}
	public String getCardNo(){
		return cardNo;
	}
	public void setCardNo(String cardNo){
		this.cardNo = cardNo;
	}
	@Override
    public Integer getWeight(){
		return weight;
	}
	@Override
    public void setWeight(Integer weight){
		this.weight = weight;
	}
	public Integer getOrderNo(){
		return orderNo;
	}
	public void setOrderNo(Integer orderNo){
		this.orderNo = orderNo;
	}
	public String getJobTitle(){
		return jobTitle;
	}
	public void setJobTitle(String jobTitle){
		this.jobTitle = jobTitle;
	}
	public String getMobile(){
		return mobile;
	}
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	public String getOfficeTel(){
		return officeTel;
	}
	public void setOfficeTel(String officeTel){
		this.officeTel = officeTel;
	}
	public String getGroupTel(){
		return groupTel;
	}
	public void setGroupTel(String groupTel){
		this.groupTel = groupTel;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getEntryPoliticalDate(){
		return entryPoliticalDate;
	}
	public void setEntryPoliticalDate(Date entryPoliticalDate){
		this.entryPoliticalDate = entryPoliticalDate;
	}
	public Date getEntryPoliticalDateBegin(){
		return entryPoliticalDateBegin;
	}
	public void setEntryPoliticalDateBegin(Date entryPoliticalDateBegin){
		this.entryPoliticalDateBegin = entryPoliticalDateBegin;
	}
	public Date getEntryPoliticalDateEnd(){
		return entryPoliticalDateEnd;
	}
	public void setEntryPoliticalDateEnd(Date entryPoliticalDateEnd){
		this.entryPoliticalDateEnd = DateUtils.fillTime(entryPoliticalDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getBirthday(){
		return birthday;
	}
	public void setBirthday(Date birthday){
		this.birthday = birthday;
	}
	public Date getBirthdayBegin(){
		return birthdayBegin;
	}
	public void setBirthdayBegin(Date birthdayBegin){
		this.birthdayBegin = birthdayBegin;
	}
	public Date getBirthdayEnd(){
		return birthdayEnd;
	}
	public void setBirthdayEnd(Date birthdayEnd){
		this.birthdayEnd = DateUtils.fillTime(birthdayEnd);
	}
	public String getEthnic(){
		return ethnic;
	}
	public void setEthnic(String ethnic){
		this.ethnic = ethnic;
	}
	public String getHometown(){
		return hometown;
	}
	public void setHometown(String hometown){
		this.hometown = hometown;
	}
	public String getDegree(){
		return degree;
	}
	public void setDegree(String degree){
		this.degree = degree;
	}
	public String getCername(){
		return cername;
	}
	public void setCername(String cername){
		this.cername = cername;
	}
	public String getMaritalStatus(){
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus){
		this.maritalStatus = maritalStatus;
	}
	public String getPayCardNo(){
		return payCardNo;
	}
	public void setPayCardNo(String payCardNo){
		this.payCardNo = payCardNo;
	}
	public String getCardBank(){
		return cardBank;
	}
	public void setCardBank(String cardBank){
		this.cardBank = cardBank;
	}
	public String getCardName(){
		return cardName;
	}
	public void setCardName(String cardName){
		this.cardName = cardName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLastLonginDate(){
		return lastLonginDate;
	}
	public void setLastLonginDate(Date lastLonginDate){
		this.lastLonginDate = lastLonginDate;
	}
	public Date getLastLonginDateBegin(){
		return lastLonginDateBegin;
	}
	public void setLastLonginDateBegin(Date lastLonginDateBegin){
		this.lastLonginDateBegin = lastLonginDateBegin;
	}
	public Date getLastLonginDateEnd(){
		return lastLonginDateEnd;
	}
	public void setLastLonginDateEnd(Date lastLonginDateEnd){
		this.lastLonginDateEnd = DateUtils.fillTime(lastLonginDateEnd);
	}
	public Integer getWrongCount(){
		return wrongCount;
	}
	public void setWrongCount(Integer wrongCount){
		this.wrongCount = wrongCount;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getLockStartDate(){
		return lockStartDate;
	}
	public void setLockStartDate(Date lockStartDate){
		this.lockStartDate = lockStartDate;
	}
	public Date getLockStartDateBegin(){
		return lockStartDateBegin;
	}
	public void setLockStartDateBegin(Date lockStartDateBegin){
		this.lockStartDateBegin = lockStartDateBegin;
	}
	public Date getLockStartDateEnd(){
		return lockStartDateEnd;
	}
	public void setLockStartDateEnd(Date lockStartDateEnd){
		this.lockStartDateEnd = DateUtils.fillTime(lockStartDateEnd);
	}
	public String getOfficeAddress(){
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress){
		this.officeAddress = officeAddress;
	}
	public String getIsDriver(){
		return isDriver;
	}
	public void setIsDriver(String isDriver){
		this.isDriver = isDriver;
	}
	public String getIsLeader(){
		return isLeader;
	}
	public void setIsLeader(String isLeader){
		this.isLeader = isLeader;
	}
	public String getIsCheck(){
		return isCheck;
	}
	public void setIsCheck(String isCheck){
		this.isCheck = isCheck;
	}
	public String getOpenCale(){
		return openCale;
	}
	public void setOpenCale(String openCale){
		this.openCale = openCale;
	}
	public String getQuestion(){
		return question;
	}
	public void setQuestion(String question){
		this.question = question;
	}
	public String getAnswer(){
		return answer;
	}
	public void setAnswer(String answer){
		this.answer = answer;
	}
	public String getPhoto(){
		return photo;
	}
	public void setPhoto(String photo){
		this.photo = photo;
	}
	public Integer getIsAdmin(){
		return isAdmin;
	}
	public void setIsAdmin(Integer isAdmin){
		this.isAdmin = isAdmin;
	}
	public String getLockType(){
		return lockType;
	}
	public void setLockType(String lockType){
		this.lockType = lockType;
	}
	public Integer getModifyPwdFlag(){
		return modifyPwdFlag;
	}
	public void setModifyPwdFlag(Integer modifyPwdFlag){
		this.modifyPwdFlag = modifyPwdFlag;
	}
	public String getTheme(){
		return theme;
	}
	public void setTheme(String theme){
		this.theme = theme;
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
}