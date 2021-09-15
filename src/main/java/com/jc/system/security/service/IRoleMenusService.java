package com.jc.system.security.service;

import java.util.List;

import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.RoleMenus;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleMenusService extends IBaseService<RoleMenus>{

	List<Menu> queryMenuForRoles(String string);
	
}
