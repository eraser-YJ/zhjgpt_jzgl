package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.IBeanPropertyService;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.dao.ISubsystemDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.IRoleMenusService;
import com.jc.system.security.service.IShortcutService;
import com.jc.system.security.service.ISubsystemService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 子系统维护接口实现类
 * @author Administrator
 * @date 2020-06-29
 */
@Service
public class SubsystemServiceImpl extends BaseServiceImpl<Subsystem> implements ISubsystemService{
	private static Logger logger = Logger.getLogger(SubsystemServiceImpl.class);
	private ISubsystemDao subsystemDao;
	@Autowired
	private IMenuService menuService;
	@Autowired
	private IShortcutService shortcutService;

	public SubsystemServiceImpl(){}
	
	@Autowired
	public SubsystemServiceImpl(ISubsystemDao subsystemDao){
		super(subsystemDao);
		this.subsystemDao = subsystemDao;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer save(Subsystem subsystem) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subsystem,false);
			Menu menu = getMenuBySubsystem(subsystem);
			menuService.save(menu);
			subsystem.setMenuid(menu.getId());
			result = dao.save(subsystem);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subsystem);
			throw ce;
		}
		return result;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer update(Subsystem subsystem) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subsystem,true);
			result = dao.update(subsystem);
			Menu menu = getMenuBySubsystem(subsystem);
			menuService.update(menu);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subsystem);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(Subsystem subsystem) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(subsystem,true);
			result = subsystemDao.delete(subsystem);
			List<Subsystem> list = subsystemDao.queryDeleteMenu(subsystem);
			String [] menuPrimaryKeys = new String[list.size()];
			int x=0;
			for(Subsystem sub:list){
				menuPrimaryKeys[x] = String.valueOf(sub.getMenuid());
				x++;
				Shortcut shortcut = new Shortcut();
				shortcut.setModifyUser(subsystem.getModifyUser());
				shortcut.setModifyDate(subsystem.getModifyDate());
				shortcut.setSubsystemid(sub.getMenuid());
				shortcutService.deleteBysubSystemId(shortcut);
			}
			Menu menu = getMenuBySubsystem(subsystem);
			menu.setPrimaryKeys(menuPrimaryKeys);
			menuService.delete(menu);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(subsystem);
			throw ce;
		}
		return result;
	}
	
	private Menu getMenuBySubsystem(Subsystem subsystem){
		Menu menu = new Menu();
		menu.setId(subsystem.getMenuid());
		menu.setName(subsystem.getName());
		menu.setParentId("-1");
		menu.setIcon(subsystem.getIcon());
		menu.setActionName(subsystem.getFirstUrl());
		menu.setMenuType(0);
		menu.setQueue(subsystem.getQueue());
		menu.setIsShow(subsystem.getIsShow());
		menu.setRootNode("-1");
		if(subsystem.getPermission()==null||!subsystem.getPermission().equals(GlobalContext.SYS_CENTER)){
			menu.setWeight(25);
		}
		menu.setCreateDate(subsystem.getCreateDate());
		menu.setCreateUser(subsystem.getCreateUser());
		menu.setModifyDate(subsystem.getModifyDate());
		menu.setModifyUser(subsystem.getModifyUser());
		menu.setDeleteFlag(subsystem.getDeleteFlag());
		menu.setExtStr1(subsystem.getUrl());
		return menu;
	}
	
	@Override
    public void init(){
		if("false".equals(GlobalContext.getProperty("subsystem.init"))){
			return;
		}
		ISubsystemDao tempSubsystemDao = SpringContextHolder.getBean(ISubsystemDao.class);
		IMenuService tempMenuService = SpringContextHolder.getBean(IMenuService.class);
		IBeanPropertyService tempPropertyService = SpringContextHolder.getBean(IBeanPropertyService.class);
		Subsystem subsystem = new Subsystem();
		if(GlobalContext.isSysCenter()){
			subsystem.setPermission(GlobalContext.SYS_CENTER);
		}else{
			subsystem.setPermission(GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD));
		}
		List<Subsystem> list = tempSubsystemDao.queryAll(subsystem);
		for(Subsystem tempSubSystem : list){
			tempSubSystem.setUrl(GlobalContext.getProperty("cas.localUrl"));
			try {
				tempPropertyService.fillProperties(tempSubSystem,true);
				tempSubsystemDao.update(tempSubSystem);
				Menu menu = getMenuBySubsystem(tempSubSystem);
				tempMenuService.update(menu);
			} catch (DBException e) {
				logger.error(e);
			} catch (CustomException e) {
				logger.error(e);
			}
		}
	}

}