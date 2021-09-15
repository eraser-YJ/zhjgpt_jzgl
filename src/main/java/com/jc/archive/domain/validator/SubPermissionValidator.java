package com.jc.archive.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.archive.domain.SubPermission;

/**
 * @title  GOA2.0源代码
 * @description  检验类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-19
 */

public class SubPermissionValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-06-19
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return SubPermission.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-06-19
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		SubPermission v =  (SubPermission)arg0;
			if(v.getControlName()!=null&&v.getControlName().length()>255){
				arg1.reject("controlName", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
	}
}
