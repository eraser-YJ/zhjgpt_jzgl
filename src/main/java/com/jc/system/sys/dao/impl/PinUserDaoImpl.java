package com.jc.system.sys.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.sys.domain.PinReUser;
import org.springframework.stereotype.Repository;

import com.jc.system.sys.dao.IPinUserDao;
import com.jc.system.sys.domain.PinUser;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class PinUserDaoImpl extends BaseServerDaoImpl<PinUser> implements IPinUserDao{

	public PinUserDaoImpl(){}

	@Override
	public List<PinReUser> queryPinUser(PinUser pinUser) {
		return getTemplate().selectList(getNameSpace(new PinUser()) + ".queryPinUser",pinUser);
	}

	@Override
	public void deleteBack(PinUser pinUser) {
		getTemplate().update(getNameSpace(pinUser) + ".deleteBack", pinUser);
	}
}