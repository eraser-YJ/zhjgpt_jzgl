package com.jc.sys.service.impl;

import com.jc.sys.domain.*;
import com.jc.sys.service.*;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IUserService;
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
public class SubDelServiceImpl implements ISubDelService {

	private static Logger logger = Logger.getLogger(SubDelServiceImpl.class);

	@Autowired
	private ISubUserService subUserService;

	@Autowired
	private ISubUserRoleService subUserRoleService;

	@Autowired
	private ISubDepartmentRoleGroupService subDepartmentRoleGroupService;

	@Autowired
	private ISubDepartmentService subDepartmentService;

	@Autowired
	private ISubRoleService subRoleService;

	@Autowired
	private ISubRoleMenuService subRoleMenuService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IDepartmentService departmentService;

	public SubDelServiceImpl(){}


	@Override
	public Map<String, Object> copyDeptTree()  {
		Map<String, Object> resultMap = new HashMap<>();

		try{
			//清除部门
			subDepartmentService.deleteAll(new SubDepartment());
			//清除部门角色组关系
			subDepartmentRoleGroupService.deleteAll(new SubDepartmentRoleGroup());
			//清除部门下角色
			subRoleService.deleteAll(new SubRole());
			//清除角色菜单关系
			subRoleMenuService.deleteAll(new SubRoleMenu());
			//清除用户
			subUserService.deleteAll(new SubUser());
			//清除用户角色关系
			subUserRoleService.deleteAll(new SubUserRole());

			//组装
			Department depts = new Department();
			depts.setDeleteFlag(0);
			List<Department> departmentList = departmentService.queryAll(depts);
			User queryuser = new User();
			queryuser.setIsAdmin(0);
			queryuser.setDeleteFlag(0);
			List<User> userList = userService.queryAll(queryuser);
			List<SubDepartment> subDepartments = new ArrayList<>();
			List<SubUser> subUsers = new ArrayList<>();

			for(Department dept:departmentList){
				SubDepartment subDepartment = new SubDepartment();
				subDepartment.setId(dept.getId());
				subDepartment.setDeptType(dept.getDeptType());
				subDepartment.setName(dept.getName());
				subDepartment.setDeleteFlag(0);
				subDepartment.setParentId(dept.getParentId());
				subDepartment.setOrganizationId(dept.getId());
				subDepartment.setQueue(dept.getQueue());
				subDepartments.add(subDepartment);
			}

			for(User user:userList){
				if(!user.getIsSystemAdmin()) {
					SubUser subUser = new SubUser();
					subUser.setId(user.getId());
					subUser.setDeptId(user.getDeptId());
					subUser.setCode(user.getCode());
					subUser.setLoginName(user.getLoginName());
					subUser.setDisplayName(user.getDisplayName());
					subUser.setSex(user.getSex());
					subUser.setOrderNo(user.getOrderNo());
					subUsers.add(subUser);
				}
			}

			//保存
			subDepartmentService.saveList(subDepartments);

			subUserService.saveList(subUsers);

			resultMap.put("success", "true");
			resultMap.put("successMessage","复制成功");
		}catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("success", "false");
			resultMap.put("errorMessage","复制失败");
		}

		return resultMap;
	}
}