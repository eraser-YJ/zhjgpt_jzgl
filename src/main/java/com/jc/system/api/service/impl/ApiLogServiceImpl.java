package com.jc.system.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.api.dao.IApiLogDao;
import com.jc.system.api.domain.ApiLog;
import com.jc.system.api.service.IApiLogService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class ApiLogServiceImpl extends BaseServiceImpl<ApiLog> implements IApiLogService{

	private IApiLogDao apiLogDao;
	
	public ApiLogServiceImpl(){}
	
	@Autowired
	public ApiLogServiceImpl(IApiLogDao apiLogDao){
		super(apiLogDao);
		this.apiLogDao = apiLogDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ApiLog apiLog) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(apiLog,true);
			result = apiLogDao.delete(apiLog);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(apiLog);
			throw ce;
		}
		return result;
	}

}