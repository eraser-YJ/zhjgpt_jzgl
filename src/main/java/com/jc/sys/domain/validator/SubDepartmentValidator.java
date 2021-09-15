package com.jc.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.sys.domain.SubDepartment;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubDepartmentValidator implements Validator{

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
		return SubDepartment.class.equals(arg0);
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
		SubDepartment v =  (SubDepartment)arg0;
			if(v.getCode()!=null&&v.getCode().length()>64){
				arg1.reject("code", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
			}
			if(v.getName()==null){
				arg1.reject("name", MessageUtils.getMessage(JC_SYS_010));
			}
			if(v.getName()!=null&&v.getName().length()>255){
				arg1.reject("name", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
			}
			if(v.getFullName()!=null&&v.getFullName().length()>100){
				arg1.reject("fullName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
			}
			if(v.getQueue()==null){
				arg1.reject("queue", MessageUtils.getMessage(JC_SYS_010));
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
