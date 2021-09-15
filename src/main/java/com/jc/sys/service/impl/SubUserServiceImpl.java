package com.jc.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jc.sys.domain.SubUserRole;
import com.jc.sys.service.ISubUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.impl.BaseServiceImpl;

import com.jc.sys.dao.ISubUserDao;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubUserService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubUserServiceImpl extends BaseServiceImpl<SubUser> implements ISubUserService{


	@Autowired
	private ISubUserRoleService subUserRoleService;

	private ISubUserDao subUserDao;
	
	public SubUserServiceImpl(){}
	
	@Autowired
	public SubUserServiceImpl(ISubUserDao subUserDao){
		super(subUserDao);
		this.subUserDao = subUserDao;
	}


	

	/**
	* @description 根据主键删除多条记录方法
	* @param  subUser 实体类
	* @return Integer 处理结果
	* @author
	* @version  2018-04-04 
	*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(SubUser subUser) throws CustomException{
		Integer result = -1;
		try{

			List<SubUserRole> subUserRoles = new ArrayList<>();
			String[] userIds = subUser.getPrimaryKeys();

			for (String userId:userIds){
				SubUserRole subUserRole = new SubUserRole();
				subUserRole.setUserId(userId);
				subUserRole.setDeptId(subUser.getDeptId());
				List<SubUserRole> subUserRoleList = subUserRoleService.getRolesByUserAndDeptId(subUserRole);
				subUserRoles.addAll(subUserRoleList);

				subUser.setId(userId);
			}

			if (subUserRoles.size() > 0){
				String[] primaryKeys = new String[subUserRoles.size()];
				int i=0;
				for (SubUserRole userRole:subUserRoles){
					primaryKeys[i] = userRole.getId().toString();
					i++;
				}

				SubUserRole userRoles = new SubUserRole();
				userRoles.setPrimaryKeys(primaryKeys);
				subUserRoleService.delete(userRoles,false);
			}

			result = subUserDao.delete(subUser,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(subUser);
			throw ce;
		}
		return result;
	}

	@Override
	public void deleteAll(SubUser vo) {
		subUserDao.deleteAll(vo);
	}

	@Override
	public void reTheme(SubUser vo) {
		subUserDao.reTheme(vo);
	}

}