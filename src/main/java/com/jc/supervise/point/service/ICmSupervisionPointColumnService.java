package com.jc.supervise.point.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;

/**
 * 建设管理-监察点数据来源管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmSupervisionPointColumnService extends IBaseService<CmSupervisionPointColumn>{
	public final static String SQL = "sql";
	public final static String BEAN = "bean";

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmSupervisionPointColumn entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmSupervisionPointColumn entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmSupervisionPointColumn entity) throws CustomException;

	/**
	 * 修改supervisionId；
	 * @param newSupervisionId：原来的supervisionId
	 * @param oldSupervisionId：新的supervisionId
	 * @return
	 */
	Result updateSupervisionId(String newSupervisionId, String oldSupervisionId);
}
