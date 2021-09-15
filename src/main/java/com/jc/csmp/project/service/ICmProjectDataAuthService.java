package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectDataAuth;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-项目数据权限管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectDataAuthService extends IBaseService<CmProjectDataAuth>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectDataAuth entity) throws CustomException;

	/**
	 * 保存或修改方法
	 * @param busId: 业务的主键id
	 * @param deptIds: 参与机构
	 * @return
	 * @throws CustomException
	 */
    Result saveOrUpdate(String busId, String...deptIds) throws CustomException;
}
