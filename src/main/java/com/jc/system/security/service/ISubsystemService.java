package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Subsystem;

/**
 * 子系统维护接口
 * @author Administrator
 * @date 2020-06-29
 */
public interface ISubsystemService extends IBaseService<Subsystem>{
	/**
	 * 根据主键删除多条记录方法
	 * @param subsystem
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(Subsystem subsystem) throws CustomException;

	/**
	 * 插入记录方法
	 * @param subsystem
	 * @return
	 * @throws CustomException
	 */
	@Override
    Integer save(Subsystem subsystem) throws CustomException;

	/**
	 * 修改记录方法
	 * @param subsystem
	 * @return
	 * @throws CustomException
	 */
	@Override
    Integer update(Subsystem subsystem) throws CustomException;

	/**
	 * 启动时修改子系统url地址
	 */
	void init();
	
}