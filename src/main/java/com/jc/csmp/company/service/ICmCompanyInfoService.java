package com.jc.csmp.company.service;

import com.jc.csmp.company.domain.CmCompanyInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-单位管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmCompanyInfoService extends IBaseService<CmCompanyInfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmCompanyInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmCompanyInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmCompanyInfo entity) throws CustomException;

	/**
	 * 验证统一社会信用代码是否存在
	 * @param id
	 * @param creditCode
	 * @return
	 */
	Result checkCreditCode(String id, String creditCode);
}
