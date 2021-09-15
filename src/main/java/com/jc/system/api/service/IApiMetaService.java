package com.jc.system.api.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.api.domain.ApiMeta;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IApiMetaService extends IBaseService<ApiMeta> {
	/**
	* @description 根据主键删除多条记录方法
	* @param apiMeta 实体类
	* @return Integer 处理结果
	*/
	Integer deleteByIds(ApiMeta apiMeta) throws CustomException;

	void disableApp(ApiMeta app);

	void enableApp(ApiMeta app);
	
	String getAllSubsystemAndApi(String pluginId);
	
}