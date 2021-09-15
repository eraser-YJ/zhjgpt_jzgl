package com.jc.foundation.plugin;

import com.jc.system.plugin.domain.PluginMenu;
import com.jc.system.security.domain.Menu;

public class PluginMenuToMenu {

	public static Menu toMenu(PluginMenu pluginMenu, String parentId) {
		Menu menu = new Menu();
        menu.setName(pluginMenu.getName());
        menu.setActionName(pluginMenu.getAction());
        menu.setQueue(pluginMenu.getQueue());
        menu.setParentId(parentId);
        menu.setIcon(pluginMenu.getIcon());
        menu.setPermission(pluginMenu.getPermission());
        menu.setMenuType(pluginMenu.getMenuType());
        menu.setIsShow(0);
        menu.setWeight(pluginMenu.getWeight());
        return menu;
	}
	
}
