package com.jc.pin.dao.impl;

import com.jc.pin.domain.PinReSubUser;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.pin.dao.IPinSubUserDao;
import com.jc.pin.domain.PinSubUser;

import java.util.List;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class PinSubUserDaoImpl extends BaseClientDaoImpl<PinSubUser> implements IPinSubUserDao{

	public PinSubUserDaoImpl(){}

	@Override
	public List<PinReSubUser> queryPinUser(PinSubUser pinUser) {
		return getTemplate().selectList(getNameSpace(new PinSubUser()) + ".queryPinSubUser",pinUser);
	}
}