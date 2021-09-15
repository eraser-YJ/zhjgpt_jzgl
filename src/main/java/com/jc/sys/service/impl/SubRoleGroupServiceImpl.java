package com.jc.sys.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubRoleGroupDao;
import com.jc.sys.dao.ISubRoleGroupMenuDao;
import com.jc.sys.domain.SubRoleGroup;
import com.jc.sys.domain.SubRoleGroupMenu;
import com.jc.sys.service.ISubRoleGroupService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubRoleGroupServiceImpl extends BaseServiceImpl<SubRoleGroup> implements ISubRoleGroupService {

	private ISubRoleGroupDao subRoleGroupDao;

	@Autowired
	private ISubRoleGroupMenuDao subRoleGroupMenuDao;

	public SubRoleGroupServiceImpl() {
	}

	@Autowired
	public SubRoleGroupServiceImpl(ISubRoleGroupDao subRoleGroupDao) {
		super(subRoleGroupDao);
		this.subRoleGroupDao = subRoleGroupDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRoleGroup 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubRoleGroup subRoleGroup) throws CustomException {
		Integer result = -1;
		SubRoleGroupMenu subRoleGroupMenu = null;
		try {
			subRoleGroupMenu = new SubRoleGroupMenu();
			propertyService.fillProperties(subRoleGroup, true);
			subRoleGroupMenu.setModifyUser(subRoleGroup.getModifyUser());
			subRoleGroupMenu.setModifyDate(subRoleGroup.getModifyDate());
			subRoleGroupMenu.setRoleGroupIds(StringUtils.join(subRoleGroup.getPrimaryKeys(), ","));
			this.subRoleGroupMenuDao.deleteByRoleGroupIds(subRoleGroupMenu);
			result = subRoleGroupDao.delete(subRoleGroup);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subRoleGroup);
			throw ce;
		}
		return result;
	}

}