package com.jc.system.security.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.RoleExts;
import com.jc.system.security.domain.RoleMenus;

/**
 * 角色扩展
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleExtsDao extends IBaseDao<RoleExts> {

	@Override
    Integer save(RoleExts roleExts);

	Integer deleteByRoleId(RoleExts roleExts);
}
