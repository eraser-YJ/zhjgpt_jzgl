package com.jc.sys.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubRoleMenuDao;
import com.jc.sys.domain.SubRoleMenu;
import com.jc.sys.service.ISubRoleMenuService;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class SubRoleMenuServiceImpl extends BaseServiceImpl<SubRoleMenu> implements ISubRoleMenuService {

	@Autowired
	private IMenuService menuService;
	
	private ISubRoleMenuDao subRoleMenuDao;

	public SubRoleMenuServiceImpl() {
	}

	@Autowired
	public SubRoleMenuServiceImpl(ISubRoleMenuDao subRoleMenuDao) {
		super(subRoleMenuDao);
		this.subRoleMenuDao = subRoleMenuDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRoleMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-18
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubRoleMenu subRoleMenu) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subRoleMenu, true);
			result = subRoleMenuDao.delete(subRoleMenu);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subRoleMenu);
			throw ce;
		}
		return result;
	}

	/**
	 * @description 按照MenuIds保存数据
	 * @param  subRoleMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	@Override
	public void saveByMenuIds(SubRoleMenu subRoleMenu) throws CustomException {
		Menu menu = null;
		List<SubRoleMenu> subRoleMenuList = null;
		String menuIds = subRoleMenu.getMenuIds();
		String roleId = subRoleMenu.getRoleId();
		subRoleMenuList = this.subRoleMenuDao.queryAll(subRoleMenu);
		if (subRoleMenuList != null) {
			for (SubRoleMenu srgm : subRoleMenuList) {
				srgm.setPrimaryKeys(new String[] { srgm.getId()});
				this.subRoleMenuDao.delete(srgm);
			}
		} 
		if (menuIds != null && !"".equals(menuIds.trim())) {
			for (String menuId : menuIds.split(",")) {
				menu = new Menu();
				subRoleMenu = new SubRoleMenu();
				menu.setId(menuId);
				menu = this.menuService.get(menu);
				subRoleMenu.setRoleId(roleId);
				subRoleMenu.setMenuId(menuId);
				subRoleMenu.setMenuName(menu.getName());
				this.save(subRoleMenu);
			}
		} 
	}
	
	/**
	 * 按照roleIds删除信息
	 * @param subRoleMenu
	 * @return
	 */
	@Override
	public Integer deleteByRoleIds(SubRoleMenu subRoleMenu) {
		return this.subRoleMenuDao.deleteByRoleIds(subRoleMenu);
	}

	@Override
	public Map<String, Object> getMenuIdsForUserAndDeptId(String userId, String deptId) throws CustomException {
		Map<String, Object> returnMenus = new HashMap<>();
		Map<String,Object> menuIds = new HashMap<>();
		SubRoleMenu subRoleMenu = new SubRoleMenu();
		subRoleMenu.setDeptId(deptId);
		subRoleMenu.setUserId(userId);
		List<SubRoleMenu> subRoleMenuList = subRoleMenuDao.getMenuIdsForUserAndDeptId(subRoleMenu);
		for(SubRoleMenu roleMenu:subRoleMenuList){
			menuIds.put(roleMenu.getMenuId(),roleMenu.getMenuId());
		}
		if (menuIds.isEmpty()){
			Menu menuVo = new Menu();
			List<Menu> menuList = menuService.queryAll(menuVo);
			for (Menu menu:menuList){
				if (menuIds.get(menu.getId()) != null){
					returnMenus.put(menu.getId(),menu);
				}
			}
		}

		return returnMenus;
	}

	@Override
	public void deleteAll(SubRoleMenu vo) {
		subRoleMenuDao.deleteAll(vo);
	}
}