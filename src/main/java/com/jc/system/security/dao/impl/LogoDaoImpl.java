package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.ILogoDao;
import com.jc.system.security.domain.Logo;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class LogoDaoImpl extends BaseServerDaoImpl<Logo> implements ILogoDao{

	public LogoDaoImpl(){}

	@Override
	public void updateInit() {
		Logo logo = new Logo();
		getTemplate().update(getNameSpace(logo) + ".updateInit", logo);
	}
}