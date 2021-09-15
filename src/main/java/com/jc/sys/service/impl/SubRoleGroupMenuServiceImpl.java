package com.jc.sys.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubRoleGroupMenuDao;
import com.jc.sys.domain.SubRoleGroupMenu;
import com.jc.sys.service.ISubRoleGroupMenuService;
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
public class SubRoleGroupMenuServiceImpl extends BaseServiceImpl<SubRoleGroupMenu> implements ISubRoleGroupMenuService {

	@Autowired
	private IMenuService menuService;

	private ISubRoleGroupMenuDao subRoleGroupMenuDao;

	public SubRoleGroupMenuServiceImpl() {
	}

	@Autowired
	public SubRoleGroupMenuServiceImpl(ISubRoleGroupMenuDao subRoleGroupMenuDao) {
		super(subRoleGroupMenuDao);
		this.subRoleGroupMenuDao = subRoleGroupMenuDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRoleGroupMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubRoleGroupMenu subRoleGroupMenu) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subRoleGroupMenu, true);
			result = subRoleGroupMenuDao.delete(subRoleGroupMenu);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subRoleGroupMenu);
			throw ce;
		}
		return result;
	}

	/**
	 * @description 按照MenuIds保存数据
	 * @param  subRoleGroupMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	@Override
	public void saveByMenuIds(SubRoleGroupMenu subRoleGroupMenu) throws CustomException {
		Menu menu = null;
		List<SubRoleGroupMenu> subRoleGroupMenuList = null;
		String menuIds = subRoleGroupMenu.getMenuIds();
		String roleGroupId = subRoleGroupMenu.getRoleGroupId();
		subRoleGroupMenuList = this.subRoleGroupMenuDao.queryAll(subRoleGroupMenu);
		if (subRoleGroupMenuList != null) {
			for (SubRoleGroupMenu srgm : subRoleGroupMenuList) {
				srgm.setPrimaryKeys(new String[] {srgm.getId()});
				this.subRoleGroupMenuDao.delete(srgm);
			}
		}
		if (menuIds != null && !"".equals(menuIds.trim())) {
			for (String menuId : menuIds.split(",")) {
				menu = new Menu();
				subRoleGroupMenu = new SubRoleGroupMenu();
				menu.setId(menuId);
				menu = this.menuService.get(menu);
				subRoleGroupMenu.setRoleGroupId(roleGroupId);
				subRoleGroupMenu.setMenuId(menuId);
				subRoleGroupMenu.setMenuName(menu.getName());
				this.save(subRoleGroupMenu);
			}
		}
	}

	/**
	 * 按照roleGroupIds删除信息
	 * 
	 * @param subRoleGroupMenu
	 * @return
	 */
	@Override
	public Integer deleteByRoleGroupIds(SubRoleGroupMenu subRoleGroupMenu) {
		return this.subRoleGroupMenuDao.deleteByRoleGroupIds(subRoleGroupMenu);
	}

	@Override
	public Map<String, Object> getMenuIdsForDeptId(String deptId) throws CustomException {
		Map<String, Object> returnMenus = new HashMap<>();
		Map<String,Object> menuIds = new HashMap<>();
		SubRoleGroupMenu subRoleGroupMenu = new SubRoleGroupMenu();
		subRoleGroupMenu.setDeptId(deptId);
		List<SubRoleGroupMenu> subRoleGroupMenuList = subRoleGroupMenuDao.getMenuIdsForDeptId(subRoleGroupMenu);
		for(SubRoleGroupMenu roleGroupMenu:subRoleGroupMenuList){
			menuIds.put(roleGroupMenu.getMenuId(),roleGroupMenu.getMenuId());
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
}