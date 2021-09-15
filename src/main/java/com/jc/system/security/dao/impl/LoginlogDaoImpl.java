package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.ILoginlogDao;
import com.jc.system.security.domain.Loginlog;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class LoginlogDaoImpl extends BaseServerDaoImpl<Loginlog> implements ILoginlogDao{

	public LoginlogDaoImpl(){}

}