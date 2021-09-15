package com.jc.pin.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.pin.domain.PinReSubDepartment;
import com.jc.pin.domain.PinSubDepartment;

import java.util.List;
import java.util.Map;

/**
 * 部门拼音service
 * @author Administrator
 * @date 2020-06-30
 */
public interface IPinSubDepartmentService extends IBaseService<PinSubDepartment>{
	/**
	 * 根据主键删除多条记录方法
	 * @param pinSubDepartment
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(PinSubDepartment pinSubDepartment) throws CustomException;

	/**
	 * 提取部门初始化数据列表
	 * @param pinDepartment
	 * @return
	 * @throws CustomException
	 */
	List<PinReSubDepartment> queryPinDepartment(PinSubDepartment pinDepartment) throws CustomException;

	/**
	 * 提取部门信息导入部门拼音表
	 * @return
	 * @throws CustomException
	 */
	Map<String, Object> infoLoading() throws CustomException;
}