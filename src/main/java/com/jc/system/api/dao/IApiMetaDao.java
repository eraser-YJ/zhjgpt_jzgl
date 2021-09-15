package com.jc.system.api.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.api.domain.ApiMeta;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IApiMetaDao extends IBaseDao<ApiMeta> {
	String getAllSubsystemAndApi();
}
