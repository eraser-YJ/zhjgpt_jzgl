package com.jc.archive.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.archive.domain.Recommend;

/**
 * @title  GOA2.0源代码
 * @description  检验类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class RecommendValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-06-05
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Recommend.class.equals(arg0);
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
		Recommend v =  (Recommend)arg0;
			if(v.getDocumentId()==null){
				arg1.reject("documentId", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getFileType()!=null&&v.getFileType().length()>1){
				arg1.reject("fileType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
			if(v.getKmRemind()==null){
				arg1.reject("kmRemind", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getKmReceive()==null){
				arg1.reject("kmReceive", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getKmReason()==null){
				arg1.reject("kmReason", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getKmReason()!=null&&v.getKmReason().length()>255){
				arg1.reject("kmReason", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getKmSender()==null){
				arg1.reject("kmSender", MessageUtils.getMessage("JC_SYS_010"));
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
