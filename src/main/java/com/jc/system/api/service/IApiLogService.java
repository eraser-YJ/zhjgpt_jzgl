package com.jc.system.api.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.api.domain.ApiLog;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IApiLogService extends IBaseService<ApiLog>{
	Integer deleteByIds(ApiLog apiLog) throws CustomException;
}