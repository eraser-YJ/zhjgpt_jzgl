package com.jc.system.security.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IUserExtDao;
import com.jc.system.security.domain.UserExt;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UserExtDaoImpl extends BaseServerDaoImpl<UserExt> implements IUserExtDao {

	public UserExtDaoImpl(){}

}