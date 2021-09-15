package com.jc.mobile.login.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.mobile.login.domain.MobileLoginToken;

/**
 * 移动端登录tokenservice
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface IMobileLoginTokenService extends IBaseService<MobileLoginToken>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(MobileLoginToken entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	MobileLoginToken getById(String id);

	/**
	 * 根据用户id获取对象
	 * @param userId
	 * @return
	 */
	MobileLoginToken getByUserId(String userId);

	/**
	 * 根据token获取对象
	 * @param token
	 * @return
	 */
	MobileLoginToken getByToken(String token);

	/**
	 * 生成token
	 * @param userId
	 * @return
	 */
	MobileLoginToken createToken(String userId);
}
