package com.jc.sys.service.impl;

import com.jc.foundation.util.JsonUtil;
import com.jc.sys.service.ISubMenuService;
import com.jc.sys.service.ISubRoleGroupMenuService;
import com.jc.sys.service.ISubRoleMenuService;
import com.jc.system.security.domain.Menu;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubMenuServiceImpl implements ISubMenuService {

    private static Logger logger = Logger.getLogger(SubMenuServiceImpl.class);
    @Autowired
    private ISubRoleGroupMenuService subRoleGroupMenuService;

    @Autowired
    private ISubRoleMenuService subRoleMenuService;

    @Override
    public Map<String, Object> desktopMenuForSub(String userId, String deptId) {
        Map<String,Object> menuMap = new HashMap<String,Object>();
        try{
            Map<String,Object> filterMenu = new HashMap<>();
            Map<String,Object> groupMenus = subRoleGroupMenuService.getMenuIdsForDeptId(deptId);
            filterMenu.putAll(groupMenus);
            Map<String,Object> roleMenus = subRoleMenuService.getMenuIdsForUserAndDeptId(userId,deptId);
            filterMenu.putAll(roleMenus);
            List<Menu> menus = new ArrayList<>();
            for (String key : filterMenu.keySet()) {
                menus.add((Menu)filterMenu.get(key));
            }
            if (menus.size() > 0 ){
                List<Menu> returnmenuList = new ArrayList<>();//一次取出所有菜单
                List<Menu> tempMenus = gatherMenus(menus);
                for (Menu tempvo:tempMenus){
                    if("-1".equals(tempvo.getParentId())){
                        if (tempvo.getChildmenus() != null && tempvo.getChildmenus().size()>0){
                            returnmenuList.addAll(tempvo.getChildmenus());
                        }
                    }
                }
                menuMap.put("menuList",returnmenuList);
                List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
                for(Menu menu:returnmenuList){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("id", menu.getId());
                    map.put("action", menu.getActionName());
                    map.put("name", menu.getName());
                    map.put("icon",menu.getIcon());
                    map.put("parentId", "-1");
                    List<Menu> childMenus = menu.getChildmenus();
                    if(childMenus!=null&&childMenus.size()>0){
                        setMenuMap(map,childMenus);
                    }
                    result.add(map);
                }
                String json = JsonUtil.java2Json(result);
                menuMap.put("menuJsonStr", json);
            } else {
                menuMap.put("menuList",menus);
            }

        } catch (Exception e){
            logger.error(e.getMessage());
        }
        menuMap.put("subsystemMap", "[]");
        return menuMap;
    }

    private List<Menu> gatherMenus(List<Menu> leftmenuList){
        List<Menu> returnlist = new ArrayList<Menu>();
        if(leftmenuList != null && leftmenuList.size() > 0){
            Map<String,Menu> menuMap = new HashMap<String,Menu>();
            for(int i=0;i<leftmenuList.size();i++){
                Menu remenuVo = leftmenuList.get(i);
                if(remenuVo.getMenuType() == 0){
                    String key = remenuVo.getParentId()+"-"+remenuVo.getId();
                    menuMap.put(key, remenuVo);
                }
            }

            for(int i=0;i<leftmenuList.size();i++){
                Menu remenuVo = leftmenuList.get(i);
                if(!"-1".equals(remenuVo.getParentId())) {continue;}
                if(remenuVo.getIsShow() != 0){continue;}
                List<Menu> childmenus = new ArrayList<Menu>();
                for(int k=0;k<leftmenuList.size();k++){
                    String getkey = remenuVo.getId()+"-"+leftmenuList.get(k).getId();
                    if(menuMap.containsKey(getkey)){
                        childmenus.add(menuMap.get(getkey));}
                }
                gatherchildMenus(leftmenuList,childmenus,menuMap);
                remenuVo.setChildmenus(childmenus);
                remenuVo.setChildmenussize(childmenus.size());
                returnlist.add(remenuVo);
            }
        }
        return returnlist;
    }

    private List<Menu> gatherchildMenus(List<Menu> leftmenuList,List<Menu> childmenuList,Map<String,Menu> menuMap){
        for(int i=0;i<childmenuList.size();i++){
            Menu remenuVo = childmenuList.get(i);
            if("-1".equals(remenuVo.getId())){
                continue;}
            List<Menu> childmenus = new ArrayList<Menu>();
            for(int k=0;k<leftmenuList.size();k++){
                String getkey = remenuVo.getId()+"-"+leftmenuList.get(k).getId();
                if(menuMap.containsKey(getkey)){
                    childmenus.add(menuMap.get(getkey));}
            }
            gatherchildMenus(leftmenuList,childmenus,menuMap);
            remenuVo.setChildmenus(childmenus);
            remenuVo.setChildmenussize(childmenus.size());
        }

        return childmenuList;
    }

    private void setMenuMap(Map<String,Object> parentMenu,List<Menu> menuList){
        List<Map<String,Object>> menus = new ArrayList<Map<String,Object>>();
        for(Menu menu:menuList){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id", menu.getId());
            map.put("action", menu.getActionName());
            map.put("name", menu.getName());
            map.put("icon",menu.getIcon());
            map.put("parentId", parentMenu.get("id"));
            List<Menu> childMenus = menu.getChildmenus();
            if(childMenus!=null&&childMenus.size()>0){
                setMenuMap(map,childMenus);
            }
            menus.add(map);
        }
        parentMenu.put("children", menus);
    }
}
