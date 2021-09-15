package com.jc.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.sys.domain.SubDepartmentRoleGroup;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public interface ISubDepartmentRoleGroupService extends IBaseService<SubDepartmentRoleGroup>{
	/**
	* @description 根据主键删除多条记录方法
	* @param  subDepartmentRoleGroup 实体类
	* @return Integer 处理结果
	* @author
	* @version  2018-04-20 
	*/
	public Integer deleteByIds(SubDepartmentRoleGroup subDepartmentRoleGroup) throws CustomException;


	/**
	* @description 按照groupIds保存数据
	* @param  subDepartmentRoleGroup 实体类
	* @return void
	* @author
	* @version  2018-04-20 
	*/
	public void saveByGroupIds(SubDepartmentRoleGroup subDepartmentRoleGroup) throws CustomException;

	public void deleteAll(SubDepartmentRoleGroup vo);
}