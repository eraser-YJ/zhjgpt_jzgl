package com.jc.system.security.service;

import java.util.List;
import java.util.Map;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.UserShortcut;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserShortcutService extends IBaseService<UserShortcut>{

	Integer deleteByIds(UserShortcut userShortcut) throws CustomException;

	/**
	 * 根据快捷方式id删除多条记录方法
	 * @param shortcutids
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByShortcutId(String shortcutids) throws CustomException;

	/**
	 * 批量插入快捷方式记录
	 * @param shortcutid
	 * @param sequence
	 * @return
	 * @throws CustomException
	 */
	Map<String,Object> saveorupdate(String shortcutid, String sequence) throws CustomException;

	/**
	 * 根据用户id查询快捷方式
	 * @param userid
	 * @return
	 * @throws CustomException
	 */
	List<Shortcut> getShortcutListByUserId(String userid) throws CustomException;
}