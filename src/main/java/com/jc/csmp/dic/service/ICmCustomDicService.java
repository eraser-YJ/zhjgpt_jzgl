package com.jc.csmp.dic.service;

import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * 建设管理-自定义字典项service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmCustomDicService extends IBaseService<CmCustomDic>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmCustomDic entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmCustomDic entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmCustomDic entity) throws CustomException;

	/**
	 * 验证编码
	 * @param id: 主键id
	 * @param code: 编码
	 * @param dataType: 数据类型
	 * @return
	 */
	Result checkCode(String id, String code, String dataType);

	/**
	 * 根据id获取子数据的数量
	 * @param entity
	 * @return
	 */
	long queryCountByParentIds(CmCustomDic entity);

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	CmCustomDic getById(String id);

	/**
	 * 初始化缓存
	 */
	void initCache();
}
