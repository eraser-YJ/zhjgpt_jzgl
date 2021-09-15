package com.jc.system.security.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.SystemSecurityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IShortcutDao;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.UserShortcut;
import com.jc.system.security.service.IShortcutService;
import com.jc.system.security.service.IUserShortcutService;

import java.util.Collections;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class ShortcutServiceImpl extends BaseServiceImpl<Shortcut> implements IShortcutService{

	protected transient final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IUserShortcutService userShortcutService;
	
	private IShortcutDao shortcutDao;

	public ShortcutServiceImpl(){}
	
	@Autowired
	public ShortcutServiceImpl(IShortcutDao shortcutDao){
		super(shortcutDao);
		this.shortcutDao = shortcutDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(Shortcut shortcut) throws CustomException {
		Integer result = -1;
		try{
			if(shortcut.getPrimaryKeys().length>0){
				String shortcutids = "";
				for(int i=0;i<shortcut.getPrimaryKeys().length;i++){
					if("".equals(shortcutids)){
						shortcutids = shortcut.getPrimaryKeys()[i];
					}else {
						shortcutids = shortcutids + "," + shortcut.getPrimaryKeys()[i];
					}
				}
				userShortcutService.deleteByShortcutId(shortcutids);
			}
			propertyService.fillProperties(shortcut,true);
			result = shortcutDao.delete(shortcut);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(shortcut);
			throw ce;
		}
		return result;
	}

	@Override
    public Integer deleteBysubSystemId(Shortcut shortcut) {
		List<Shortcut> shortcutList = shortcutDao.queryAll(shortcut);
		if(shortcutList.size()>0){
			String shortcutids = "";
			for(int i=0;i<shortcutList.size();i++){
				if("".equals(shortcutids)){
					shortcutids = "'"+shortcutList.get(i).getId()+"'";
				}else {
					shortcutids = shortcutids + ",'" + shortcutList.get(i).getId()+"'";
				}
			}
			try {
				userShortcutService.deleteByShortcutId(shortcutids);
			} catch (CustomException e) {
				logger.error(e);
			}
		}
		return shortcutDao.deleteBysubSystemId(shortcut);
	}

	@Override
    public Map<String, Object> getShortcutUserList() {
		Map<String,Object> shortcutMap = new HashMap<>();
		List<Shortcut> filterlist = new ArrayList<>();
		List<Shortcut> checkedlist = new ArrayList<>();
		try {
			List<Menu> menulist = SystemSecurityUtils.getMenuList();
			if(menulist != null && menulist.size()>0){
				//获取所有快捷方式
				Shortcut shortcut = new Shortcut();
				shortcut.setDeleteFlag(0);
				List<Shortcut> shortcutList = shortcutDao.queryAll(shortcut);
				if(shortcutList != null && shortcutList.size()>0){
					String menuids = ",";
					for(int m=0;m<menulist.size();m++){
						menuids = menuids + menulist.get(m).getId()+",";
					}
					for(int i=0;i<shortcutList.size();i++){
						Shortcut tempvo = shortcutList.get(i);
						if(tempvo.getMenuid() == null) {
                            continue;
                        }
						if(menuids.indexOf(","+tempvo.getMenuid().toString()+",") != -1){
							filterlist.add(tempvo);
						}
					}
					UserShortcut userShortcut = new UserShortcut();
					userShortcut.setDeleteFlag(0);
					userShortcut.setUserid(SystemSecurityUtils.getUser().getId());
					List<UserShortcut> userShortcuts = userShortcutService.queryAll(userShortcut);
					if(userShortcuts != null && userShortcuts.size()>0){
						Map<String,Object> shortcutids = new HashMap<>();
						for(int n=0;n<userShortcuts.size();n++){
							UserShortcut temp = userShortcuts.get(n);
							shortcutids.put(temp.getShortcutid().toString(), temp.getQueue());
						}
						for(int i=0;i<filterlist.size();i++){
							Shortcut tempvo = filterlist.get(i);
							if(shortcutids.get(tempvo.getId()) != null){
								int userorder = Integer.parseInt(shortcutids.get(tempvo.getId()).toString());
								tempvo.setIsChecked(1);
								tempvo.setUserOrder(userorder);
								checkedlist.add(tempvo);
							}
						}
						Collections.sort(checkedlist,new Comparator<Shortcut>(){
							@Override
							public int compare(Shortcut arg0, Shortcut arg1) {
								return arg0.getUserOrder().compareTo(arg1.getUserOrder());
							}
						});
					}
				}
			}
			shortcutMap.put("shortcutlist", filterlist);
			shortcutMap.put("checkedlist", checkedlist);
		} catch (CustomException e) {
			logger.error(e);
		}
		return shortcutMap;
	}

}