package com.jc.system.api.domain.validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.api.domain.ApiMeta;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class ApiMetaValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";

	@Override
	public boolean supports(Class<?> arg0) {
		return ApiMeta.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		ApiMeta v =  (ApiMeta)arg0;
		if(v.getAppName()!=null&&v.getAppName().length()>20){
			arg1.reject("appName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"20"}));
		}
		if(v.getApiName()!=null&&v.getApiName().length()>50){
			arg1.reject("apiName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if(v.getUri()!=null&&v.getUri().length()>100){
			arg1.reject("uri", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if(v.getParams()!=null&&v.getParams().length()>200){
			arg1.reject("params", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
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
