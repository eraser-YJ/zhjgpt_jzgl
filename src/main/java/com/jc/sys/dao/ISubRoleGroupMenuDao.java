package com.jc.sys.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.CustomException;
import com.jc.sys.domain.SubRoleGroupMenu;

import java.util.List;


/**
 *@ClassName ISubRoleGroupMenuDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/

public interface ISubRoleGroupMenuDao extends IBaseDao<SubRoleGroupMenu> {
	
	/**
	 * 按照roleGroupIds删除信息
	 * 
	 * @param subRoleGroupMenu
	 * @return
	 */
	public Integer deleteByRoleGroupIds(SubRoleGroupMenu subRoleGroupMenu);

	public List<SubRoleGroupMenu> getMenuIdsForDeptId(SubRoleGroupMenu subRoleGroupMenu) throws CustomException;
}
