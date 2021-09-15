package com.jc.csmp.doc.project.service;

import com.jc.csmp.doc.project.domain.ScsProjectInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IScsProjectInfoService extends IBaseService<ScsProjectInfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ScsProjectInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ScsProjectInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ScsProjectInfo entity) throws CustomException;


	/**
	 * 查询数据
	 *
	 * @param code
	 * @return
	 * @throws CustomException
	 */
	ScsProjectInfo queryByCode(String code)  ;

}
