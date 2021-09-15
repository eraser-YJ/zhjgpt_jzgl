package com.jc.system.security.service;

import java.util.List;
import java.util.Map;

import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Menu;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IMenuService extends IBaseService<Menu>{
	Map<String,Object> desktopMenuForQuery(Menu menuVo);
	Menu getAddInfo(String id);
	Menu getEditInfo(String id);
	String groupMenuId(String id);
	Map<String,Object> getNavigationMenuById(String id);
	Map<String,Object> getNavigationMenuByUrl(String url);
	List<Menu> getResourceInfo(Menu menu,String seltype);
	int getRolesByMenu(String id);

	/**
	 * 用户角色使用菜单
	 * @param menu
	 * @return
	 */
	List<Menu> queryWithRole(Menu menu);
	Menu queryUserMenu(String id,String userId);
	List<Menu> queryMenuTree(String id);
	List<Menu> queryMTreeForOne(Menu menu);
	Menu queryMenuForUrl(String url,String subSysSign);
	Menu getMenuByHierarchy(String parentNames);
	Menu getMenuByHierarchy(String parentNames,int deleteFlag);
	List<Menu> queryMenuTreeForMove(String id);

	/**
	 * 组装当前菜单及其子菜单列表
	 * @param id
	 * @param menulist
	 * @return
	 */
	List<Menu> menuTreeForParentId(String id,List<Menu> menulist);

	/**
	 * 根据父节点获取菜单
	 * @param parentId
	 * @return
	 */
	List<Menu> getMenuByParentId(String parentId);
}