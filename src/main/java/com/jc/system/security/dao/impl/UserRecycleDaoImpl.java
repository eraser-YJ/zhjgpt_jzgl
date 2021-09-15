package com.jc.system.security.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IUserRecycleDao;
import com.jc.system.security.domain.UserRecycle;
import org.springframework.stereotype.Repository;
/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UserRecycleDaoImpl extends BaseServerDaoImpl<UserRecycle> implements IUserRecycleDao {

	public UserRecycleDaoImpl(){}

}