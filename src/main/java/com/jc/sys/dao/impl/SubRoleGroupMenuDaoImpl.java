package com.jc.sys.dao.impl;

import com.jc.foundation.exception.CustomException;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubRoleGroupMenuDao;
import com.jc.sys.domain.SubRoleGroupMenu;

import java.util.List;

/**
 *@ClassName SubRoleGroupMenuDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubRoleGroupMenuDaoImpl extends BaseClientDaoImpl<SubRoleGroupMenu> implements ISubRoleGroupMenuDao {

	public SubRoleGroupMenuDaoImpl() {
	}

	/**
	 * 按照roleGroupIds删除信息
	 * 
	 * @param subRoleGroupMenu
	 * @return
	 */
	@Override
	public Integer deleteByRoleGroupIds(SubRoleGroupMenu subRoleGroupMenu) {
		return this.getTemplate().update(this.getNameSpace(subRoleGroupMenu).concat(".deleteByRoleGroupIds"), subRoleGroupMenu);
	}

	@Override
	public List<SubRoleGroupMenu> getMenuIdsForDeptId(SubRoleGroupMenu subRoleGroupMenu) throws CustomException {
		return getTemplate().selectList(getNameSpace(subRoleGroupMenu) + ".getMenuIdsForDeptId", subRoleGroupMenu);
	}
}