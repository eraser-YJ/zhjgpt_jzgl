package com.jc.sys.dao;

import com.jc.foundation.exception.CustomException;
import com.jc.sys.domain.SubRoleMenu;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ISubRoleMenuDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public interface ISubRoleMenuDao extends IBaseDao<SubRoleMenu> {
	
	/**
	 * 按照roleIds删除信息
	 * @param subRoleMenu
	 * @return
	 */
	public Integer deleteByRoleIds(SubRoleMenu subRoleMenu);

	/**
	 * @description 根据用户、部门ID返回子系统角色菜单集合
	 * @param  subRoleMenu
	 * @return void
	 * @author
	 * @version 2018-04-12
	 */
	public List<SubRoleMenu> getMenuIdsForUserAndDeptId(SubRoleMenu subRoleMenu) throws CustomException;

	public void deleteAll(SubRoleMenu vo);
}
