package com.jc.system.content.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.domain.Attach;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class AttachValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-04-17
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Attach.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-04-17
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		Attach v =  (Attach)arg0;
		if(v.getResourcesName()!=null&&v.getResourcesName().length()>200){
			arg1.reject("resourcesName", "ResourcesName is too long");
		}
		if(v.getFileName()!=null&&v.getFileName().length()>64){
			arg1.reject("fileName", "FileName is too long");
		}
		if(v.getBusinessSource()!=null&&v.getBusinessSource().length()>2){
			arg1.reject("businessSource", "BusinessSource is too long");
		}
	}
}
