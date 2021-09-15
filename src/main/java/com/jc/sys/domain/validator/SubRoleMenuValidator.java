package com.jc.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.sys.domain.SubRoleMenu;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class SubRoleMenuValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2018-04-18
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return SubRoleMenu.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2018-04-18
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		SubRoleMenu v =  (SubRoleMenu)arg0;
			if(v.getMenuName()!=null&&v.getMenuName().length()>64){
				arg1.reject("menuName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
			}
			if(v.getSecret()!=null&&v.getSecret().length()>32){
				arg1.reject("secret", MessageUtils.getMessage(JC_SYS_011, new Object[]{"32"}));
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
