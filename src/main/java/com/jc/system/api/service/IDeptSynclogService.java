package com.jc.system.api.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.api.domain.DeptSynclog;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IDeptSynclogService extends IBaseService<DeptSynclog>{
	/**
	* @description 根据主键删除多条记录方法
	* @param deptSync userSync 实体类
	* @return Integer 处理结果
	*/
	Integer deleteByIds(DeptSynclog deptSync) throws CustomException;
}