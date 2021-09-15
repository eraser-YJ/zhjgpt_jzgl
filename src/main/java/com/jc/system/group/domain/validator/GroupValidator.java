package com.jc.system.group.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.group.domain.Group;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class GroupValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-07-24
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return Group.class.equals(arg0);
	}
	
	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @author
	 * @version  2014-07-24
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		Group v =  (Group)arg0;
			if(v.getName()==null){
				arg1.reject("name", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getName()!=null&&v.getName().length()>255){
				arg1.reject("name", MessageUtils.getMessage("JC_SYS_011", new Object[]{"255"}));
			}
			if(v.getGroupType()==null){
				arg1.reject("groupType", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getGroupType()!=null&&v.getGroupType().length()>1){
				arg1.reject("groupType", MessageUtils.getMessage("JC_SYS_011", new Object[]{"1"}));
			}
	}
}
