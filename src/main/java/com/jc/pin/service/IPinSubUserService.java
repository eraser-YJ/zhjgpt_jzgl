package com.jc.pin.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubUser;

import java.util.List;
import java.util.Map;

/**
 * 用户拼音处理接口
 * @author Administrator
 * @date 2020-06-30
 */
public interface IPinSubUserService extends IBaseService<PinSubUser>{
	/**
	 * 根据主键删除多条记录方法
	 * @param pinSubUser
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(PinSubUser pinSubUser) throws CustomException;

	/**
	 * 提取用户初始化数据列表
	 * @param pinUser
	 * @return
	 * @throws CustomException
	 */
	List<PinReSubUser> queryPinUser(PinSubUser pinUser) throws CustomException;

	/**
	 * 提取用户信息导入用户拼音表
	 * @return
	 * @throws CustomException
	 */
	Map<String, Object> infoLoading() throws CustomException;

	/**
	 * 查询所有拼音检索用户
	 * @return
	 * @throws CustomException
	 */
	List<Object> queryPinUsers() throws CustomException;
}