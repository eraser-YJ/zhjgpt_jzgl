package com.jc.system.sys.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.domain.PageManager;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;

import java.util.List;
import java.util.Map;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPinUserService extends IBaseService<PinUser>{
	/**
	 * @description 根据主键删除多条记录方法
	 * @param  pinUser 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version  2016-12-26
	 */
	Integer deleteByIds(PinUser pinUser) throws CustomException;

	/**
	 * @description 提取用户初始化数据列表
	 */
	List<PinReUser> queryPinUser(PinUser pinUser) throws CustomException;

	/**
	 * @description 还原用户信息
	 */
	void deleteBack(PinUser pinUser);
	/**
	 * @description 提取用户信息导入用户拼音表
	 */
	Map<String, Object> infoLoading() throws CustomException;
	/**
	 * description 查询所有拼音检索用户
	 */
	List<Object> queryPinUsers() throws CustomException;
}