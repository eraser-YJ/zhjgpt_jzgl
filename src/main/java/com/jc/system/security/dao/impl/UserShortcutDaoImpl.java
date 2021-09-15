package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.dao.IUserShortcutDao;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.UserShortcut;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UserShortcutDaoImpl extends BaseServerDaoImpl<UserShortcut> implements IUserShortcutDao{

	public UserShortcutDaoImpl(){}

	@Override
    public Integer deleteByShortcutId(UserShortcut userShortcut) throws CustomException {
		return getTemplate().update(getNameSpace(userShortcut)+".deleteByShortcutIds", userShortcut);
	}

	@Override
    public Integer deleteByUserId(UserShortcut userShortcut) throws CustomException {
		return getTemplate().update(getNameSpace(userShortcut)+".deleteByUserId", userShortcut);
	}
	
	@Override
    public List<Shortcut> getShortcutListByUserId(String userid) throws CustomException {
		UserShortcut userShortcut = new UserShortcut();
		userShortcut.setUserid(userid);
		userShortcut.setDeleteFlag(0);
		userShortcut.setOrderBy("t.QUEUE");
		return getTemplate().selectList(getNameSpace(userShortcut)+".getShortcutListByUser", userShortcut);
	}

}