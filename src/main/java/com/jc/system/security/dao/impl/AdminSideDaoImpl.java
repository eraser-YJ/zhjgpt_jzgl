package com.jc.system.security.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.jc.system.security.domain.AdminSide;
import com.jc.system.security.dao.IAdminSideDao;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class AdminSideDaoImpl extends BaseServerDaoImpl<AdminSide> implements IAdminSideDao{

	@Override
	public Integer deleteAdminSide(AdminSide adminSide) {
		return getTemplate().delete(getNameSpace(adminSide) + ".deleteAdminSide" , adminSide);
	}

	@Override
	public List<AdminSide> queryManagerDeptRree(AdminSide adminSide) {
		return getTemplate().selectList(getNameSpace(adminSide) + ".queryManagerDeptRree" , adminSide);
	}

	@Override
	public List<AdminSide> queryAdminSideIdByDeptId(AdminSide adminSide) {
		return getTemplate().selectList(getNameSpace(adminSide) + ".queryAdminSideIdByDeptId" , adminSide);
	}

	@Override
	public Integer deleteBack(AdminSide adminSide) {
		return getTemplate().update(getNameSpace(adminSide) + ".deleteBackByIds", adminSide);
	}

}