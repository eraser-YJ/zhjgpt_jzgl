package com.jc.system.common.service.impl;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.StringUtil;
import com.jc.system.common.dao.ICommonDao;
import com.jc.system.common.service.ICommonService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.IAdminSideService;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IRoleService;
import com.jc.system.security.service.IUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class CommonServiceImpl extends BaseServiceImpl<BaseBean> implements ICommonService {

	private static final Logger logger = Logger.getLogger(CommonServiceImpl.class);
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAdminSideService adminSideService;
	@Autowired
	private IRoleService roleService;
	@Resource
	private ICommonDao commonDao;
	@Override
	public String getDBSysdate() throws CustomException {
		return commonDao.getDBSysdate();
	}

	@Override
	public List<Department> getDeptsAndUser() throws Exception{
		List<Department> depts = queryManagerDeptRree();
		Map<String,Department> map = new HashMap<>();
		for(Department dept:depts){
			map.put(dept.getId(), dept);
		}
		User userQuery = new User();
		userQuery.setDeleteFlag(0);
		List<User> users = userService.queryAll(userQuery);
		for(User user:users){
			Department dept = map.get(user.getDeptId());
			if(dept!=null){
				dept.addUser(user);
			}
		}
		List<Department> result = new ArrayList<>();
		result.addAll(map.values());
		return result;
	}

	@Override
	public List getDeptTree() {
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			return departmentService.getDeptByDeptId(userInfo.getOrgId());
		}
		return Collections.emptyList();
	}

	@Override
	public List<Role> getRolesForUser() throws CustomException {
		return filterRolesForUser("");
	}

	@Override
	public List<Role> getRolesForUser(String orgId) throws CustomException {
		return filterRolesForUser(orgId);
	}

	private List<Role> filterRolesForUser(String orgId) throws CustomException {
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if (userInfo.getIsSystemAdmin()) {
				//超级管理员
				return roleService.getRolesForUser(new Role());
			} else if(userInfo.getIsAdmin() == 1) {
				//管理员
				return pottingAdminRoles(userInfo,orgId);
			}
		}
		return Collections.emptyList();
	}

	private List<Role> pottingAdminRoles(User userInfo,String orgId) {
		try {
			AdminSide adminSide = new AdminSide();
			adminSide.setUserId(userInfo.getId());
			List<AdminSide> asList = adminSideService.queryAll(adminSide);
			List<Department> deptList = new ArrayList<>();
			String roleIds = "";
			for(AdminSide as : asList){
				if("1".equals(as.getIsChecked())){
					roleIds = roleIds + as.getDeptId() + ",";
					getDeptsForRole(as.getDeptId(), deptList);
				}
			}
			for(int i=0;i<deptList.size();i++){
				Department d = deptList.get(i);
				roleIds = roleIds + d.getId();
				if(i != deptList.size()-1) {
                    roleIds = roleIds + ",";
				}
			}
			if(roleIds.length() >0){
				if(roleIds.endsWith(",")){
					roleIds = roleIds.substring(0,roleIds.length()-1);
				}
				Role role = new Role();
				role.setDeptIds(roleIds);
				return roleService.getRolesForUser(role);
			}

		} catch (CustomException e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}
	
	public List<Department> getDeptsForRole(String id, List<Department> deptList){
		try {
			//查询所有部门
			Department temp = new Department();
			temp.setDeleteFlag(0);
			List<Department> allDeptList = departmentService.queryAll(temp);
			Department dept = new Department();
			dept.setDeleteFlag(0);
			dept.setParentId(id);
			List<Department> list = departmentService.queryAll(dept);
			for(Department d : list){
				if(!isExist(d, deptList)){
					d.setIsChecked("1");
					deptList.add(d);
					getDepts(d.getId(), deptList, 4, allDeptList);
				}
			}
		} catch (CustomException e) {
			logger.error(e.getMessage());
		}
		return deptList;
	}

	@Override
	public List queryManagerDeptRree() {
		return filterManagerDeptRree("");
	}

	@Override
	public List queryManagerDeptRree(String orgid) {
		return filterManagerDeptRree(orgid);
	}

	private List filterManagerDeptRree(String orgid){
		try {
			User userInfo = SystemSecurityUtils.getUser();
			if(userInfo != null){
				if(userInfo.getIsSystemAdmin() || userInfo.getIsSystemSecurity() || userInfo.getIsSystemManager()){
					//超级管理员
					Department dept = new Department();
					dept.setDeleteFlag(0);
					return departmentService.queryAll(dept);
				} else if(userInfo.getIsAdmin() == 1){
					//管理员
					//查询所有部门
					return pottingIsAdmin(userInfo,orgid);
				} else {
					//普通用户
					return pottingCustomer(userInfo,orgid);
				}
			}
		} catch (CustomException e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}

	private List pottingIsAdmin(User userInfo,String orgid) throws CustomException{
		Department dept = new Department();
		dept.setDeleteFlag(0);
		if (orgid != null && !"".equals(orgid)) {
            dept.setOrgId(orgid);
        }
		List<Department> allDeptList = departmentService.queryAll(dept);
		//查询在管理员机构树中选择的节点
		AdminSide adminSide = new AdminSide();
		adminSide.setUserId(userInfo.getId());
		List<AdminSide> asList = adminSideService.queryAll(adminSide);
		//返回结果LIST
		List<Department> deptList = new ArrayList<>();
		//循环添加机构
		for(AdminSide as : asList){
			//添加自己
			Department d = new Department();
			d.setId(as.getDeptId());
			if(!isExist(d, deptList)){
				d = departmentService.get(d);
				d.setIsChecked(as.getIsChecked());
				d.setUserType(2);
				deptList.add(d);
				//递归添加部门	判断有权限的
				if("1".equals(as.getIsChecked())){
					getDepts(as.getDeptId(), deptList, 2, allDeptList);
				}
			}
		}
		return deptList;
	}

	private List pottingCustomer(User userInfo,String orgid) throws CustomException{
		List<Department> deptList = new ArrayList<>();
		String[] roleId = new String[userInfo.getSysUserRole().size()];
		for(int i=0;i<userInfo.getSysUserRole().size();i++){
			SysUserRole userRole = userInfo.getSysUserRole().get(i);
			roleId[i] = userRole.getRoleId();
		}
		if(roleId.length > 0){
			//查询角色关联的机构
			List<RoleBlocks> orgList = roleService.getAllDeptWithRoles(roleId);
			//角色没有关联机构 默认显示本部门
			if(orgList == null || orgList.isEmpty()){
				orgList = roleService.getDeptRelation(SystemSecurityUtils.getUser().getDeptId());
			}
			//查询所有部门
			Department dept = new Department();
			dept.setDeleteFlag(0);
			List<Department> allDeptList = departmentService.queryAll(dept);

			//返回结果LIST
			for(RoleBlocks roleBlock : orgList){
				//添加自己
				Department d = new Department();
				d.setId(roleBlock.getDeptId());
				d = departmentService.get(d);
				filterDepts(d,roleBlock.getDeptId(),roleBlock.getIsChecked(),deptList,allDeptList,3);
			}
			return deptList;
		} else {
			return deptList;
		}
	}

	private void filterDepts(Department d,String deptId,String isChecked,List<Department> deptList,List<Department> allDeptList,int userType){
		if(!isExist(d, deptList)){
			d.setIsChecked(isChecked);
			d.setUserType(userType);
			deptList.add(d);
			//判断选择的机构
			if("1".equals(isChecked)){
				//递归添加部门
				getDepts(deptId, deptList, userType, allDeptList);
			}
		}
	}

	public List<Department> getDepts(String id, List<Department> deptList, int userType, List<Department> allDeptList){
		Department dept = new Department();
		dept.setParentId(id);
		dept.setDeptType(0);
		List<Department> list = queryDept(dept, allDeptList);
		for(Department d : list){
			if(!isExist(d, deptList)){
				d.setIsChecked("1");
				d.setUserType(userType);
				deptList.add(d);
				getDepts(d.getId(), deptList, userType, allDeptList);
			}
		}
		return deptList;
	}
	
	public List<Department> queryDept(Department dept, List<Department> allDeptList){
		List<Department> result = new ArrayList<>();
		for(Department d : allDeptList){
			if(d.getParentId().equals(dept.getParentId()) && d.getDeptType().equals(dept.getDeptType())){
				result.add(d);
			}
		}
		return result;
	}
	
	public boolean isExist(Department d, List<Department> list){
		for(Department department : list){
			if(department.getId().equals(d.getId())){
				return true;
			}
		}
		return false;
	}

	@Override
	public List queryManagerOrgRree(){
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			//超级管理员
			if(userInfo.getIsSystemAdmin()){
				try {
					Department department = new Department();
					department.setDeleteFlag(0);
					department.setDeptType(1);
					return departmentService.queryOrgTree(department);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			//管理员
			else if(userInfo.getIsAdmin() == 1){
				AdminSide adminSide = new AdminSide();
				adminSide.setUserId(userInfo.getId());
				return adminSideService.queryManagerDeptRree(adminSide);
			}
		}
		return Collections.emptyList();
	}

	@Override
	public List<Department> getDeptAndUserByDeptId(String id) throws Exception {
		List<Department> newList = new ArrayList<>();
		List<Department> list = departmentService.getDeptAndUserByDeptId(id);
		User user = new User();
		user.setDeleteFlag(0);
		List<User> uList = userService.queryAll(user);
		if(list != null && !list.isEmpty()){
			if(uList != null && !uList.isEmpty()){
				for(int i=0;i<list.size();i++){
					Department deptTmp = list.get(i);
					for(int j=0;j<uList.size();j++){
						User uTmp = uList.get(j);
						if(deptTmp.getId().equals(uTmp.getDeptId())){
							deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}
		}
		return newList;
	}

	@Override
	public List<User> getLeaderUserByDeptId(User user) throws Exception {
		List<User> leaderList = new ArrayList<>();
		List<Department> list = departmentService.getDeptAndUserByDeptId(user.getOrgId());
		List<String> tmpDeptId = new ArrayList<>();
		if(list != null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Department deptTmp = list.get(i);
				if(deptTmp.getDeptType() == 1 && !"0".equals(deptTmp.getParentId())){
					driverByOrgIdRecursive(deptTmp.getId(), list, tmpDeptId);
					if(!user.getOrgId().equals(deptTmp.getId())) {
                        tmpDeptId.add(deptTmp.getId());
                    }
				}
			}
			deleteRepeat(tmpDeptId);
			if(tmpDeptId != null && !tmpDeptId.isEmpty()){
				for(int j=0;j<tmpDeptId.size();j++){
					String id = tmpDeptId.get(j);
					for(int i=list.size()-1;i>=0;i--){
						Department deptTmp = list.get(i);
						if(id.equals(deptTmp.getParentId())){
							list.remove(i);
						}
					}
				}
			}
		}else{
			return Collections.emptyList();
		}
		User tmp = new User();
		user.setDeleteFlag(0);
		List<User> uList = userService.queryAll(tmp);
		if(uList != null && !uList.isEmpty()){
			for(int i=0;i<list.size();i++){
				Department deptTmp = list.get(i);
				for(int j=0;j<uList.size();j++){
					User uTmp = uList.get(j);
					if(deptTmp.getId().equals(uTmp.getDeptId())){
						if(uTmp.getIsLeader().equals(1)){
							if("1".equals(uTmp.getOpenCale())){
								leaderList.add(uTmp);
							}
						}
						
					}
				}
			}
		}
		return leaderList;
	}

	@Override
	public List<User> getDriverByDeptId(String id) throws Exception {
		List<Department> newList = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		List<Department> list = departmentService.getDeptAndUserByDeptId(id);
		User user = new User();
		user.setDeleteFlag(0);
		user.setIsDriver(1);
		List<User> uList = userService.queryAll(user);
		if(list != null && !list.isEmpty()){
			if(uList != null && !uList.isEmpty()){
				for(int i=0;i<list.size();i++){
					Department deptTmp = list.get(i);
					for(int j=0;j<uList.size();j++){
						User uTmp = uList.get(j);
						if(deptTmp.getId().equals(uTmp.getDeptId())){
							deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}
		}
		for(Department d : newList){
			if(d.getUsers() != null && !d.getUsers().isEmpty()){
				for(User u : d.getUsers()) {
                    userList.add(u);
                }
			}
		}
		return userList;
	}

	@Override
	public List<User> getDriverByOrgId(String orgId) throws Exception {
		List<Department> deptList = departmentService.getDeptByDeptId(orgId);
		if(deptList != null && !deptList.isEmpty()){
			List<Department> newList = new ArrayList<>();
			List<User> userList = new ArrayList<>();
			User user = new User();
			user.setDeleteFlag(0);
			user.setIsDriver(1);
			List<User> uList = userService.queryAll(user);
			if(uList != null && !uList.isEmpty()){
				for(int i=0;i<deptList.size();i++){
					Department deptTmp = deptList.get(i);
					for(int j=0;j<uList.size();j++){
						User uTmp = uList.get(j);
						if(deptTmp.getId().equals(uTmp.getDeptId())){
							deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}else{
				return Collections.emptyList();
			}
			for(Department d : newList){
				if(d.getUsers() != null && !d.getUsers().isEmpty()){
					for(User u : d.getUsers()) {
                        userList.add(u);
                    }
				}
			}
			return userList;
		}else{
			return Collections.emptyList();
		}
	}

	@Override
	public List<UserBean> getOnlineUserBean() {
		return SystemSecurityUtils.getOnlineUserBean();
	}


	@Override
	public User getUserById(String id){
		return userService.getUser(id);
	}

	@Override
	public int getOnlineUserCount() {
		return SystemSecurityUtils.getOnlineCount();
	}
	
	private void driverByOrgIdRecursive(String id, List<Department> list, List<String> tmpDeptId){
		for(int i=0;i<list.size();i++){
			Department department = list.get(i);
			if(id.equals(department.getParentId())){
				String deptId = department.getId();
				tmpDeptId.add(deptId);
				driverByOrgIdRecursive(deptId, list, tmpDeptId);
			}
		}
	}

	private void deleteRepeat(List list) { 
		HashSet h = new HashSet(list); 
	    list.clear(); 
	    list.addAll(h); 
	}

	@Override
	public List<Department> getDeptByDeptId(String deptId) throws Exception {
		List<Department> deptList = departmentService.getDeptByDeptId(deptId);
		return deptList != null && !deptList.isEmpty() ? deptList : null;
	}

	@Override
	public List<User> getUsersByOrgId(String orgId) throws Exception {
		Department dp = new Department();
		dp.setId(orgId);
		List<Department> deptList = departmentService.queryAllByDeptId(dp);
		if(deptList != null && !deptList.isEmpty()){
			List<Department> newList = new ArrayList<>();
			List<User> userList = new ArrayList<>();
			User user = new User();
			user.setDeleteFlag(0);
			List<User> uList = userService.queryAll(user);
			if(uList != null && !uList.isEmpty()){
				for(int i=0;i<deptList.size();i++){
					Department deptTmp = deptList.get(i);
					for(int j=0;j<uList.size();j++){
						User uTmp = uList.get(j);
						if(deptTmp.getId().equals(uTmp.getDeptId())){
							deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}else{
				return Collections.emptyList();
			}
			for(Department d : newList){
				if(d.getUsers() != null && !d.getUsers().isEmpty()){
					for(User u : d.getUsers()) {
                        userList.add(u);
                    }
				}
			}
			return userList;
		}else{
			return Collections.emptyList();
		}
	}

	@Override
	public String[] getPermissionOrg(String[] orgReult) throws CustomException {
		try {
			User user = SystemSecurityUtils.getUser();
			StringBuilder orgStrBuf = new StringBuilder();
			List<SysUserRole> sysUserRoleList = user.getSysUserRole();
			String[] roleId = new String[sysUserRoleList.size()];
			for(int i=0	;i<sysUserRoleList.size();i++){
				SysUserRole userRole = sysUserRoleList.get(i);
				roleId[i] = userRole.getRoleId();
			}
			List<RoleBlocks> orgList = roleService.getAllDeptWithRolesPermission(roleId);
			for(int k=0	;k<orgList.size();k++){
				orgStrBuf.append(orgList.get(k).getDeptId());
				orgStrBuf.append(",");
			}
			if(orgStrBuf.length() >0) {
                orgReult = orgStrBuf.substring(0, orgStrBuf.length()-1).split(",");
            }
		} catch (CustomException e) {
			logger.error(e.getMessage());
		}
		return orgReult;
	}

}
