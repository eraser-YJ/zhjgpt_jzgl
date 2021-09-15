package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IRoleExtsDao;
import com.jc.system.security.domain.RoleExts;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class RoleExtsDaoImpl extends BaseServerDaoImpl<RoleExts> implements IRoleExtsDao {

	@Override
    public Integer save(RoleExts roleExts) {
		return getTemplate().insert(getNameSpace(roleExts)+".insert", roleExts);
	}

	@Override
    public Integer deleteByRoleId(RoleExts roleExts) {
		return getTemplate().update(getNameSpace(roleExts)+".deleteByIds", roleExts);
	}
}
