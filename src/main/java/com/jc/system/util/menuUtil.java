package com.jc.system.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class menuUtil {

    private menuUtil() {
        throw new IllegalStateException("menuUtil class");
    }

    private static IMenuService menuService;

    public static IMenuService getMenuService() {
        if(menuService == null) {
            menuService = SpringContextHolder.getBean(IMenuService.class);
        }
        return menuService;
    }

    public static void saveMenuID(String url, HttpServletRequest request) throws CustomException {
        Menu menu = new Menu();
        Menu menuExtstr2 = new Menu();
        menu.setActionName(url);
        menu =  getMenuService().queryMenuForUrl(url,"sys="+ GlobalContext.getProperty("subsystem.id"));
        if(menu == null){
            menuExtstr2.setExtStr2(GlobalContext.getProperty("subsystem.id"));
            menuExtstr2.setActionName(url);
            menuExtstr2 =  getMenuService().get(menuExtstr2);
        }else{
            request.setAttribute("menuId",menu.getId());
        }
        if(menuExtstr2 != null && menuExtstr2.getId()!=null){
            request.setAttribute("menuId",menuExtstr2.getId());
        }
    }
}
