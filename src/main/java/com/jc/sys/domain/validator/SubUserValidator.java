package com.jc.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.sys.domain.SubUser;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubUserValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final String JC_SYS_010 = "JC_SYS_010";
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2018-04-04
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return SubUser.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2018-04-04
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		SubUser v =  (SubUser)arg0;
			if(v.getCode()!=null&&v.getCode().length()>64){
				arg1.reject("code", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
			}
			if(v.getDisplayName()!=null&&v.getDisplayName().length()>50){
				arg1.reject("displayName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
			}
			if(v.getLoginName()!=null&&v.getLoginName().length()>50){
				arg1.reject("loginName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
			}
			if(v.getSex()==null){
				arg1.reject("sex", MessageUtils.getMessage(JC_SYS_010));
			}
			if(v.getSex()!=null&&v.getSex().length()>32){
				arg1.reject("sex", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
			}
			if(v.getDutyId()!=null&&v.getDutyId().length()>32){
				arg1.reject("dutyId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
			}
			if(v.getDeptId()==null){
				arg1.reject("deptId", MessageUtils.getMessage(JC_SYS_010));
			}
			if(v.getStatus()!=null&&v.getStatus().length()>32){
				arg1.reject("status", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
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
