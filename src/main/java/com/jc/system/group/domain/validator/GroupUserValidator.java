package com.jc.system.group.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.foundation.util.MessageUtils;
import com.jc.system.group.domain.GroupUser;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public class GroupUserValidator implements Validator{
	
	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @author
	 * @version  2014-07-24
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return GroupUser.class.equals(arg0);
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
		GroupUser v =  (GroupUser)arg0;
			if(v.getGroupId()==null){
				arg1.reject("groupId", MessageUtils.getMessage("JC_SYS_010"));
			}
			if(v.getUserId()==null){
				arg1.reject("userId", MessageUtils.getMessage("JC_SYS_010"));
			}
	}
}
