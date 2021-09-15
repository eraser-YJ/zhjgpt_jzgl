package com.jc.system.security.domain.validator;

import com.jc.system.security.domain.UserRecycle;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserRecycleValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final String JC_SYS_010 = "JC_SYS_010";
	
	@Override
    public boolean supports(Class<?> arg0) {
		return UserRecycle.class.equals(arg0);
	}
	
	@Override
    public void validate(Object arg0, Errors arg1) {
		UserRecycle v =  (UserRecycle)arg0;
		if(v.getCode()!=null&&v.getCode().length()>64){
			arg1.reject("code", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getDisplayName()!=null&&v.getDisplayName().length()>50){
			arg1.reject("displayName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getLoginName()==null){
			arg1.reject("loginName", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getLoginName()!=null&&v.getLoginName().length()>50){
			arg1.reject("loginName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getPassword()==null){
			arg1.reject("password", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getPassword()!=null&&v.getPassword().length()>64){
			arg1.reject("password", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getSex()!=null&&v.getSex().length()>32){
			arg1.reject("sex", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getKind()!=null&&v.getKind().length()>32){
			arg1.reject("kind", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getDutyId()!=null&&v.getDutyId().length()>32){
			arg1.reject("dutyId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getLevel()!=null&&v.getLevel().length()>32){
			arg1.reject("level", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getDeptId()==null){
			arg1.reject("deptId", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getStatus()!=null&&v.getStatus().length()>32){
			arg1.reject("status", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getPolitical()!=null&&v.getPolitical().length()>32){
			arg1.reject("political", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getCardNo()!=null&&v.getCardNo().length()>20){
			arg1.reject("cardNo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"20"}));
		}
		if(v.getOrderNo()==null){
			arg1.reject("orderNo", MessageUtils.getMessage(JC_SYS_010));
		}
		if(v.getJobTitle()!=null&&v.getJobTitle().length()>32){
			arg1.reject("jobTitle", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getMobile()!=null&&v.getMobile().length()>20){
			arg1.reject("mobile", MessageUtils.getMessage(JC_SYS_011, new Object[]{"20"}));
		}
		if(v.getOfficeTel()!=null&&v.getOfficeTel().length()>30){
			arg1.reject("officeTel", MessageUtils.getMessage(JC_SYS_011, new Object[]{"30"}));
		}
		if(v.getGroupTel()!=null&&v.getGroupTel().length()>30){
			arg1.reject("groupTel", MessageUtils.getMessage(JC_SYS_011, new Object[]{"30"}));
		}
		if(v.getEmail()!=null&&v.getEmail().length()>64){
			arg1.reject("email", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getEthnic()!=null&&v.getEthnic().length()>32){
			arg1.reject("ethnic", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getHometown()!=null&&v.getHometown().length()>256){
			arg1.reject("hometown", MessageUtils.getMessage(JC_SYS_011, new Object[]{"256"}));
		}
		if(v.getDegree()!=null&&v.getDegree().length()>32){
			arg1.reject("degree", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getCername()!=null&&v.getCername().length()>255){
			arg1.reject("cername", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getMaritalStatus()!=null&&v.getMaritalStatus().length()>32){
			arg1.reject("maritalStatus", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getPayCardNo()!=null&&v.getPayCardNo().length()>50){
			arg1.reject("payCardNo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getCardBank()!=null&&v.getCardBank().length()>255){
			arg1.reject("cardBank", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getCardName()!=null&&v.getCardName().length()>50){
			arg1.reject("cardName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getOfficeAddress()!=null&&v.getOfficeAddress().length()>255){
			arg1.reject("officeAddress", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getIsDriver()!=null&&v.getIsDriver().length()>1){
			arg1.reject("isDriver", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if(v.getIsLeader()!=null&&v.getIsLeader().length()>1){
			arg1.reject("isLeader", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if(v.getIsCheck()!=null&&v.getIsCheck().length()>1){
			arg1.reject("isCheck", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if(v.getOpenCale()!=null&&v.getOpenCale().length()>1){
			arg1.reject("openCale", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if(v.getQuestion()!=null&&v.getQuestion().length()>255){
			arg1.reject("question", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getAnswer()!=null&&v.getAnswer().length()>255){
			arg1.reject("answer", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getPhoto()!=null&&v.getPhoto().length()>255){
			arg1.reject("photo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if(v.getLockType()!=null&&v.getLockType().length()>1){
			arg1.reject("lockType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if(v.getTheme()!=null&&v.getTheme().length()>100){
			arg1.reject("theme", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if(v.getExtStr1()!=null&&v.getExtStr1().length()>200){
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr2()!=null&&v.getExtStr2().length()>200){
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr3()!=null&&v.getExtStr3().length()>200){
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr4()!=null&&v.getExtStr4().length()>200){
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if(v.getExtStr5()!=null&&v.getExtStr5().length()>200){
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
	}
}
