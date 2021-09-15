package com.jc.csmp.project.domain.validator;

import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 建设管理-工程变更单管理检验类
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmProjectChangeOrderValidator implements Validator{
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
		return CmProjectChangeOrder.class.equals(arg0);
	}

	/**
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		CmProjectChangeOrder v = (CmProjectChangeOrder) arg0;
		if (v.getId() != null && v.getId().length() > 50) {
			arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getPiId() != null && v.getPiId().length() > 255) {
			arg1.reject("piId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getCode() != null && v.getCode().length() > 255) {
			arg1.reject("code", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getProjectId() != null && v.getProjectId().length() > 50) {
			arg1.reject("projectId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getContractId() != null && v.getContractId().length() > 50) {
			arg1.reject("contractId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getDeptId() != null && v.getDeptId().length() > 50) {
			arg1.reject("deptId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getChangeType() != null && v.getChangeType().length() > 50) {
			arg1.reject("changeType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getChangeReason() != null && v.getChangeReason().length() > 255) {
			arg1.reject("changeReason", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getChangeContent() != null && v.getChangeContent().length() > 16383) {
			arg1.reject("changeContent", MessageUtils.getMessage(JC_SYS_011, new Object[]{"16383"}));
		}
		if (v.getHandleUser() != null && v.getHandleUser().length() > 255) {
			arg1.reject("handleUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getCreateUser() != null && v.getCreateUser().length() > 50) {
			arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getCreateUserDept() != null && v.getCreateUserDept().length() > 50) {
			arg1.reject("createUserDept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getCreateUserOrg() != null && v.getCreateUserOrg().length() > 50) {
			arg1.reject("createUserOrg", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getModifyUser() != null && v.getModifyUser().length() > 50) {
			arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
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
	}
}
