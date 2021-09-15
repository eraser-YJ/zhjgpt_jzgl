package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserValidator implements Validator {

	private static final String JC_SYS_011 = "JC_SYS_011";

	@Override
	public boolean supports(Class<?> arg0) {
		return User.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		User v = (User) arg0;
		
		if(v.getStatus()!=null&&v.getStatus().length()>32){
			arg1.reject("status", "用户状态"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}

		if(v.getCode()!=null&&v.getCode().length()>64){
			arg1.reject("code", "用户编号"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}

		if (v.getLoginName() == null || "".equals(v.getLoginName())) {
			arg1.reject("loginName", "登录名称不能为空");
		}
		if (v.getLoginName() != null && v.getLoginName().length() > 200) {
			arg1.reject("loginName", "登录名称"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}

		if (v.getDisplayName() == null || "".equals(v.getDisplayName())) {
			arg1.reject("displayName", "姓名不能为空");
		}
		if (v.getDisplayName() != null && v.getDisplayName().length() > 200) {
			arg1.reject("displayName", "姓名"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));

		}

		if(v.getSex()!=null&&v.getSex().length()>32){
			arg1.reject("sex",  "性别"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}
		if(v.getKind()!=null&&v.getKind().length()>32){
			arg1.reject("kind",  "用户性质"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}

		if(v.getDeptId()==null){
			arg1.reject("deptId", "所属部门不能为空");
		}

		if(v.getCardNo()!=null&&v.getCardNo().length()>20){
			arg1.reject("cardNo", "身份证号"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"18"}));
		}

		if(v.getOrderNo()==null){
			arg1.reject("orderNo","用户序号不能为空");
		}

		if(v.getEthnic()!=null&&v.getEthnic().length()>32){
			arg1.reject("ethnic", "民族"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}

		if(v.getMaritalStatus()!=null&&v.getMaritalStatus().length()>32){
			arg1.reject("maritalStatus", "婚姻状况"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}

		if(v.getHometown()!=null&&v.getHometown().length()>256){
			arg1.reject("hometown", "籍贯"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"256"}));
		}
		if(v.getDegree()!=null&&v.getDegree().length()>32){
			arg1.reject("degree", "学历"+MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
		}

	}
}
