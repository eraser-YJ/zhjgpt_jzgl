package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.system.security.domain.Menu;
import com.jc.system.security.dao.IMenuDao;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class MenuDaoImpl extends BaseServerDaoImpl<Menu> implements IMenuDao{

	@Override
    public List<Menu> queryMenu(Menu menu) {
		return getTemplate().selectList(getNameSpace(menu)+".queryMenus", menu);
	}

	@Override
    public List<Menu> queryMenuBySubsystem(Menu menu) {
		return getTemplate().selectList(getNameSpace(menu)+".queryMenuBySubsystem", menu);
	}

	@Override
    public List<Menu> queryWithRole(Menu menu){
		return getTemplate().selectList(getNameSpace(menu)+".queryWithRole", menu);
	}

	@Override
    public List<Menu> queryUserMenu(Menu menu) {
		return getTemplate().selectList(getNameSpace(menu)+".queryUserMenu", menu);
	}

	@Override
	public Menu queryMenuForUrl(String url, String subSysSign) {
		Menu menu = new Menu();
		menu.setExtStr3(url);
		menu.setExtStr4(subSysSign);
		return getTemplate().selectOne(getNameSpace(menu)+".queryMenuForUrl", menu);
	}

}