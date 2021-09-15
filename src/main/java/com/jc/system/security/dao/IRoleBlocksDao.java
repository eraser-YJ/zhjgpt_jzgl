package com.jc.system.security.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.RoleBlocks;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleBlocksDao extends IBaseDao<RoleBlocks> {

	@Override
    Integer save(RoleBlocks roleBlocks);

	Integer deleteByRoleId(RoleBlocks roleBlocks);
}
