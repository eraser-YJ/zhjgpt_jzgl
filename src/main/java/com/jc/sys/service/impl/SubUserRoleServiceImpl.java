package com.jc.sys.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.sys.dao.ISubUserRoleDao;
import com.jc.sys.domain.SubRole;
import com.jc.sys.domain.SubUserRole;
import com.jc.sys.service.ISubRoleService;
import com.jc.sys.service.ISubUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubUserRoleServiceImpl extends BaseServiceImpl<SubUserRole> implements ISubUserRoleService {

	@Autowired
	private ISubRoleService subRoleService;

	private ISubUserRoleDao subUserRoleDao;

	public SubUserRoleServiceImpl() {
	}

	@Autowired
	public SubUserRoleServiceImpl(ISubUserRoleDao subUserRoleDao) {
		super(subUserRoleDao);
		this.subUserRoleDao = subUserRoleDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subUserRole 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-20
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubUserRole subUserRole) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(subUserRole, true);
			result = subUserRoleDao.delete(subUserRole);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(subUserRole);
			throw ce;
		}
		return result;
	}

	/**
	 * 按照RoleIds保存数据
	 * 
	 * @param subUserRole
	 * @throws CustomException
	 * @author
	 * @version 2018-04-23
	 */
	@Override
	public void saveByRoleIds(SubUserRole subUserRole) throws CustomException {
		SubRole subRole = null;
		List<SubUserRole> subUserRoleList = null;
		String roleIds = subUserRole.getRoleIds();
		String userId = subUserRole.getUserId();
		String selDeptId = subUserRole.getSelDeptId();
		if (selDeptId != null){
			subUserRole.setDeptId(selDeptId);}
		subUserRoleList = this.subUserRoleDao.getRolesByUserAndDeptId(subUserRole);
		if (subUserRoleList != null) {
			for (SubUserRole sdrg : subUserRoleList) {
				sdrg.setPrimaryKeys(new String[] {sdrg.getId()});
				this.subUserRoleDao.delete(sdrg,false);
			}
		}
		if (roleIds != null && !"".equals(roleIds.trim())) {
			for (String roleId : roleIds.split(",")) {
				subRole = new SubRole();
				subUserRole = new SubUserRole();
				subRole.setId(roleId);
				subRole = this.subRoleService.get(subRole);
				subUserRole.setUserId(userId);
				subUserRole.setRoleId(roleId);
				subUserRole.setRoleName(subRole.getRoleName());
				this.save(subUserRole);
			}
		}
	}

	@Override
	public List<SubUserRole> getRolesByUserAndDeptId(SubUserRole subUserRole) throws CustomException {
		return subUserRoleDao.getRolesByUserAndDeptId(subUserRole);
	}

	@Override
	public void deleteAll(SubUserRole vo) {
		subUserRoleDao.deleteAll(vo);
	}
}