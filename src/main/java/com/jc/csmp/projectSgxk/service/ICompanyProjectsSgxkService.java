package com.jc.csmp.projectSgxk.service;

import com.jc.csmp.projectSgxk.domain.CompanyProjectsSgxk;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface ICompanyProjectsSgxkService extends IBaseService<CompanyProjectsSgxk>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CompanyProjectsSgxk entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CompanyProjectsSgxk entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CompanyProjectsSgxk entity) throws CustomException;
}
