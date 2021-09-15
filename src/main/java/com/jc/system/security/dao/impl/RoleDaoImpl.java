package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.dao.IRoleDao;
import com.jc.system.security.domain.Role;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class RoleDaoImpl extends BaseServerDaoImpl<Role> implements IRoleDao {

	@Override
	public List<Role> getRolesForUser(Role role) throws CustomException {
		return getTemplate().selectList(getNameSpace(role)+".getRolesForUser", role);
	}

	@Override
	public Role getRoleById(Role role) throws CustomException {
		return getTemplate().selectOne(getNameSpace(role)+".getRoleById", role);
	}

	@Override
	public List<Role> getRolesByUserId(Role role) {
		return getTemplate().selectList(getNameSpace(role)+".getRolesByUserId", role);
	}

	@Override
	public List<Role> getRolesByRoleOrDept(Role role) {
		return getTemplate().selectList(getNameSpace(role)+".getRolesByRoleOrDept", role);
	}

	@Override
	public List<Role> getRolesByUserIdAndMenuId(Role role) {
		return getTemplate().selectList(getNameSpace(role)+".getRolesByUserIdAndMenuId", role);
	}

}