package com.jc.dlh.domain.validator;

import com.jc.dlh.domain.DlhTableMap;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @Version 1.0
 */
public class DlhTableMapValidator implements Validator{
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
		return DlhTableMap.class.equals(arg0);
	}

	/**
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		DlhTableMap v = (DlhTableMap) arg0;
		if (v.getId() != null && v.getId().length() > 100) {
			arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getObjUrlK() != null && v.getObjUrlK().length() > 64) {
			arg1.reject("objUrlK", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getObjUrlV() != null && v.getObjUrlV().length() > 64) {
			arg1.reject("objUrlV", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getTableNameK() != null && v.getTableNameK().length() > 100) {
			arg1.reject("tableNameK", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getTableNameV() != null && v.getTableNameV().length() > 100) {
			arg1.reject("tableNameV", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getColumnNameK() != null && v.getColumnNameK().length() > 100) {
			arg1.reject("columnNameK", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getColumnNameV() != null && v.getColumnNameV().length() > 100) {
			arg1.reject("columnNameV", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getColumnType() != null && v.getColumnType().length() > 50) {
			arg1.reject("columnType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
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
