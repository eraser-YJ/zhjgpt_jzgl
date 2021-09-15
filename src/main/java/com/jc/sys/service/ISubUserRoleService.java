package com.jc.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.sys.domain.SubUserRole;

import java.util.List;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubUserRoleService extends IBaseService<SubUserRole> {
	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subUserRole 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-20
	 */
	public Integer deleteByIds(SubUserRole subUserRole) throws CustomException;
	
	/**
	 * 按照RoleIds保存数据
	 * @param subUserRole
	 * @throws CustomException
	 * @author
	 * @version 2018-04-23
	 */
	public void saveByRoleIds(SubUserRole subUserRole) throws CustomException;

	/**
	 * @description 根据用户、部门ID返回子系统角色集合
	 * @param  subUserRole
	 * @return void
	 * @author
	 * @version 2018-04-12
	 */
	public List<SubUserRole> getRolesByUserAndDeptId(SubUserRole subUserRole) throws CustomException;

	public void deleteAll(SubUserRole vo);
}