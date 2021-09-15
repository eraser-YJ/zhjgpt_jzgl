package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Logo;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ILogoService extends IBaseService<Logo>{
	/**
	* @description 根据主键删除多条记录方法
	* @param logo 实体类
	* @return Integer 处理结果
	*/
	Integer deleteByIds(Logo logo) throws CustomException;

	/**
	 * @description 根据选中登录页样式id设定登录页样式
	 * @param id
	 * @return
	 * @throws CustomException
     */
	Integer updateSet(String id) throws CustomException;
}