package com.jc.archive.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.archive.domain.Audithis;

/**
 * @title  GOA2.0源代码
 * @description  检验类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-30
 */

public class AudithisValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-06-30
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Audithis.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-06-30
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		Audithis v =  (Audithis)arg0;
			if(v.getAuditType()!=null&&v.getAuditType().length()>2){
				arg1.reject("auditType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"2"}));
			}
			if(v.getIp()!=null&&v.getIp().length()>20){
				arg1.reject("ip", MessageUtils.getMessage("JC_SYS_011", new Object[]{"20"}));
			}
			if(v.getOperDesc()!=null&&v.getOperDesc().length()>2000){
				arg1.reject("operDesc", MessageUtils.getMessage("JC_SYS_011", new Object[]{"2,000"}));
			}
			if(v.getExtStr1()!=null&&v.getExtStr1().length()>200){
				arg1.reject("extStr1", MessageUtils.getMessage("JC_SYS_011", new Object[]{"200"}));
			}
			if(v.getExtStr2()!=null&&v.getExtStr2().length()>200){
				arg1.reject("extStr2", MessageUtils.getMessage("JC_SYS_011", new Object[]{"200"}));
			}
			if(v.getExtStr3()!=null&&v.getExtStr3().length()>200){
				arg1.reject("extStr3", MessageUtils.getMessage("JC_SYS_011", new Object[]{"200"}));
			}
			if(v.getExtStr4()!=null&&v.getExtStr4().length()>200){
				arg1.reject("extStr4", MessageUtils.getMessage("JC_SYS_011", new Object[]{"200"}));
			}
			if(v.getExtStr5()!=null&&v.getExtStr5().length()>200){
				arg1.reject("extStr5", MessageUtils.getMessage("JC_SYS_011", new Object[]{"200"}));
			}
	}
}
