package com.jc.system.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.api.dao.IApiLogDao;
import com.jc.system.api.domain.ApiLog;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class ApiLogDaoImpl extends BaseServerDaoImpl<ApiLog> implements IApiLogDao{

	public ApiLogDaoImpl(){}

}