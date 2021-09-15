package com.jc.system.security.service;

import java.util.Map;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Shortcut;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IShortcutService extends IBaseService<Shortcut>{
	/**
	 * 根据主键删除多条记录方法
	 * @param shortcut
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(Shortcut shortcut) throws CustomException;

	/**
	 * 根据子系统对应菜单id删除多条快捷方式记录方法
	 * @param shortcut
	 * @return
	 */
	Integer deleteBysubSystemId(Shortcut shortcut);

	/**
	 * 获取快捷方式用户配置范围
	 * @return
	 */
	Map<String,Object> getShortcutUserList();
}