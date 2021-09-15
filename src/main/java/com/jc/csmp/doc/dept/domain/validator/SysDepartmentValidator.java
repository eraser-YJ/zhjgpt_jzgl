package com.jc.csmp.doc.dept.domain.validator;

import com.jc.csmp.doc.dept.domain.SysDepartment;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @Version 1.0
 */
public class SysDepartmentValidator implements Validator{
	private static final String JC_SYS_010 = "JC_SYS_010";
	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final int FIFTY = 50;
	private static final int TWO_HUNDRED = 200;

	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @Author 常鹏
	 * @version 1.0
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return SysDepartment.class.equals(arg0);
	}

	/**
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		SysDepartment v = (SysDepartment) arg0;
		if (v.getId() != null && v.getId().length() > 36) {
			arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getCode() != null && v.getCode().length() > 64) {
			arg1.reject("code", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getName() != null && v.getName().length() > 255) {
			arg1.reject("name", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getFullName() != null && v.getFullName().length() > 100) {
			arg1.reject("fullName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getDeptDesc() != null && v.getDeptDesc().length() > 255) {
			arg1.reject("deptDesc", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getLeaderId() != null && v.getLeaderId().length() > 36) {
			arg1.reject("leaderId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getChargeLeaderId() != null && v.getChargeLeaderId().length() > 36) {
			arg1.reject("chargeLeaderId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getLeaderId2() != null && v.getLeaderId2().length() > 36) {
			arg1.reject("leaderId2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getParentId() != null && v.getParentId().length() > 36) {
			arg1.reject("parentId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getOrganizationId() != null && v.getOrganizationId().length() > 36) {
			arg1.reject("organizationId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getDeptType() != null && v.getDeptType().length() > 1) {
			arg1.reject("deptType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if (v.getShortName() != null && v.getShortName().length() > 10) {
			arg1.reject("shortName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"10"}));
		}
		if (v.getUserName() != null && v.getUserName().length() > 50) {
			arg1.reject("userName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getPassword() != null && v.getPassword().length() > 50) {
			arg1.reject("password", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getType() != null && v.getType().length() > 2) {
			arg1.reject("type", MessageUtils.getMessage(JC_SYS_011, new Object[]{"2"}));
		}
		if (v.getStatus() != null && v.getStatus().length() > 1) {
			arg1.reject("status", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
		}
		if (v.getLogo() != null && v.getLogo().length() > 200) {
			arg1.reject("logo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getCont() != null && v.getCont().length() > 20) {
			arg1.reject("cont", MessageUtils.getMessage(JC_SYS_011, new Object[]{"20"}));
		}
		if (v.getTelp() != null && v.getTelp().length() > 20) {
			arg1.reject("telp", MessageUtils.getMessage(JC_SYS_011, new Object[]{"20"}));
		}
		if (v.getEmail() != null && v.getEmail().length() > 64) {
			arg1.reject("email", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getMemo() != null && v.getMemo().length() > 500) {
			arg1.reject("memo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"500"}));
		}
		if (v.getDeleteUser() != null && v.getDeleteUser().length() > 36) {
			arg1.reject("deleteUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getCreateUser() != null && v.getCreateUser().length() > 36) {
			arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getCreateUserDep() != null && v.getCreateUserDep().length() > 36) {
			arg1.reject("createUserDep", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getModifyUser() != null && v.getModifyUser().length() > 36) {
			arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"36"}));
		}
		if (v.getExtStr1() != null && v.getExtStr1().length() > 200) {
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr2() != null && v.getExtStr2().length() > 200) {
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr3() != null && v.getExtStr3().length() > 200) {
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr4() != null && v.getExtStr4().length() > 200) {
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr5() != null && v.getExtStr5().length() > 200) {
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}

		if (v.getResourceId() != null && v.getResourceId().length() > 255) {
			arg1.reject("resourceId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
	}
}
