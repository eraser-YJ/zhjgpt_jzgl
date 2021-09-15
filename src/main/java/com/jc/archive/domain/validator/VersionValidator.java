package com.jc.archive.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.archive.domain.Version;

/**
 * @title  GOA2.0源代码
 * @description  检验类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-01
 */

public class VersionValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-07-01
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Version.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-07-01
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		Version v =  (Version)arg0;
			if(v.getDocState()!=null&&v.getDocState().length()>1){
				arg1.reject("docState", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getFileType()!=null&&v.getFileType().length()>1){
				arg1.reject("fileType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getContentType()!=null&&v.getContentType().length()>1){
				arg1.reject("contentType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getDmLink()!=null&&v.getDmLink().length()>1000){
				arg1.reject("dmLink", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1,000"}));
			}
			if(v.getDmName()!=null&&v.getDmName().length()>255){
				arg1.reject("dmName", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getDmAlias()!=null&&v.getDmAlias().length()>255){
				arg1.reject("dmAlias", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getDmLockStatus()!=null&&v.getDmLockStatus().length()>1){
				arg1.reject("dmLockStatus", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getPhysicalPath()!=null&&v.getPhysicalPath().length()>1000){
				arg1.reject("physicalPath", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1,000"}));
			}
			if(v.getDmDir()!=null&&v.getDmDir().length()>255){
				arg1.reject("dmDir", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getDmType()!=null&&v.getDmType().length()>64){
				arg1.reject("dmType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getDmSuffix()!=null&&v.getDmSuffix().length()>64){
				arg1.reject("dmSuffix", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getKmTitle()!=null&&v.getKmTitle().length()>255){
				arg1.reject("kmTitle", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getKeyWords()!=null&&v.getKeyWords().length()>255){
				arg1.reject("keyWords", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getPermissionValue()!=null&&v.getPermissionValue().length()>10){
				arg1.reject("permissionValue", MessageUtils.getMessage("JC_SYS_011", new Object[]{"10"}));
			}
			if(v.getAuthor()!=null&&v.getAuthor().length()>64){
				arg1.reject("author", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getDmSize()!=null&&v.getDmSize().length()>64){
				arg1.reject("dmSize", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getSeq()!=null&&v.getSeq().length()>64){
				arg1.reject("seq", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getIsValid()!=null&&v.getIsValid().length()>64){
				arg1.reject("isValid", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getPiId()!=null&&v.getPiId().length()>64){
				arg1.reject("piId", MessageUtils.getMessage("JC_SYS_011", new Object[]{"64"}));
			}
			if(v.getKmContent()!=null&&v.getKmContent().length()>65535){
				arg1.reject("kmContent", MessageUtils.getMessage("JC_SYS_011", new Object[]{"65,535"}));
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
