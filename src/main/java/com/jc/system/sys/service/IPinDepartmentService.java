package com.jc.system.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.domain.PinReDepartment;

import java.util.List;
import java.util.Map;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPinDepartmentService extends IBaseService<PinDepartment>{

	/**
	 * 根据主键删除多条记录方法
	 * @param pinDepartment
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(PinDepartment pinDepartment) throws CustomException;

	/**
	 * 提取部门初始化数据列表
	 * @param pinDepartment
	 * @return
	 * @throws CustomException
	 */
	List<PinReDepartment> queryPinDepartment(PinDepartment pinDepartment) throws CustomException;

	/**
	 * 提取部门信息导入部门拼音表
	 * @return
	 * @throws CustomException
	 */
	Map<String, Object> infoLoading() throws CustomException;
}