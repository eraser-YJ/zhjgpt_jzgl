package com.jc.system.api.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.api.dao.IApiMetaDao;
import com.jc.system.api.domain.ApiMeta;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class ApiMetaDaoImpl extends BaseClientDaoImpl<ApiMeta> implements IApiMetaDao {

	public ApiMetaDaoImpl(){}

	@Override
	public String getAllSubsystemAndApi() {
		return null;
	}

}