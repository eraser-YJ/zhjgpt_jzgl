package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IUserIpDao;
import com.jc.system.security.domain.UserIp;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UserIpDaoImpl extends BaseServerDaoImpl<UserIp> implements IUserIpDao{

	public UserIpDaoImpl(){}

}