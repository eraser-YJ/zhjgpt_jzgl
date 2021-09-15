package com.jc.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.sys.domain.SubRoleMenu;

import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubRoleMenuService extends IBaseService<SubRoleMenu> {
	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRoleMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-18
	 */
	public Integer deleteByIds(SubRoleMenu subRoleMenu) throws CustomException;

	/**
	 * @description 按照MenuIds保存数据
	 * @param  subRoleMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	public void saveByMenuIds(SubRoleMenu subRoleMenu) throws CustomException;
	

	/**
	 * 按照roleIds删除信息
	 * @param subRoleMenu
	 * @return
	 */
	public Integer deleteByRoleIds(SubRoleMenu subRoleMenu);

	/**
	 * @description 根据用户、部门ID返回子系统角色菜单集合
	 * @param  userId 用户Id
	 * @param  deptId 部门Id
	 * @return void
	 * @author
	 * @version 2018-04-12
	 */
	public Map<String, Object> getMenuIdsForUserAndDeptId(String userId,String deptId) throws CustomException;

	public void deleteAll(SubRoleMenu vo);
}