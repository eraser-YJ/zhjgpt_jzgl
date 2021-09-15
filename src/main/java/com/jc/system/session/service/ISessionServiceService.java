package com.jc.system.session.service;

import com.jc.foundation.exception.CustomException;
import com.jc.system.session.domain.SessionService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISessionServiceService {

	String getDeptAndUserByOnLine();

	int getLogoutUser(String userName);

	/**
	 * 根据主键删除多条记录方法
	 * @param sessionService
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(SessionService sessionService) throws CustomException;
}