package com.jc.system.security.service.impl;

import java.util.*;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.ISubsystemService;
import com.jc.system.util.ClazzInvoker;
import org.apache.log4j.Logger;
import org.apache.shiro.cas.CasProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IMenuDao;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.IRoleMenusService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService{

	protected transient final Logger logger = Logger.getLogger(this.getClass());

	private IMenuDao menuDao;

	@Autowired
	public MenuServiceImpl(IMenuDao menuDao){
		super(menuDao);
		this.menuDao = menuDao;
	}

	public MenuServiceImpl(){

	}

	@Autowired
	private IRoleMenusService roleMenuService;

	@Override
	public Map<String,Object> desktopMenuForQuery(Menu menuVo) {
		Map<String,Object> menuMap = new HashMap<>();
		if (!"1".equals(GlobalContext.getProperty("cas.showAllMenu")) && GlobalContext.getProperty("subsystem.id") != null && !"".equals(GlobalContext.getProperty("subsystem.id"))) {
			String portalsign = "portalmenu:" + GlobalContext.getProperty("subsystem.id");
			List<Menu> portalmenulist = new ArrayList<>();
			if (!"2".equals(GlobalContext.getProperty("cas.showAllMenu"))) {
				menuVo.setPermission(GlobalContext.getProperty("subsystem.id"));
			}
			List<Menu> menuList = menuDao.queryMenuBySubsystem(menuVo);
			List<Menu> returnmenuList = new ArrayList<>();
			// ?????????????????????????????????????????????
			menuList = gatherMenus(menuList, menuVo.getUserId(), menuVo.getSubSystemMenuId());
			// menuList = gatherMenus(menuList, menuVo.getUserId());
			if (menuList != null && menuList.size() > 0) {
				for (int i=0;i<menuList.size();i++) {
					Menu menuvo = menuList.get(i);
					boolean check = "-1".equals(menuvo.getParentId());
					//?????????????????????????????????????????????
					if (!StringUtil.isEmpty(menuVo.getSubSystemMenuId())) {
						check = true;
					}
					if (check) {
						if (menuvo.getChildmenus() != null && menuvo.getChildmenus().size()>0) {
							for (Menu menuportal:menuvo.getChildmenus()) {
								if (menuportal.getPermission() != null && portalsign.equals(menuportal.getPermission())) {
									if ("yes".equals(GlobalContext.getProperty("portal.load-on"))) {
										portalmenu(menuportal, menuportal.getParentId(), portalmenulist);
									}
									if (portalmenulist.size() > 0) {
										menuportal.getChildmenus().addAll(portalmenulist);
										menuMap.put("homemenu", menuportal.getName());
										for (Menu portalMenu:portalmenulist) {
											if ("-1".equals(portalMenu.getId()) && portalMenu.getQueue() != null && portalMenu.getQueue() == 1) {
												menuMap.put("portalurl", portalMenu.getActionName());
												menuMap.put("menuId",portalMenu.getId());
												break;
											} else {
												menuMap.put("portalurl", "");
											}
										}
										menuMap.put("topmenuid",menuportal.getId());
									}
								}
							}
							returnmenuList.addAll(menuvo.getChildmenus());
						}
					}
				}
			}
			menuMap.put("menuList", returnmenuList);
		} else {
			List<Menu> menuList = menuDao.queryMenu(menuVo);
			if (menuList != null && menuList.size()>0) {
				List<Menu> leftmenuList = new ArrayList<>();
				for (int k = 0; k < menuList.size(); k++) {
					Menu tempMenu = menuList.get(k);
					if (tempMenu.getPermission() != null && "portalmenu".equals(tempMenu.getPermission())) {
						menuMap.put("homemenu", tempMenu.getName());
						tempMenu.setUserId(menuVo.getUserId());
						leftmenuList = getResourceInfo(tempMenu,"power");
						for (int i = 0; i < leftmenuList.size(); i++) {
							Menu portalMenu = leftmenuList.get(i);
							if ("-1".equals(portalMenu.getId()) && portalMenu.getQueue() != null && portalMenu.getQueue() == 1) {
								menuMap.put("portalurl", portalMenu.getActionName());
								menuMap.put("menuId",portalMenu.getId());
								tempMenu.setActionName(portalMenu.getActionName());
								break;
							} else {
								menuMap.put("portalurl", "");
							}
						}
						menuMap.put("topmenuid",tempMenu.getId());
						break;
					}
				}
				menuMap.put("leftmenuList", leftmenuList);
			}
			if ("1".equals(GlobalContext.getProperty("cas.showAllMenu")) && GlobalContext.getProperty("subsystem.id") != null && !GlobalContext.isSysCenter()) {
				try {
					ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
					Subsystem subsystem = new Subsystem();
					subsystem.setDeleteFlag(0);
					List<Subsystem> subsystemList = subsystemService.queryAll(subsystem);
					Map<String, String> subsystemMap = new HashMap<>();
					for (Subsystem subsystemA : subsystemList) {
						if (!StringUtil.isEmpty(subsystemA.getPermission()) && !StringUtil.isEmpty(subsystemA.getUrl())) {
							subsystemMap.put(String.valueOf(subsystemA.getMenuid()),subsystemA.getPermission());
						}
					}
					if (menuList != null && menuList.size() > 0) {
						for (int i = 0; i < menuList.size(); i++) {
							Menu remenuVo = menuList.get(i);
							if (!"-1".equals(remenuVo.getParentId())) {
								continue;
							}
							if (GlobalContext.getProperty("subsystem.id").equals(subsystemMap.get(String.valueOf(remenuVo.getId())))) {
								remenuVo.setIsShow(0);
							} else {
								remenuVo.setIsShow(1);
							}
						}
					}
				} catch (CustomException e) {
					logger.error(e);
				}
			}
			// ?????????????????????????????????????????????
			menuMap.put("menuList", gatherMenus(menuList,menuVo.getUserId(), menuVo.getSubSystemMenuId()));
			//menuMap.put("menuList", gatherMenus(menuList,menuVo.getUserId()));
		}
		if ("1".equals(GlobalContext.getProperty("cas.showAllMenu"))||"2".equals(GlobalContext.getProperty("cas.showAllMenu"))) {
			try {
				ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
				Subsystem subsystem = new Subsystem();
				subsystem.setDeleteFlag(0);
				List<Subsystem> subsystemList = subsystemService.queryAll(subsystem);
				List<Map<String, String>> subsystemUrlList = new ArrayList<>();
				for (Subsystem subsystemA : subsystemList) {
					Map<String, String> subsystemMap = new HashMap<>();
					if (!StringUtil.isEmpty(subsystemA.getPermission()) && !StringUtil.isEmpty(subsystemA.getUrl())) {
						subsystemMap.put(subsystemA.getPermission(), CasProperties.getSubsystemUrl(subsystemA));
						subsystemUrlList.add(subsystemMap);
					}
				}
				menuMap.put("subsystemMap", JsonUtil.java2Json(subsystemUrlList));
			} catch (CustomException e) {
				logger.error(e);
				menuMap.put("subsystemMap", "[]");
			}
		} else {
			menuMap.put("subsystemMap", "[]");
		}
		List<Menu> menuList = (List<Menu>)menuMap.get("menuList");
		List<Map<String,Object>> result = new ArrayList<>();
		for (Menu menu : menuList) {
			Map<String,Object> map = new HashMap<>();
			map.put("id", menu.getId());
			map.put("action", menu.getActionName());
			map.put("name", menu.getName());
			map.put("icon",menu.getIcon());
			map.put("parentId", "-1");
			List<Menu> childMenus = menu.getChildmenus();
			if (childMenus != null && childMenus.size() > 0) {
				setMenuMap(map,childMenus);
			}
			result.add(map);
		}
		String json = JsonUtil.java2Json(result);
		menuMap.put("menuJsonStr", json);
		return menuMap;
	}

	private void setMenuMap(Map<String,Object> parentMenu,List<Menu> menuList){
		List<Map<String,Object>> menus = new ArrayList<>();
		for (Menu menu:menuList) {
			Map<String,Object> map = new HashMap<>(5);
			map.put("id", menu.getId());
			map.put("action", menu.getActionName());
			map.put("name", menu.getName());
			map.put("icon",menu.getIcon());
			map.put("parentId", parentMenu.get("id"));
			List<Menu> childMenus = menu.getChildmenus();
			if (childMenus != null && childMenus.size() > 0) {
				setMenuMap(map,childMenus);
			}
			menus.add(map);
		}
		parentMenu.put("children", menus);
	}

	/**
	 * ??????????????????????????????
	 * @param menu
	 * @param rootnode
	 * @return
	 */
	public void portalmenu(Menu menu, String rootnode,List<Menu> portalmenulist) {
		User user = SystemSecurityUtils.getUser();
		if (menu != null) {
			Object[] args = new Object[4];
			if (menu != null) {
				args[0] = menu;
			}
			if (user.getSysUserRole() != null) {
				String roleStr = "";
				List<SysUserRole> userRoles = user.getSysUserRole();
				for (int i = 0; i < userRoles.size(); i++) {
					if ("".equals(roleStr)) {
						roleStr = userRoles.get(i).getRoleId();
					} else {
						roleStr = roleStr + "," + userRoles.get(i).getRoleId();
					}
				}
				args[1] = roleStr;
			}
			args[2] = user;
			if (rootnode != null) {
				args[3] = rootnode;
			}
			ClazzInvoker portalmenu = new ClazzInvoker("com.jc.jcap.portal.service.impl.PortalServiceImpl", "getPortalMenuList", args);
			List<?> protalmenus = (List<?>) portalmenu.getInvoker();
			if (protalmenus != null && protalmenus.size() > 0) {
				portalmenulist.addAll((Collection<? extends Menu>) protalmenus);
			}
		}
	}
	
	/**
	 * @description ????????????????????????
	 * @param   id ??????id
	 * @return Menu ????????????
	 */
	@Override
	public Menu getAddInfo(String id) {
		Menu menuVo = new Menu();
		if ("-1".equals(id)) {
			menuVo.setParentId(id);
			menuVo.setParentname("??????");
			menuVo.setRootNode("-1");
		} else {
			Menu menutemp = new Menu();
			menutemp.setId(id);
			menutemp = menuDao.get(menutemp);
			menuVo.setParentId(id);
			menuVo.setParentname(menutemp.getName());
			menuVo.setRootNode(menutemp.getParentId());
		}
		return menuVo;
	}

	/**
	 * @description ????????????????????????
	 * @param   id ??????id
	 * @return Menu ????????????
	 */
	@Override
	public Menu getEditInfo(String id) {
		Menu menuVo = new Menu();
		menuVo.setId(id);
		menuVo = menuDao.get(menuVo);
		if (!"-1".equals(menuVo.getParentId())) {
			Menu menutemp = new Menu();
			menutemp.setId(menuVo.getParentId());
			menutemp = menuDao.get(menutemp);
			menuVo.setParentname(menutemp.getName());
			menuVo.setRootNode(menuVo.getParentId());
		} else {
		    menuVo.setParentname("??????");
		}
		return menuVo;
	}
	
	/**
	 * @description ?????????????????????????????????id??????
	 * @param   id ??????id
	 * @return ids ????????????
	 */
	@Override
	public String groupMenuId(String id) {
		String ids = id;
		ids = recursionmenuid(id, ids);
		return ids;
	}
	
	private String recursionmenuid(String id,String ids) {
		Menu menuVo = new Menu();
		menuVo.setParentId(id);
		List<Menu> menulist = menuDao.queryAll(menuVo);
		for (int i = 0; menulist != null && i < menulist.size(); i++) {
			String temp = menulist.get(i).getId();
			ids = ids + "," + temp;
			ids = recursionmenuid(temp, ids);
		}
		return ids;
	}
	
	/**
	 * @description ???????????????????????????????????????
	 * @param  leftmenuList ????????????
	 * @return list ????????????
	 */
	public List<Menu> gatherMenus(List<Menu> leftmenuList, String userid) {
		return gatherMenus(leftmenuList, userid);
	}

	/**
	 * ?????????2020-07-21???????????????????????????????????????????????????????????????,subSystemMenuId == null ??????????????????
	 * @param leftmenuList
	 * @param userid
	 * @param subSystemMenuId
	 * @return
	 */
	public List<Menu> gatherMenus(List<Menu> leftmenuList, String userid, String subSystemMenuId){
		List<Menu> returnList = new ArrayList<>();
		if (leftmenuList != null && leftmenuList.size() > 0){
			Map<String,Menu> menuMap = new HashMap<>();
			for (int i = 0; i < leftmenuList.size(); i++) {
				Menu remenuVo = leftmenuList.get(i);
				if (remenuVo.getMenuType() == 0) {
					String key = remenuVo.getParentId() + "-" + remenuVo.getId();
					menuMap.put(key, remenuVo);
				}
			}
			for (int i = 0; i < leftmenuList.size(); i++) {
				Menu menuVo = leftmenuList.get(i);
				if (StringUtil.isEmpty(subSystemMenuId)) {
					if (!"-1".equals(menuVo.getParentId())) {
						continue;
					}
					if (menuVo.getIsShow() != 0) {
						continue;
					}
				} else {
					if (!menuVo.getId().equals(subSystemMenuId)) {
						continue;
					}
				}
				List<Menu> childList = new ArrayList<>();
				for (int k = 0; k < leftmenuList.size(); k++) {
					String getkey = menuVo.getId() + "-" + leftmenuList.get(k).getId();
					if(menuMap.containsKey(getkey)) {
						childList.add(menuMap.get(getkey));
					}
				}
				gatherchildMenus(leftmenuList, childList, menuMap);
				if("1".equals(menuVo.getId())){
					setportalToMenu(childList, menuVo, "power", "");
				}
				menuVo.setChildmenus(childList);
				menuVo.setChildmenussize(childList.size());
				returnList.add(menuVo);
			}
		}
		return returnList;
	}
	
	/**
	 * @description ???????????????????????????????????????
	 * @param   leftmenuList ???????????????,list childmenuList ???????????????,Map menuMap????????????
	 * @return list ????????????
	 */
	public List<Menu> gatherchildMenus(List<Menu> leftmenuList,List<Menu> childmenuList,Map<String,Menu> menuMap){
		for (int i = 0 ; i < childmenuList.size() ; i++) {
			Menu remenuVo = childmenuList.get(i);
			if ("-1".equals(remenuVo.getId())) {
				continue;
			}
			List<Menu> childmenus = new ArrayList<>();
			for (int k = 0; k < leftmenuList.size() ; k++) {
				String getkey = remenuVo.getId() + "-" + leftmenuList.get(k).getId();
				if (menuMap.containsKey(getkey)) {
					childmenus.add(menuMap.get(getkey));
				}
			}
			gatherchildMenus(leftmenuList, childmenus, menuMap);
			if ("1".equals(remenuVo.getId())) {
				setportalToMenu(childmenus, remenuVo, "power", "");
			}
			remenuVo.setChildmenus(childmenus);
			remenuVo.setChildmenussize(childmenus.size());
		}
		return childmenuList;
	}
	
	/**
	 * @description ??????id?????????????????????
	 * @param   id ??????id
	 * @return Map ????????????
	 */
	@Override
	public Map<String,Object> getNavigationMenuById(String id) {
		Map<String,Object> menuMap = new HashMap<>();
		List<Menu> returnlist = new ArrayList<>();
		List<Menu> templist = new ArrayList<>();
		templist = recursionNavigation(id, templist);
		SystemSecurityUtils.getSession().setAttribute("powerDivisionSign", String.valueOf(id));
		if (templist != null) {
			for (int i = templist.size() - 1; i >= 0; i--) {
				returnlist.add(templist.get(i));
			}
			menuMap.put("navigationname", templist.get(0).getName());
			menuMap.put("navigationMenuList", returnlist);
		}
		return menuMap;
	}
	
	/**
	 * @description ??????url?????????????????????
	 * @param   url
	 * @return Map ????????????
	 */
	@Override
	public Map<String,Object> getNavigationMenuByUrl(String url) {
		Map<String,Object> menuMap = new HashMap<>();
		List<Menu> returnlist = new ArrayList<>();
		List<Menu> templist = new ArrayList<>();
		Menu tempvo = new Menu();
		tempvo.setActionName(url);
		List<Menu> queryMenus= menuDao.queryAll(tempvo);
		if (queryMenus != null && queryMenus.size() > 0) {
			templist = recursionNavigation(queryMenus.get(0).getId(), templist);
			SystemSecurityUtils.getSession().setAttribute("powerDivisionSign", String.valueOf(queryMenus.get(0).getId()));
		}
		if (templist != null && templist.size() > 0) {
			for (int i = templist.size() - 1 ; i >= 0 ; i--) {
				returnlist.add(templist.get(i));
			}
			menuMap.put("navigationname", templist.get(0).getName());
			menuMap.put("navigationMenuList", returnlist);
		}
		return menuMap;
	}
	
	private List<Menu> recursionNavigation(String id, List<Menu> menulist) {
		Menu menuVo = new Menu();
		menuVo.setId(id);
		menuVo = menuDao.get(menuVo);
		menulist.add(menuVo);
		if (!"-1".equals(menuVo.getParentId())) {
			menulist = recursionNavigation(menuVo.getParentId(), menulist);
		}
		return menulist;
	}
	
	/**
	 * @description ???????????????????????????
	 * @param   menu ????????????  String seltype ????????????  all???????????? power??????????????????
	 * @return list ????????????
	 */
	@Override
	public List<Menu> getResourceInfo(Menu menu, String seltype) {
		Menu menuVo = new Menu();
		menuVo.setParentId(menu.getId());
		List<Menu> menuList = null;
		if ("power".equals(seltype)) {
			menuVo.setUserId(menu.getUserId());
			menuList = menuDao.queryMenu(menuVo);
		} else {
			menuList = menuDao.queryAll(menuVo);
		}
		String rootnode = "";
		if (!"-1".equals(menu.getId())) {
			menuVo = new Menu();
			menuVo.setId(menu.getId());
			menuVo = menuDao.get(menuVo);
			rootnode = String.valueOf(menuVo.getParentId());
		} else {
			rootnode = "-1";
		}
		for (int i = 0 ; menuList != null && i < menuList.size() ; i++) {
			Menu edvo = menuList.get(i);
			Menu tempvo = new Menu();
			tempvo.setParentId(edvo.getId());
			tempvo.setIsShow(menu.getIsShow());
			edvo.setIsNextNode(String.valueOf(menuDao.getCount(tempvo)));
			edvo.setRootNode(rootnode);
		}
		setportalToMenu(menuList, menu, seltype, rootnode);
		return menuList;
	}
	
	/**
	 * @description ??????????????????????????????????????????
	 * @param   seltype ????????????  all???????????? power??????????????????, list menulist ????????????, Menu menu
	 * @return list ????????????
	 */
	public List<Menu> setportalToMenu(List<Menu> menuList, Menu menu, String seltype, String rootnode){
		if ("power".equals(seltype)) {
			//????????????????????????
			User user = SystemSecurityUtils.getUser();
			List<SysUserRole> userRoles = user.getSysUserRole();
			String roleStr = "";
			if (userRoles != null) {
				for (int i = 0 ; i < userRoles.size(); i++) {
					if ("".equals(roleStr)) {
						roleStr = userRoles.get(i).getRoleId();
					} else {
						roleStr = roleStr + "," + userRoles.get(i).getRoleId();
					}
				}
			}
			if ("yes".equals(GlobalContext.getProperty("portal.load-on"))) {//????????????
				Object[] args = new Object[4];
				args[0] = menu;
				args[1] = roleStr;
				args[2] = user;
				args[3] = rootnode;
				ClazzInvoker portalmenu = new ClazzInvoker("com.jc.jcap.portal.service.impl.PortalServiceImpl","getPortalMenuList",args);
				List<?> protalmenu = (List<?>)portalmenu.getInvoker();
				if (protalmenu != null && protalmenu.size() > 0) {
					menuList.addAll((Collection<? extends Menu>) protalmenu);
				}
			}
		}
		return menuList;
	}

	/**
	 * ????????????id??????????????????????????????
	 * @param id
	 * @return ??????0????????? ??????0??????
	 */
	@Override
	public int getRolesByMenu(String id) {
		int issize = 0;
		try {
			RoleMenus roleMenus = new RoleMenus();
			roleMenus.setMenuId(id);
			List<RoleMenus> templist = roleMenuService.queryAll(roleMenus);
			if(templist != null) {
				issize = templist.size();
			}
		} catch (CustomException e) {
			logger.error(e);
		}
		return issize;
	}

	/**
	 * ????????????????????????
	 * @param menu
	 * @return
	 */
	@Override
	public List<Menu> queryWithRole(Menu menu) {
		List<Menu> menulist = menuDao.queryWithRole(menu);
		if (menulist != null && menulist.size() > 0){
			String idStr = "";
			for (Menu menuVo: menulist) {
				idStr = idStr + "," + menuVo.getId();
			}
			idStr = idStr + ",";
			return filterMenu(idStr,menulist);
		} else {
			return null;
		}
	}

	/**
	 * ????????????????????????????????????????????????
	 * @param idStr
	 * @param menulist
	 * @return
	 */
	private List<Menu> filterMenu(String idStr,List<Menu> menulist){
		List<Menu> menus = new ArrayList<>();
		if (menulist != null && menulist.size() > 0){
			for(Menu menuVo: menulist){
				String parentId = ","+menuVo.getParentId()+",";
				if(idStr.indexOf(parentId) != -1){
					menus.add(menuVo);
				}
			}
			return menus;
		} else {
			return null;
		}
	}

	/**
	 * ????????????id?????????id????????????????????????????????????
	 * @param id
	 * @param userId
	 * @return
	 */
	@Override
	public Menu queryUserMenu(String id, String userId) {
		Menu menuVo = new Menu();
		menuVo.setId(id);
		menuVo.setUserId(userId);
		List<Menu> menuList = menuDao.queryUserMenu(menuVo);
		if(menuList != null && menuList.size() != 0){
			menuVo = menuList.get(0);
		}else {
			menuVo = null;
		}
		return menuVo;
	}

	/**
	 * ????????????id????????????????????????
	 * @param id
	 * @return
	 */
	@Override
	public List<Menu> queryMenuTree(String id) {
		List<Menu> templist = new ArrayList<>();
		Menu menuVo = new Menu();
		menuVo.setDeleteFlag(0);
		menuVo.setMenuType(0);
		menuVo.setIsShow(0);
		menuVo.setId(id);
		menuVo = menuDao.get(menuVo);
		menuVo.setIsChecked(0);
		templist.add(menuVo);
		templist = menuTreeForParentId(id,templist);
		return templist;
	}

	@Override
	public List<Menu> menuTreeForParentId(String id, List<Menu> menulist) {
		Menu menuVo = new Menu();
		menuVo.setParentId(id);
		menuVo.setDeleteFlag(0);
		menuVo.setMenuType(0);
		menuVo.setIsShow(0);
		menuVo.setWeight(99);
		List<Menu> templist = menuDao.queryAll(menuVo);
		if (templist != null && templist.size() > 0) {
			for(int i=0;i<templist.size();i++){
				Menu tempVo = templist.get(i);
				tempVo.setIsChecked(1);
				menulist.add(tempVo);
				menulist= menuTreeForParentId(tempVo.getId(),menulist);
			}
		}
		return menulist;
	}

	/**
	 * @description  ????????????????????????????????????????????????
	 * @param menu
	 * @return
	 */
	@Override
	public List<Menu> queryMTreeForOne(Menu menu) {
		List<Menu> templist = menuDao.queryWithRole(menu);
		if (templist != null && templist.size() > 0) {
			for (int i=0;i<templist.size();i++) {
				templist.get(i).setIsChecked(1);
			}
			Menu menuVo = new Menu();
			menuVo.setId("-1");
			menuVo = menuDao.get(menuVo);
			menuVo.setIsChecked(0);
			templist.add(menuVo);
		}

		return templist;
	}

	@Override
	public Menu queryMenuForUrl(String url, String subSysSign) {
		return menuDao.queryMenuForUrl(url,subSysSign);
	}

	@Override
	public Menu getMenuByHierarchy(String parentNames) {
		return getMenuByHierarchy(parentNames,0);
	}

	@Override
	public Menu getMenuByHierarchy(String parentNames, int deleteFlag) {
		Menu menu = null;
		if (StringUtil.trimIsEmpty(parentNames)) {
			menu = new Menu();
			menu.setId("-1");
		} else {
			String[] parentNameArray = parentNames.split(",");
			String rootMenuId = "-1";
			try {
				if (parentNameArray[0].startsWith("{")) {
					ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
					Subsystem subsystem = new Subsystem();
					subsystem.setPermission(parentNameArray[0].replace("{", "").replace("}", ""));
					subsystem = subsystemService.get(subsystem);
					if (subsystem != null) {
						rootMenuId = subsystem.getMenuid();
                        if(parentNameArray.length == 1){
                            Menu searchMenu = new Menu();
                            searchMenu.setId(rootMenuId);
							Menu tempMenu = get(searchMenu);
							if(tempMenu == null) {
								if(deleteFlag == 1){
									tempMenu = searchMenu;
									tempMenu.setDeleteFlag(deleteFlag);
									tempMenu = get(tempMenu);
									if(tempMenu == null){
										return null;
									}
								}
								return null;
							}
							searchMenu = tempMenu;
                            return searchMenu;
                        }
                    } else {
						return null;
					}
				} else {
					Menu searchMenu = new Menu();
					searchMenu.setName(parentNameArray[0]);
					searchMenu.setParentId(rootMenuId);
					Menu rootMenu = get(searchMenu);
					if (rootMenu != null) {
                        if(parentNameArray.length == 1){
                            return rootMenu;
                        }
						rootMenuId = rootMenu.getId();
					} else {
						return null;
					}
				}
				for (int i = 1; i < parentNameArray.length; i++) {
                    Menu searchMenu = new Menu();
					searchMenu.setName(parentNameArray[i]);
					searchMenu.setParentId(rootMenuId);
					Menu tempMenu = get(searchMenu);
					if(tempMenu == null) {
						if(deleteFlag == 1){
							tempMenu = searchMenu;
							tempMenu.setDeleteFlag(deleteFlag);
							tempMenu = get(tempMenu);
							if(tempMenu == null){
								return null;
							}
						}
						return null;
					}
					searchMenu = tempMenu;
                    if(i == parentNameArray.length - 1) {
                        return searchMenu;
                    }
                    rootMenuId = searchMenu.getId();
				}
			} catch (CustomException cex) {
				logger.error(cex);
				return null;
			}
		}
		return menu;
	}

	@Override
	public List<Menu> queryMenuTreeForMove(String id) {
		Menu menuVo = new Menu();
		menuVo.setMenuType(0);
		List<Menu> menuList  = menuDao.queryAll(menuVo);
		if (menuList != null && menuList.size() > 0){
			String idStr = ",-2";
			for(Menu menus: menuList){
				idStr = idStr+","+menus.getId();
			}
			idStr = idStr + ",";
			menuList = filterMenu(idStr,menuList);
		}
		String ids = ","+groupMenuId(id)+",";
		List<Menu> movelist = new ArrayList<>();
		for (int i=0;i<menuList.size();i++){
			Menu tempVo = menuList.get(i);
			if (ids.indexOf(","+tempVo.getId()+",") == -1){
				movelist.add(tempVo);
			}
		}
		return movelist;
	}

	@Override
	public List<Menu> getMenuByParentId(String parentId) {
		Menu param = new Menu();
		param.setParentId(parentId);
		param.addOrderByField("queue");
		return this.menuDao.queryAll(param);
	}

}