package com.jc.system.security.dao;

import com.jc.system.security.domain.Shortcut;
import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IShortcutDao extends IBaseDao<Shortcut>{

	Integer deleteBysubSystemId(Shortcut shortcut);
	
}
