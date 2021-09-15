package com.jc.foundation.plugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.api.ApiServiceContext;
import com.jc.system.api.util.ApiServiceUtil;
import com.jc.system.plugin.PluginRegMenuListener;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.plugin.domain.PluginMenu;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;

public class PluginRegMenuListenerImpl implements PluginRegMenuListener{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected final String defaultMenuRegMode = "remote";

	@Override
	public void regMenu(Plugin plugin) {
		boolean flag;
		if (plugin.getMenus() != null && plugin.getMenus().size() > 0) {
			for (PluginMenu pluginMenu : plugin.getMenus()) {

				if (!defaultMenuRegMode.equals(GlobalContext.getProperty("plugin.menu.reg.mode"))
						|| GlobalContext.SYS_CENTER.equals(GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD))
						|| GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD) == null) {
					flag = regLocalMenu(pluginMenu);
				} else {
					flag = regRemoteMenu(pluginMenu);
				}

				if (flag) {
					logger.error("菜单没有注册成功" + pluginMenu);
					System.exit(-1);
				}
			}
		}
	}
	
	protected boolean regRemoteMenu(final PluginMenu pluginMenu) {
		
		boolean flag = false;

		Map<String, String> params = new HashMap<>();
		
		params.put("name", pluginMenu.getName());
		params.put("action", pluginMenu.getAction());
		params.put("queue", String.valueOf(pluginMenu.getQueue()));
		params.put("parentnames", getSystemMenuNames(pluginMenu.getParentName()));

		JsonResult result = ApiServiceUtil.invoke(GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD),ApiServiceContext.APP_MENU, ApiServiceContext.API_MENU_ADD,
				JsonUtil.java2Json(params));
		if (result == null || result.getStatus() != 0) {
			flag = true;
		}
		return flag;
	}

	protected boolean regLocalMenu(PluginMenu pluginMenu) {
		boolean flag = false;
		String parentId = "-1";

		IMenuService menuService = SpringContextHolder.getBean(IMenuService.class);

		try {
			String menuNameList = pluginMenu.getParentName();
			if (!StringUtil.trimIsEmpty(menuNameList)) {
				menuNameList = getSystemMenuNames(menuNameList);
				Menu parentMenu = menuService.getMenuByHierarchy(menuNameList);

				if (parentMenu == null) {
					logger.error("没有找到上层菜单" + pluginMenu + "，请检查插件配置");
					return false;
				} else {
					parentId = parentMenu.getId();
				}
			}

			Menu newMenu = PluginMenuToMenu.toMenu(pluginMenu,parentId);//pluginMenu.toMenu(parentId);

			Menu menu = new Menu();
			menu.setName(newMenu.getName());
			menu.setParentId(parentId);
			menu.setPermission(newMenu.getPermission());
			menu.setMenuType(newMenu.getMenuType());

			if (menuService.get(menu) != null) {
				logger.warn(pluginMenu.getName() + "此菜单已经注册");
			} else {
				if (newMenu.getActionName().indexOf("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}") != -1){
                    if (GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD) != null) {
						newMenu.setActionName(newMenu.getActionName().replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD)));
						if (newMenu.getPermission() != null) {
                            newMenu.setPermission(newMenu.getPermission().replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD)));
                        }
					}else {
						newMenu.setActionName(newMenu.getActionName().replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", ""));
						if (newMenu.getPermission() != null) {
                            newMenu.setPermission(newMenu.getPermission().replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", ""));
                        }
					}
				}
				Integer cnt = menuService.save(newMenu);
				if (cnt < 1) {
					flag = true;
				}
			}
		} catch (CustomException ex) {
			flag = true;
		}
		return flag;
	}
	
	private String getSystemMenuNames(String menuNameList) {
		if (StringUtil.isEmpty(menuNameList)) {
			return "";
		}
		if (menuNameList.startsWith("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}")) {
			if (GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD) != null) {
				menuNameList = menuNameList.replace(GlobalContext.SYS_SUBSYSTEM_FIELD,
						GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD));
			} else {
				menuNameList = menuNameList.replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "},", "")
						.replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", "");
			}
		}
		return menuNameList;
	}

}
