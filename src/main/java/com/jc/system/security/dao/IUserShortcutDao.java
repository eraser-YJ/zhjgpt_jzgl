package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.UserShortcut;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserShortcutDao extends IBaseDao<UserShortcut>{

	/**
	* @description 根据快捷方式id删除多条记录方法
	* @return Integer 处理结果
	*/
	Integer deleteByShortcutId(UserShortcut userShortcut) throws CustomException;
	
	/**
	* @description 根据用户id删除多条记录方法
	* @return Integer 处理结果
	*/
	Integer deleteByUserId(UserShortcut userShortcut) throws CustomException;

	/**
	 * @description 根据用户id查询快捷方式
	 * @return List<Shortcut> 查询结果
	 */
	List<Shortcut> getShortcutListByUserId(String userid) throws CustomException;
}
