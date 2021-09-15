package com.jc.system.content.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.content.domain.AttachBusiness;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class AttachBusinessValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-05-21
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return AttachBusinessValidator.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-05-21
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		AttachBusiness v =  (AttachBusiness)arg0;
			if(v.getBusinessTable()!=null&&v.getBusinessTable().length()>100){
				arg1.reject("businessTable", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
			}
			if(v.getBusinessSource()!=null&&v.getBusinessSource().length()>1){
				arg1.reject("businessSource", MessageUtils.getMessage(JC_SYS_011, new Object[]{"1"}));
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
