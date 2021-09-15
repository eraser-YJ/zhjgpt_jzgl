package com.jc.supervise.warning.service;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.supervise.point.vo.SupervisionPointInformation;
import com.jc.supervise.warning.domain.CmSupervisionWarning;

/**
 * 建设管理-监管预警管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmSupervisionWarningService extends IBaseService<CmSupervisionWarning>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmSupervisionWarning entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmSupervisionWarning entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmSupervisionWarning entity) throws CustomException;

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	CmSupervisionWarning getById(String id);

	/**
	 * 定时任务
	 */
	void jobHandler();

	/**
	 * 检查脚本
	 * @param information
	 * @param projectNumber
	 * @return
	 */
	Result checkScan(SupervisionPointInformation information, String projectNumber);
}
