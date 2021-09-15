package com.jc.sys.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.sys.dao.ISubRoleMenuDao;
import com.jc.sys.domain.SubRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@ClassName SubRoleMenuDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubRoleMenuDaoImpl extends BaseClientDaoImpl<SubRoleMenu> implements ISubRoleMenuDao {

	public SubRoleMenuDaoImpl() {
	}

	/**
	 * 按照roleIds删除信息
	 * 
	 * @param subRoleMenu
	 * @return
	 */
	@Override
	public Integer deleteByRoleIds(SubRoleMenu subRoleMenu) {
		return this.getTemplate().update(this.getNameSpace(subRoleMenu).concat(".deleteByRoleIds"), subRoleMenu);
	}

	@Override
	public List<SubRoleMenu> getMenuIdsForUserAndDeptId(SubRoleMenu subRoleMenu) throws CustomException {
		return getTemplate().selectList(getNameSpace(subRoleMenu) + ".getMenuIdsForUserAndDeptId", subRoleMenu);
	}

	@Override
	public void deleteAll(SubRoleMenu vo) {
		getTemplate().delete(getNameSpace(new SubRoleMenu()) + ".deleteAll",vo);
	}
}