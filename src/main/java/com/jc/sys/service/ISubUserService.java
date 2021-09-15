package com.jc.sys.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.domain.PageManager;
import com.jc.sys.domain.SubUser;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public interface ISubUserService extends IBaseService<SubUser>{
	/**
	* @description 根据主键删除多条记录方法
	* @param  subUser 实体类
	* @return Integer 处理结果
	* @author
	* @version  2018-04-04 
	*/
	public Integer deleteByIds(SubUser subUser) throws CustomException;

	public void deleteAll(SubUser vo);

	public void reTheme(SubUser vo);
}