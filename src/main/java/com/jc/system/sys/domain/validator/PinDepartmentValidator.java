package com.jc.system.sys.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.sys.domain.PinDepartment;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public class PinDepartmentValidator implements Validator{

	private static final String JC_SYS_011 = "JC_SYS_011";

	@Override
    public boolean supports(Class<?> arg0) {
		return PinDepartment.class.equals(arg0);
	}

	@Override
    public void validate(Object arg0, Errors arg1) {
		PinDepartment v =  (PinDepartment)arg0;
		if(v.getDeptName()!=null&&v.getDeptName().length()>64){
			arg1.reject("deptName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getDeptInitials()!=null&&v.getDeptInitials().length()>64){
			arg1.reject("deptInitials", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getDeptAbbreviate()!=null&&v.getDeptAbbreviate().length()>64){
			arg1.reject("deptAbbreviate", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if(v.getDeptFull()!=null&&v.getDeptFull().length()>64){
			arg1.reject("deptFull", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
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
