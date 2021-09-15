package com.jc.system.security.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IRoleBlocksDao;
import com.jc.system.security.domain.RoleBlocks;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class RoleBlocksDaoImpl extends BaseServerDaoImpl<RoleBlocks> implements
		IRoleBlocksDao {

	@Override
    public Integer save(RoleBlocks roleBlocks) {
		return getTemplate().insert(getNameSpace(roleBlocks)+".insert",roleBlocks);
	}

	@Override
    public Integer deleteByRoleId(RoleBlocks roleBlocks) {
		return getTemplate().update(getNameSpace(roleBlocks)+".deleteByIds",roleBlocks);
	}
}
