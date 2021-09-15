package com.jc.system.session.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.session.dao.ISessionServiceDao;
import com.jc.system.session.domain.SessionService;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class SessionServiceDaoImpl extends BaseServerDaoImpl<SessionService> implements ISessionServiceDao {

	public SessionServiceDaoImpl(){}

}