package com.jc.sys.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubRoleDao;
import com.jc.sys.dao.ISubRoleMenuDao;
import com.jc.sys.domain.SubRole;
import com.jc.sys.domain.SubRoleMenu;
import com.jc.sys.service.ISubRoleService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubRoleServiceImpl extends BaseServiceImpl<SubRole> implements ISubRoleService {

	private ISubRoleDao subRoleDao;

	@Autowired
	private ISubRoleMenuDao subRoleMenuDao;

	public SubRoleServiceImpl() {
	}

	@Autowired
	public SubRoleServiceImpl(ISubRoleDao subRoleDao) {
		super(subRoleDao);
		this.subRoleDao = subRoleDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRole 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-18
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubRole subRole) throws CustomException {
		Integer result = -1;
		SubRoleMenu subRoleMenu = null;
		try {
			subRoleMenu = new SubRoleMenu();
			propertyService.fillProperties(subRole, true);
			subRoleMenu.setModifyUser(subRole.getModifyUser());
			subRoleMenu.setModifyDate(subRole.getModifyDate());
			subRoleMenu.setRoleIds(StringUtils.join(subRole.getPrimaryKeys(), ","));
			this.subRoleMenuDao.deleteByRoleIds(subRoleMenu);
			result = subRoleDao.delete(subRole);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subRole);
			throw ce;
		}
		return result;
	}

	@Override
	public void deleteAll(SubRole vo) {
		subRoleDao.deleteAll(vo);
	}

}