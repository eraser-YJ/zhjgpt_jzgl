package com.jc.oa.click.service.impl;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.util.GlobalContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.oa.click.dao.IClickDao;
import com.jc.oa.click.domain.Click;
import com.jc.oa.click.service.IClickService;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;

@Service
public class ClickServiceImpl extends BaseServiceImpl<Click> implements IClickService{
	
	private IClickDao clickDao;
	
	@Autowired
	public ClickServiceImpl(IClickDao clickDao) {
		super(clickDao);
		this.clickDao = clickDao;
	}
	
	@Autowired
	private IMenuService menuService;
	public ClickServiceImpl() {
	}
	
	public Integer save(Click click) throws DBException {

			return clickDao.save(click);

	}
	
	public List<Click> queryAll(Click click){
		return clickDao.queryAll(click);
	}
	public void clicks(String url) throws CustomException {
         String userId = SystemSecurityUtils.getUser().getId();
		 Menu  menuvo = menuService.queryMenuForUrl(url, GlobalContext.getProperty("subsystem.id"));
		 Click click = new Click();
		 click.setUserId(userId);
		 click.setClicks(1L);
		 click.setMenuAction(menuvo.getActionName());
		 click.setMenuId(menuvo.getId());
		 click.setMenuName(menuvo.getName());
		 clickDao.save(click);
	}
}
