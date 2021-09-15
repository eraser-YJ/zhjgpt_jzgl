package com.jc.system.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.api.dao.IUserSynclogDao;
import com.jc.system.api.domain.UserSynclog;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UserSynclogDaoImpl extends BaseServerDaoImpl<UserSynclog> implements IUserSynclogDao{

	public UserSynclogDaoImpl(){}

}