package com.jc.system.security.dao;

import java.util.List;

import com.jc.system.security.domain.Menu;
import com.jc.foundation.dao.IBaseDao;

/**
 * 菜单
 * @author Administrator
 * @date 2020-07-01
 */
public interface IMenuDao extends IBaseDao<Menu>{

	List<Menu> queryMenu(Menu menu);
	
	List<Menu> queryMenuBySubsystem(Menu menu);

	/**
	 * 用户角色使用菜单
	 * @param menu
	 * @return
	 */
	List<Menu> queryWithRole(Menu menu);

	/**
	 * 根据菜单id及用户id判断当前用户是否有此菜单
	 * @param menu
	 * @return
	 */
	List<Menu> queryUserMenu(Menu menu);

	Menu queryMenuForUrl(String url, String subSysSign);
}
