package com.jc.archive.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.archive.domain.Folder;

/**
 * @title  GOA2.0源代码
 * @description  检验类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class FolderValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-06-05
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Folder.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-06-05
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		Folder v =  (Folder)arg0;
			if(v.getFolderType()!=null&&v.getFolderType().length()>1){
				arg1.reject("folderType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getNodeType()!=null&&v.getNodeType().length()>1){
				arg1.reject("nodeType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getFolderName()!=null&&v.getFolderName().length()>64){
				arg1.reject("folderName", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getFolderPath()!=null&&v.getFolderPath().length()>1000){
				arg1.reject("folderPath", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1,000"}));
			}
			if(v.getDmInRecycle()!=null&&v.getDmInRecycle()>1){
				arg1.reject("dmInRecycle", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getKmAppFlag()==null){
				arg1.reject("kmAppFlag", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getKmAppFlag()!=null&&v.getKmAppFlag().length()>1){
				arg1.reject("kmAppFlag", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getPermissionValue()!=null&&v.getPermissionValue().length()>10){
				arg1.reject("permissionValue", MessageUtils.getMessage("JC_SYS_011", new Object[]{"10"}));
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
