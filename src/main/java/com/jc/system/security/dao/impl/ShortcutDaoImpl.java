package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.dao.IShortcutDao;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class ShortcutDaoImpl extends BaseServerDaoImpl<Shortcut> implements IShortcutDao{

	public ShortcutDaoImpl(){}

	@Override
    public Integer deleteBysubSystemId(Shortcut shortcut) {
		return getTemplate().update(getNameSpace(shortcut) + ".deletebysubSystemId", shortcut);
	}

}