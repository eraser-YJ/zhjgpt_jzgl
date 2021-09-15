package com.jc.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.sys.domain.SubRoleGroup;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRoleGroupValidator implements Validator {

	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final String JC_SYS_010 = "JC_SYS_010";

	/**
	 * @description 检验类能够校验的类
	 * @param arg0 校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version 2018-04-12
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return SubRoleGroup.class.equals(arg0);
	}

	/**
	 * @description 检验具体实现方法
	 * @param arg0 当前的实体类
	 * @param arg1 错误的信息
	 * @author
	 * @version 2018-04-12
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		SubRoleGroup v = (SubRoleGroup) arg0;
		if (v.getGroupName() == null) {
			arg1.reject("groupName", MessageUtils.getMessage(JC_SYS_010));
		}
		if (v.getGroupName() != null && v.getGroupName().length() > 256) {
			arg1.reject("groupName", MessageUtils.getMessage(JC_SYS_011, new Object[] { "256" }));
		}
		if (v.getGroupDescription() != null && v.getGroupDescription().length() > 512) {
			arg1.reject("groupDescription", MessageUtils.getMessage(JC_SYS_011, new Object[] { "512" }));
		}
		/*
		 * if(v.getStatus()==null){ arg1.reject("status", MessageUtils.getMessage(JC_SYS_010)); }
		 */
		if (v.getSecret() != null && v.getSecret().length() > 32) {
			arg1.reject("secret", MessageUtils.getMessage(JC_SYS_011, new Object[] { "32" }));
		}
		if (v.getExtStr1() != null && v.getExtStr1().length() > 200) {
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[] { "200" }));
		}
		if (v.getExtStr2() != null && v.getExtStr2().length() > 200) {
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[] { "200" }));
		}
		if (v.getExtStr3() != null && v.getExtStr3().length() > 200) {
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[] { "200" }));
		}
		if (v.getExtStr4() != null && v.getExtStr4().length() > 200) {
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[] { "200" }));
		}
		if (v.getExtStr5() != null && v.getExtStr5().length() > 200) {
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[] { "200" }));
		}
	}
}
