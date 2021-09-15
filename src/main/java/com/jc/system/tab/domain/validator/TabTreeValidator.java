package com.jc.system.tab.domain.validator;

import com.jc.system.tab.domain.TabTree;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class TabTreeValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";

	@Override
    public boolean supports(Class<?> arg0) {
		return TabTree.class.equals(arg0);
	}

	@Override
    public void validate(Object arg0, Errors arg1) {
		TabTree v =  (TabTree)arg0;
		if(v.getSysPermission()!=null&&v.getSysPermission().length()>64){
			arg1.reject("sysPermission", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getTabTitle()!=null&&v.getTabTitle().length()>64){
			arg1.reject("tabTitle", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getTabUrl()!=null&&v.getTabUrl().length()>64){
			arg1.reject("tabUrl", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getTabFlag()!=null&&v.getTabFlag().length()>64){
			arg1.reject("tabFlag", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
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
