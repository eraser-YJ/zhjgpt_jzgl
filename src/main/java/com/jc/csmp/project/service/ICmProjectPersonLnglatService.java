package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectPersonLnglat;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-人员坐标上报
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPersonLnglatService extends IBaseService<CmProjectPersonLnglat>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectPersonLnglat entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectPersonLnglat entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectPersonLnglat entity) throws CustomException;

	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	CmProjectPersonLnglat getById(String id);
}
