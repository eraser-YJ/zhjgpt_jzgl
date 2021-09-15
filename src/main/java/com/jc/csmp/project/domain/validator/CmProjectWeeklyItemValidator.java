package com.jc.csmp.project.domain.validator;

import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 建设管理-周报事项检验类
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmProjectWeeklyItemValidator implements Validator{
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
		return CmProjectWeeklyItem.class.equals(arg0);
	}

	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @Author 常鹏
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		CmProjectWeeklyItem v = (CmProjectWeeklyItem) arg0;
		if (v.getCreateUser() != null && v.getCreateUser().length() > FIFTY) {
			arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getCreateUserDept() != null && v.getCreateUserDept().length() > FIFTY) {
			arg1.reject("createUserDept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getCreateUserOrg() != null && v.getCreateUserOrg().length() > FIFTY) {
			arg1.reject("createUserOrg", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getExtStr1() != null && v.getExtStr1().length() > TWO_HUNDRED) {
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr2() != null && v.getExtStr2().length() > TWO_HUNDRED) {
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr3() != null && v.getExtStr3().length() > TWO_HUNDRED) {
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr4() != null && v.getExtStr4().length() > TWO_HUNDRED) {
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getExtStr5() != null && v.getExtStr5().length() > TWO_HUNDRED) {
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
		}
		if (v.getModifyUser() != null && v.getModifyUser().length() > FIFTY) {
			arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
	}
}
