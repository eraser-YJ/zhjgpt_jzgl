package com.jc.dlh.filemanage.domain.validator;

import com.jc.dlh.filemanage.domain.DlhFile;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @Version 1.0
 */
public class DlhFileValidator implements Validator{
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
		return DlhFile.class.equals(arg0);
	}

	/**
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		DlhFile v = (DlhFile) arg0;
		if (v.getId() != null && v.getId().length() > 200) {
			arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getFilename() != null && v.getFilename().length() > 200) {
			arg1.reject("filename", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getObjurl() != null && v.getObjurl().length() > 200) {
			arg1.reject("objurl", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getYewuid() != null && v.getYewuid().length() > 200) {
			arg1.reject("yewuid", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getYewucolname() != null && v.getYewucolname().length() > 200) {
			arg1.reject("yewucolname", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getRemark() != null && v.getRemark().length() > 2000) {
			arg1.reject("remark", MessageUtils.getMessage(JC_SYS_011, new Object[]{"2000"}));
		}
		if (v.getTableName() != null && v.getTableName().length() > 200) {
			arg1.reject("tableName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
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
		if (v.getUserId() != null && v.getUserId().length() > 200) {
			arg1.reject("userId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getCreateUser() != null && v.getCreateUser().length() > 200) {
			arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getCreateUserDept() != null && v.getCreateUserDept().length() > 200) {
			arg1.reject("createUserDept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getCreateUserOrg() != null && v.getCreateUserOrg().length() > 200) {
			arg1.reject("createUserOrg", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getModifyUser() != null && v.getModifyUser().length() > 200) {
			arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
	}
}
