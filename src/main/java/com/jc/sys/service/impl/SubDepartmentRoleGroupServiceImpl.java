package com.jc.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubDepartmentRoleGroupDao;
import com.jc.sys.dao.ISubRoleGroupDao;
import com.jc.sys.domain.SubDepartmentRoleGroup;
import com.jc.sys.domain.SubRoleGroup;
import com.jc.sys.service.ISubDepartmentRoleGroupService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubDepartmentRoleGroupServiceImpl extends BaseServiceImpl<SubDepartmentRoleGroup> implements ISubDepartmentRoleGroupService {

	@Autowired
	private ISubRoleGroupDao subRoleGroupDao;

	private ISubDepartmentRoleGroupDao subDepartmentRoleGroupDao;

	public SubDepartmentRoleGroupServiceImpl() {
	}

	@Autowired
	public SubDepartmentRoleGroupServiceImpl(ISubDepartmentRoleGroupDao subDepartmentRoleGroupDao) {
		super(subDepartmentRoleGroupDao);
		this.subDepartmentRoleGroupDao = subDepartmentRoleGroupDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subDepartmentRoleGroup 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-20
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubDepartmentRoleGroup subDepartmentRoleGroup) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subDepartmentRoleGroup, true);
			result = subDepartmentRoleGroupDao.delete(subDepartmentRoleGroup);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subDepartmentRoleGroup);
			throw ce;
		}
		return result;
	}

	/**
	 * @description 按照groupIds保存数据
	 * @param  subDepartmentRoleGroup 实体类
	 * @return void
	 * @author
	 * @version 2018-04-20
	 */
	@Override
	public void saveByGroupIds(SubDepartmentRoleGroup subDepartmentRoleGroup) throws CustomException {
		SubRoleGroup subRoleGroup = null;
		List<SubDepartmentRoleGroup> subDepartmentRoleGroupList = null;
		String roleGroupIds = subDepartmentRoleGroup.getRoleGroupIds();
		String deptId = subDepartmentRoleGroup.getDeptId();
		SubDepartmentRoleGroup selSubDepartmentRoleGroup = new SubDepartmentRoleGroup();
		selSubDepartmentRoleGroup.setDeptId(deptId);
		subDepartmentRoleGroupList = this.subDepartmentRoleGroupDao.queryAll(selSubDepartmentRoleGroup);
		if (subDepartmentRoleGroupList != null) {
			for (SubDepartmentRoleGroup sdrg : subDepartmentRoleGroupList) {
				sdrg.setPrimaryKeys(new String[] {sdrg.getId()});
				this.subDepartmentRoleGroupDao.delete(sdrg,false);
			}
		}
		if (roleGroupIds != null && !"".equals(roleGroupIds.trim())) {
			for (String roleGroupId : roleGroupIds.split(",")) {
				subRoleGroup = new SubRoleGroup();
				subDepartmentRoleGroup = new SubDepartmentRoleGroup();
				subRoleGroup.setId(roleGroupId);
				subRoleGroup = this.subRoleGroupDao.get(subRoleGroup);
				subDepartmentRoleGroup.setDeptId(deptId);
				subDepartmentRoleGroup.setRoleGroupId(roleGroupId);
				subDepartmentRoleGroup.setRoleGroupName(subRoleGroup.getGroupName());
				this.save(subDepartmentRoleGroup);
			}
		}
	}

	@Override
	public void deleteAll(SubDepartmentRoleGroup vo) {
		subDepartmentRoleGroupDao.deleteAll(vo);
	}
}