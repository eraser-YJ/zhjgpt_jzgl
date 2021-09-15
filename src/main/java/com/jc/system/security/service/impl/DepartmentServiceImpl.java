package com.jc.system.security.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.system.api.util.DeptSyncUtil;
import com.jc.system.common.util.CacheUtils;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
import com.jc.system.group.domain.Group;
import com.jc.system.group.service.IGroupService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.beans.DeptTree;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.beans.UserTree;
import com.jc.system.security.dao.IDepartmentDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.*;
import com.jc.system.security.util.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 组织机构部门service
 * @author Administrator
 * @date 2020-06-29
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department> implements IDepartmentService {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private IDepartmentDao departmentDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAdminSideService adminSideService;
	@Autowired
	private IDicService dicService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private ISystemService systemService;
	public final String CACHE_DEPT_INFO = "dept_info_";

	@Autowired
	public DepartmentServiceImpl(IDepartmentDao departmentDao) {
		super(departmentDao);
		this.departmentDao=departmentDao;
	}

	public DepartmentServiceImpl(){}

	@Override
    public PageManager query(Department department, PageManager page) {
		return departmentDao.queryByPage(department, page);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer deleteByIds(Department department) throws CustomException {
		Integer result = -1;
		try {
			result = departmentDao.delete(department);
			if(result != -1){
				// 清空组织缓存
				CacheUtils.remove(CACHE_DEPT_INFO + department.getId());
				DeptSyncUtil.push(department);
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(department);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer save(Department department) throws CustomException {
		Integer result = -1;
		try {
			result = departmentDao.save(department);
			DeptSyncUtil.push(department);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(department);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer update(Department department) throws CustomException {
		Integer result = -1;
		try {
			result = departmentDao.update(department);
			if(result != -1){
				// 清空组织缓存
				CacheUtils.remove(CACHE_DEPT_INFO + department.getId());
				DeptSyncUtil.push(department);
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(department);
			throw ce;
		}
		return result;
	}

	@Override
    public Department get(Department department) throws CustomException {
		return departmentDao.get(department);
	}

	@Override
    public List<Department> query(Department department) {
		return departmentDao.queryAll(department);
	}

	@Override
    public PageManager searchQuery(Department department, PageManager page) {
		PageManager returnPage = departmentDao.searchQuery(department, page);
		if(department.getDeleteFlag() == 1){
			List<Department> departmentlist = (List<Department>) returnPage.getData();
			// 判断是否有机构管理员关系存在
			String deptIds = "";
			if (departmentlist != null && departmentlist.size() > 0) {
				try {
					for (int i = 0; i < departmentlist.size(); i++) {
						deptIds = departmentlist.get(i).getId() + "," + deptIds;
					}
					deptIds = "," + deptIds;
					User userInfo = SystemSecurityUtils.getUser();
					AdminSide adminSide = new AdminSide();
					adminSide.setUserId(userInfo.getId());
					List<AdminSide> asList = adminSideService.queryAll(adminSide);
					List<Department> defaultDeptList = new ArrayList<>();
					for (AdminSide side : asList) {
						if ("1".equals(side.getIsChecked())) {
							defaultDeptList.addAll(getDeptByDeptIdForDel(side.getDeptId()));
						}
					}
					if (defaultDeptList.size() > 0 && userInfo.getIsAdmin() != 1) {
						int flag = 1;
						for(Department dept : defaultDeptList){
							if (deptIds.indexOf("," + dept.getId() + ",") != -1) {
								flag = 0;
								break;
							}
						}
						if (flag == 1) {
							return page;
						}
					} else {
						if (userInfo.getIsAdmin() != 1) {
                            return page;
                        }
					}
				} catch (CustomException e) {
					logger.error(e);
				}
			}
		}
		return returnPage;
	}

	@Override
    public List<Department> queryTree(Department department) {
		return departmentDao.queryTree(department);
	}

	@Override
    public Department queryOne(Department department) {
		Department reDept = departmentDao.queryOne(department);
		if (!"0".equals(reDept.getParentId())) {
			Department temp = this.getById(reDept.getParentId());
			if (temp != null) {
				reDept.setParentWeight(temp.getWeight());
			}
		} else {
			reDept.setParentWeight(reDept.getWeight());
		}
		return reDept;
	}

	@Override
    public List<Department> queryAllByDeptId(Department department) {
		return departmentDao.queryAllByDeptId(department);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer updateByDeptIds(Department department) {
		return departmentDao.updateByDeptIds(department);
	}

	@Override
    public List<Department> getDeptAndUserByDeptId(String id) {
		return departmentDao.getDeptAndUserByDeptId(id);
	}

	@Override
    public Map<String, Object> logicDeleteById(Department department) throws CustomException {
		Map<String, Object> resultMap = new HashMap<>(5);
		// 判断是否有人员存在
		User userInfo = new User();
		userInfo.setDeptId(department.getId());
		List<User> userList =  userService.queryUserByDeptId(userInfo);
		if (userList != null && userList.size() > 0) {
			resultMap.put("labelErrorMessage", MessageUtils.getMessage("JC_SYS_115"));
			return resultMap;
		}
		List<Department> dList = getDeptAndUserByDeptId(department.getId());
		// 判断是否有角色存在
		String deptIds = "";
		if (dList != null && dList.size() > 0) {
			for (int i = 0; i < dList.size(); i++) {
				deptIds += dList.get(i).getId() + ",";
			}
			deptIds = deptIds.substring(0, deptIds.length() - 1);
		}
		if (deptIds.length() > 0) {
			Role role = new Role();
			role.setDeptIds(deptIds);
			List<Role> roleList = roleService.queryAll(role);
			if (roleList != null && roleList.size() > 0) {
				resultMap.put("labelErrorMessage", MessageUtils.getMessage("JC_SYS_114"));
				return resultMap;
			}
		}
		// 判断是否有机构管理员关系存在
		String[] depts = null;
		if (dList != null && dList.size() > 0) {
			depts = new String[dList.size()];
			for (int i = 0; i < dList.size(); i++) {
				depts[i] = dList.get(i).getId();
			}
		}
		if(depts != null){
			if(adminSideService.queryAdminSideIdByDeptIds(depts)){
				String primarkeys = "";
				for (String deptId : depts) {
					AdminSide adminSide = new AdminSide();
					adminSide.setDeptId(deptId);
					adminSide.setUserId(userInfo.getId());
					adminSide = adminSideService.get(adminSide);
					if (adminSide != null) {
						if ("".equals(primarkeys)) {
							primarkeys = adminSide.getId();
						} else {
							primarkeys = primarkeys + "," + adminSide.getId();
						}
					}
				}
				if (!"".equals(primarkeys)) {
					AdminSide adminSide = new AdminSide();
					adminSide.setPrimaryKeys(primarkeys.split(","));
					adminSideService.delete(adminSide,false);
				}
			}
		}
		List<Department> deptList = departmentDao.queryAllByDeptId(department);
		if (deptList != null && deptList.size() > 0) {
			String[] primaryKeys = new String[deptList.size()];
			for (int i = 0; i < deptList.size(); i++) {
				primaryKeys[i] = deptList.get(i).getId();
			}
			department.setModifyUser(SystemSecurityUtils.getUser().getModifyUser());
			department.setModifyDate(DateUtils.getSysDate());
			department.setDeleteFlag(1);
			department.setPrimaryKeys(primaryKeys);
		} else {
			resultMap.put("success", "true");
			return resultMap;
		}
		if (departmentDao.updateByDeptIds(department) >= 1) {
			DeptSyncUtil.push(department);
			resultMap.put("success", "true");
		} else {
			resultMap.put("success", "false");
		}
		return resultMap;
	}

	@Override
    public PageManager userManageList(User user, PageManager page) {
		if(user.getDeptIds() == null) {
            user.setDeptId("1");
        }
		return userService.query(user, page);
	}

	@Override
    public List<User> searchUserList() {
		List<User> users = new ArrayList<>();
		try {
			List<Department> list = getDepartmentByUserAndRole("");
			User user = new User();
			user.addOrderByField("t.ORDER_NO");
			List<User> uList = userService.queryAll(user);
			if (list != null && list.size() > 0) {
				if (uList != null && uList.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Department deptTmp = list.get(i);
						for (int j = 0; j < uList.size(); j++) {
							User uTmp = uList.get(j);
							if (uTmp.getDeptId().equals(deptTmp.getId())) {
								users.add(uTmp);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return users;
	}

	@Override
    public String getAllDeptAndUser() throws Exception {
		List<Department> list = this.getDeptByUserAndRoleForSelect("");
		User user = new User();
		user.setDeleteFlag(0);
		user.setIsAdmin(0);
		user.addOrderByField("t.ORDER_NO");
		List<User> uList = userService.queryAll(user);
		Object unique = UserUtils.getUnique("unique");
		if (unique != null ){
			String secret = unique.toString();
			if (!"".equals(secret)){
				if ("secret_type_2".equals(secret)){
					secret = "secret_type_2";
				} else if ("secret_type_1".equals(secret)){
					secret = "secret_type_1,secret_type_2";
				} else if ("secret_type_0".equals(secret)){
					secret = "secret_type_2,secret_type_1,secret_type_0";
				}
				List<User> userList = new ArrayList<>();
				for (User users : uList) {
					if (users.getExtStr1() != null && secret.indexOf(users.getExtStr1()) != -1){
						userList.add(users);
					}
				}
				uList.clear();
				uList.addAll(userList);
			}
			UserUtils.clearUnique();
		} else {
			Object userSecret = UserUtils.getUserSecret("userSecret");
			if (userSecret != null ){
				String secret = userSecret.toString();
				if (!"".equals(secret)){
					if ("secret_type_2".equals(secret)){
						secret = "secret_type_2,secret_type_1,secret_type_0";
					} else if ("secret_type_1".equals(secret)){
						secret = "secret_type_1,secret_type_0";
					} else if ("secret_type_0".equals(secret)){
						secret = "secret_type_0";
					}
					List<User> userList = new ArrayList<>();
					for (User users:uList) {
						if (users.getExtStr1() != null && secret.indexOf(users.getExtStr1()) != -1){
							userList.add(users);
						}
					}
					uList.clear();
					uList.addAll(userList);
				}
			}
		}
		return JsonUtil.java2Json(pottingAllDeptAndUser(list,uList));
	}

	@Override
    public String getAllDeptAndUser(String orgId) throws Exception {
		List<Department> list = this.getDeptByUserAndRoleForSelect(orgId);
		User user = new User();
		user.setDeleteFlag(0);
		user.addOrderByField("t.ORDER_NO");
		user.setOrgId(orgId);
		List<User> uList = userService.queryAll(user);
		return JsonUtil.java2Json(pottingAllDeptAndUser(list,uList));
	}

	private ArrayNode pottingAllDeptAndUser(List<Department> list,List<User> uList){
		List<Department> newList = new ArrayList<>();
		if (list != null && list.size() > 0) {
			if (uList != null && uList.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Department deptTmp = list.get(i);
					for (int j = 0; j < uList.size(); j++) {
						User uTmp = uList.get(j);
						if (deptTmp.getId().equals(uTmp.getDeptId())) {
							UserBean uBean = new UserBean();
							uBean.setId(uTmp.getId());
							uBean.setDeptId(uTmp.getDeptId());
							uBean.setDisplayName(uTmp.getDisplayName());
							uBean.setOrderNo(uTmp.getOrderNo());
							uBean.setMobile(uTmp.getMobile());
							uBean.setGroupTel(uTmp.getGroupTel());
							uBean.setEmail(uTmp.getEmail());
							uBean.setLevel(uTmp.getLevel());
							deptTmp.addUserBean(uBean);
						}
					}
					newList.add(deptTmp);
				}
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		Department d = this.getDeptRoot();
		for (Department department : newList) {
			if (department.getParentId().equals(d.getParentId())) {
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("user", department.getUserBeanList());
				recur(newList, department.getId(), obj);
				array.add(obj);
			}
		}
		return array;
	}

	@Override
    public String getDeptAndUserByOnLine() throws Exception {
		List<Department> list = getDepartmentByOrg("");
		List<Principal> uList = SystemSecurityUtils.getOnlineUsers("");
		return JsonUtil.java2Json(pottingOnLineDeptAndUser(list,uList));
	}

	@Override
    public String getDeptAndUserByOnLine(String orgId) throws Exception {
		List<Department> list = getDepartmentByOrg(orgId);
		List<Principal> uList = SystemSecurityUtils.getOnlineUsers(orgId);
		return JsonUtil.java2Json(pottingOnLineDeptAndUser(list,uList));
	}

	private ArrayNode pottingOnLineDeptAndUser(List<Department> list,List<Principal> uList){
		List<User> userList = new ArrayList<>();
		if (uList != null && uList.size() > 0) {
			if (list != null && list.size() > 0) {
				for (int j = 0; j < uList.size(); j++) {
					User tempUser = new User();
					tempUser.setId(uList.get(j).getId());
					User uTmp = userService.getUser(uList.get(j).getId());
					for (int i = 0; i < list.size(); i++) {
						Department deptTmp = list.get(i);
						if (deptTmp.getId().equals(uTmp.getDeptId()) && uTmp.getIsAdmin() == 0) {
							if(!isExistUser(uTmp, userList)){
								UserBean uBean = new UserBean();
								uBean.setId(uTmp.getId());
								uBean.setDeptId(uTmp.getDeptId());
								uBean.setDisplayName(uTmp.getDisplayName());
								uBean.setOrderNo(uTmp.getOrderNo());
								deptTmp.addUserBean(uBean);
								userList.add(uTmp);
							}
						}
					}
				}
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		Department d = this.getDeptRoot();
		for (Department department : list) {
			if (department.getId().equals(d.getId())) {
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("user", department.getUserBeanList());
				recur(list, department.getId(), obj);
				array.add(obj);
			}
		}
		return array;
	}

	/**
	 * 私有方法-递归添加下级菜单
	 * @param deptList
	 * @param id
	 * @param obj
	 * @return
	 */
	private ObjectNode recur(List<Department> deptList, String id, ObjectNode obj) {
		ArrayNode array = JsonUtil.createArrayNode();
		for (int i = 0; i < deptList.size(); i++) {
			if (deptList.get(i).getParentId().equals(id)) {
				ObjectNode sonObj = (ObjectNode) JsonUtil.createNode();
				sonObj.put("id", deptList.get(i).getId());
				sonObj.put("name", deptList.get(i).getName());
				sonObj.put("deptType", deptList.get(i).getDeptType());
				sonObj.put("parentId", deptList.get(i).getParentId());
				sonObj.putPOJO("user", deptList.get(i).getUserBeanList());
				recur(deptList, deptList.get(i).getId(), sonObj);
				array.add(sonObj);
			}
		}
		if (!array.isNull()) {
			obj.put("subDept", array);
		}
		return obj;
	}

	@Override
    public String getPostAndUser() throws Exception {
		return JsonUtil.java2Json(pottingPostAndUser(""));
	}

	@Override
    public String getPostAndUser(String orgId) throws Exception {
		return JsonUtil.java2Json(pottingPostAndUser(orgId));
	}

	private ArrayNode pottingPostAndUser(String orgId) throws Exception {
		List<Department> newList = new ArrayList<>();
		List<Department> dList = new ArrayList<>();
		Dic dic = new Dic();
		dic.setParentId("dutyId");
		dic.setUseFlag(1);
		List<Dic> dicList = dicService.getDicByDuty(dic);
		User user = new User();
		user.addOrderByField("t.ORDER_NO");
		user.setIsAdmin(0);
		if (orgId != null && !"".equals(orgId)){
			user.setOrgId(orgId);
		}
		List<User> uList = userService.queryAll(user);
		List<Department> deptList = getDepartmentByUserAndRole(orgId);
		List<User> deptUser = new ArrayList<>();
		if (deptList != null && deptList.size() > 0) {
			if (uList != null && uList.size() > 0) {
				for (int i = 0; i < deptList.size(); i++) {
					Department deptTmp = deptList.get(i);
					for (int j = 0; j < uList.size(); j++) {
						User uTmp = uList.get(j);
						if (deptTmp.getId().equals(uTmp.getDeptId())) {
							deptUser.add(uTmp);
						}
					}
				}
			}
		}
		for(Dic d : dicList){
			Department dept = new Department();
			dept.setId(d.getId());
			dept.setCode(d.getCode());
			dept.setName(d.getValue());
			dept.setDeptType(1);
			dept.setParentId("49");
			dList.add(dept);
		}
		if (dList != null && dList.size() > 0) {
			if (deptUser != null && deptUser.size() > 0) {
				for (int i = 0; i < dList.size(); i++) {
					Department deptTmp = dList.get(i);
					for (int j = 0; j < deptUser.size(); j++) {
						User uTmp = deptUser.get(j);
						if (deptTmp.getCode().equals(uTmp.getDutyId())) {
							deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		for (Department department : newList) {
			if(department.getUsers() != null && department.getUsers().size() > 0){
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", department.getCode());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("user", department.getUsers());
				array.add(obj);
			}
		}
		return array;
	}

	@Override
    public String getPersonGroupAndUser() {
		List<Department> dList = new ArrayList<>();
		Group group = new Group();
		group.setGroupType("1");
		group.setCreateUser(SystemSecurityUtils.getUser().getId());
		List<Group> groupList= groupService.getAllGroupUsers(group);
		if (groupList != null && groupList.size() > 0) {
			for (Group g : groupList) {
				Department dept = new Department();
				dept.setId(g.getId());
				dept.setCode(g.getName());
				dept.setName(g.getName());
				dept.setDeptType(1);
				dept.setParentId("49");
				dept.setGroupUsers(g.getLstGroupUser());
				dList.add(dept);
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		for (Department department : dList) {
			if(department.getGroupUsers() != null && department.getGroupUsers().size() > 0){
				ObjectNode obj = (ObjectNode)JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("group", department.getGroupUsers());
				array.add(obj);
			}
		}
		return JsonUtil.java2Json(array);
	}

	@Override
    public String getPublicGroupAndUser() {
		List<Department> dList = new ArrayList<>();
		Group group = new Group();
		group.setGroupType("2");
		group.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		List<Group> groupList= groupService.getAllGroupUsers(group);
		if(groupList != null && groupList.size() > 0){
			for(Group g : groupList){
				Department dept = new Department();
				dept.setId(g.getId());
				dept.setCode(g.getName());
				dept.setName(g.getName());
				dept.setDeptType(1);
				dept.setParentId("49");
				dept.setGroupUsers(g.getLstGroupUser());
				dList.add(dept);
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		for (Department department : dList) {
			if(department.getGroupUsers() != null && department.getGroupUsers().size() > 0){
				ObjectNode obj = (ObjectNode)JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("group", department.getGroupUsers());
				array.add(obj);
			}
		}
		return JsonUtil.java2Json(array);
	}

	@Override
	public List<Department> queryOrgTree(Department department) throws CustomException {
		return departmentDao.queryTree(department);
	}

	@Override
	public List<Department> queryManagerDeptTree(String userId) throws CustomException {
		return departmentDao.queryManagerDeptTree(userId);
	}

	@Override
    public void clearDeptAndUserCache() {
		logger.info("部门人员缓存已清除......");
	}

	@Override
	public List<Department> getDepartmentByUserAndRole() throws Exception {
		return getDepartmentByUserAndRole("");
	}

	public List<Department> getDepartmentByUserAndRole(String orgId) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsSystemAdmin() || userInfo.getIsSystemManager()) {
				//超级管理员
				Department dept = new Department();
				dept.setDeleteFlag(0);
				return departmentDao.queryAll(dept);
			} else if (userInfo.getIsAdmin() == 1) {
				//管理员
				return getDeptListForIsAdmin(userInfo,orgId);
			} else {
				//普通用户
				String[] roleId = new String[userInfo.getSysUserRole().size()];
				for (int i = 0; i < userInfo.getSysUserRole().size(); i++) {
					SysUserRole userRole = userInfo.getSysUserRole().get(i);
					roleId[i] = userRole.getRoleId();
				}
				if (roleId.length > 0) {
					// 查询角色关联的机构
					List<RoleBlocks> orgList = roleService.getAllDeptWithRoles(roleId);
					// 角色没有关联机构 默认显示本部门
					if (orgList == null || orgList.size() == 0) {
						orgList = roleService.getDeptRelation(SystemSecurityUtils.getUser().getDeptId());
					}
					// 查询所有部门
					Department dept = new Department();
					dept.setDeleteFlag(0);
					List<Department> allDeptList = departmentDao.queryAll(dept);
					// 返回结果LIST
					List<Department> deptList = new ArrayList<>();
					for (RoleBlocks roleBlock : orgList) {
						// 添加自己
						Department d = new Department();
						d.setId(roleBlock.getDeptId());
						d = departmentDao.get(d);
						if (!isExist(d, deptList)) {
							d.setIsChecked(roleBlock.getIsChecked());
							d.setUserType(3);
							deptList.add(d);
							// 判断选择的机构
							if ("1".equals(roleBlock.getIsChecked())) {
								// 递归添加部门
								getDepts(roleBlock.getDeptId(), deptList, 3, allDeptList);
							} 
						}
					}
					return deptList;
				}
			}
		}
		return null;
	}

	public List<Department> getDeptByUserAndRoleForSelect(String orgId) throws CustomException {
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsSystemAdmin() || userInfo.getIsSystemManager()) {
				//超级管理员
				Department dept = new Department();
				dept.setDeleteFlag(0);
				return departmentDao.queryAll(dept);
			} else if(userInfo.getIsAdmin() == 1) {
				//管理员
				return getDeptListForIsAdmin(userInfo,orgId);
			} else {
				//普通用户
				return getDeptListForCommonUser(userInfo,orgId);
			}
		}
		return null;
	}

	private List<Department> getDeptListForIsAdmin(User userInfo, String orgId) throws CustomException{
		Department dept = new Department();
		dept.setDeleteFlag(0);
		if (orgId != null && !"".equals(orgId)) {
            dept.setOrgId(orgId);
        }
		List<Department> allDeptList = departmentDao.queryAll(dept);
		AdminSide adminSide = new AdminSide();
		adminSide.setUserId(userInfo.getId());
		List<AdminSide> asList = adminSideService.queryAll(adminSide);
		List<Department> deptList = new ArrayList<>();
		// 循环添加机构
		for (AdminSide as : asList) {
			// 添加自己
			Department d = new Department();
			d.setId(as.getDeptId());
			if (!isExist(d, deptList)) {
				d = departmentDao.get(d);
				d.setIsChecked(as.getIsChecked());
				d.setUserType(2);
				deptList.add(d);
				// 递归添加部门	判断有权限的
				if("1".equals(as.getIsChecked())){
					getDepts(as.getDeptId(), deptList, 2, allDeptList);
				}
			}
		}
		String userOrgId = userInfo.getOrgId();
		List<Department> defaultDeptList = departmentDao.getDeptByOrgId(userOrgId);
		List<Department> deptParent = departmentDao.getQueryDeptPidByOrgId(userOrgId);
		if (deptParent != null && deptParent.size() > 0) {
			for (Department d : deptParent) {
				defaultDeptList.add(d);
			}
		}
		defaultDeptList.add(this.getDeptRoot());
		for (Department d : defaultDeptList) {
			deptList.add(d);
		}
		removeDuplicateWithOrder(deptList);
		return deptList;
	}

	private List<Department> getDeptListForCommonUser(User userInfo, String orgid)  throws CustomException{
		String orgId = null;
		if (orgid != null && !"".equals(orgid)) {
            orgId = orgid;
        } else {
            orgId = userInfo.getOrgId();
        }
		List<Department> defaultDeptList = departmentDao.getDeptByOrgId(orgId);
		List<Department> deptParent = departmentDao.getQueryDeptPidByOrgId(orgId);
		if (deptParent != null && deptParent.size() > 0) {
			for (Department d : deptParent) {
				defaultDeptList.add(d);
			}
		}
		defaultDeptList.add(this.getDeptRoot());
		String[] roleId = new String[userInfo.getSysUserRole().size()];
		for (int i = 0; i < userInfo.getSysUserRole().size(); i++) {
			SysUserRole userRole = userInfo.getSysUserRole().get(i);
			roleId[i] = userRole.getRoleId();
		}
		if (roleId.length > 0) {
			//查询角色关联的机构
			List<RoleBlocks> orgList = roleService.getAllDeptWithRolesSelect(roleId);
			if(orgList != null && orgList.size() > 0){
				//查询所有部门
				Department dept = new Department();
				dept.setDeleteFlag(0);
				//返回结果LIST
				for(RoleBlocks roleBlock : orgList){
					List<Department> roleRelationDeptList = getAllParentDeptByDeptId(roleBlock.getDeptId());
					defaultDeptList.removeAll(roleRelationDeptList);
					defaultDeptList.addAll(roleRelationDeptList);
				}
			}
			removeDuplicateWithOrder(defaultDeptList);
			return defaultDeptList;
		}
		return defaultDeptList;
	}

	private List<Department> getAllParentDeptByDeptId(String deptId) {
		return departmentDao.getQueryDeptPidByOrgId(deptId);
	}

	private List<Department> getDepts(String id, List<Department> deptList, int userType, List<Department> allDeptList){
		try {
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
		} catch (Exception e) {
			logger.error(e);
		}
		return deptList;
	}
	
	public List<Department> queryDept(Department dept, List<Department> allDeptList){
		List<Department> result = new ArrayList<>();
		for (Department d : allDeptList) {
			if(d.getParentId().equals(dept.getParentId()) && d.getDeptType().equals(dept.getDeptType())){
				result.add(d);
			}
		}
		return result;
	}
	
	private boolean isExist(Department d, List<Department> list){
		for(Department department : list){
			if(department.getId().equals(d.getId())){
				return true;
			}
		}
		return false;
	}
	
	private boolean isExistUser(User user, List<User> list){
		for(User u : list){
			if(u.getLoginName().equals(user.getLoginName())){
				return true;
			}
		}
		return false;
	}

	@Override
    public String getDeptIdsByOrgId() throws Exception	{
		String deptString = "";
		List<Department> list = getDeptByUserAndRoleForSelect("");
		for (Department d : list) {
			if (d.getDeptType() != 1) {
				deptString += d.getId() + ",";
			}
		}
		return !"".equals(deptString) ? deptString.substring(0, deptString.length() - 1) : "";
	}

	@Override
    public List<Department> getDeptByDeptId(String id) {
		return departmentDao.getDeptByDeptId(id);
	}

	public List<Department> getDepartmentByOrg(String orgid) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsSystemAdmin() || userInfo.getIsSystemManager()) {
				//超级管理员
				Department dept = new Department();
				dept.setDeleteFlag(0);
				return departmentDao.queryAll(dept);
			} else {
				//普通用户
				String orgId;
				if (orgid != null && !"".equals(orgid)) {
                    orgId = orgid;
                } else {
                    orgId = userInfo.getOrgId();
                }
				List<Department> defaultDeptList = departmentDao.getDeptByOrgId(orgId);//机构下只显示部门的
				//获取上级组织
				List<Department> deptParent = departmentDao.getQueryDeptPidByOrgId(orgId);
				if (deptParent != null && deptParent.size() > 0) {
					for (Department d : deptParent) {
						defaultDeptList.add(d);
					}
				}
				//获取上级组织
				defaultDeptList.add(this.getDeptRoot());
				String[] roleId = new String[userInfo.getSysUserRole().size()];
				for (int i = 0; i < userInfo.getSysUserRole().size(); i++) {
					SysUserRole userRole = userInfo.getSysUserRole().get(i);
					roleId[i] = userRole.getRoleId();
				}
				if (roleId.length > 0) {
					//查询角色关联的机构
					List<RoleBlocks> orgList = roleService.getAllDeptWithRolesSelect(roleId);
					if (orgList != null && orgList.size() > 0) {
						//返回结果LIST
						for (RoleBlocks roleBlock : orgList) {
							//查询所有部门
							List<Department> roleRelationDeptList = getAllParentDeptByDeptId(roleBlock.getDeptId());
							defaultDeptList.removeAll(roleRelationDeptList);
							defaultDeptList.addAll(roleRelationDeptList);
						}
					}
					removeDuplicateWithOrder(defaultDeptList);
					return defaultDeptList;
				}
				return defaultDeptList;
			}
		}
		return null;
	}

	@Override
    public void getAllDepts(){
		Department dept = new Department();
		dept.setDeleteFlag(0);
		List<Department> deptList = departmentDao.queryAll(dept);
		if (deptList != null && deptList.size() > 0) {
			for (Department d : deptList) {
				Department department = new Department();
				department.setId(d.getId());
				department.setParentId(d.getParentId());
				department = systemService.queryOrgIdAndName(department,deptList);
				if (department != null) {
					d.setOrgId(department.getId());
					d.setOrgName(department.getName());
					CacheUtils.put(CACHE_DEPT_INFO + d.getId(), d);
				}
			}
		}
	}

	@Override
    public Department getDeptById(Department dept){
		Department d = (Department) CacheUtils.get(CACHE_DEPT_INFO + dept.getId());
		if (d == null) {
			Department tempDept = new Department();
			tempDept.setDeleteFlag(0);
			tempDept.setId(dept.getId());
			d = departmentDao.get(tempDept);
			if (d != null) {
				Department department = new Department();
				department.setId(d.getId());
				department = systemService.queryOrgIdAndName(department);
				d.setOrgId(department.getId());
				d.setOrgName(department.getName());
				CacheUtils.put(CACHE_DEPT_INFO + dept.getId(), d);
			}
		}
		return d;
	}

	private Department getDeptRoot(){
		Department d = new Department();
		d.setParentId("0");
		return departmentDao.get(d);
	}

	private void removeDuplicateWithOrder(List<Department> list) {
		Set<String> set = new HashSet<>();
        List<Department> newList = new ArrayList<>();
        for (Iterator<Department> iter = list.iterator(); iter.hasNext();) {
        	Department element = iter.next();
            if (set.add(element.getId())){
            	newList.add(element);
            }
        } 
        list.clear();
        list.addAll(newList);
	}

	@Override
    public List<Department> getOrgTree() throws CustomException {
		return this.getDeptByUserAndRoleForSelect("");
	}

	@Override
    public List<Department> getOrgTree(String orgId) throws CustomException {
		return this.getDeptByUserAndRoleForSelect(orgId);
	}

	@Override
    public List<Department> getAllOrgNoDeptTree() {
		return departmentDao.getAllOrgNoDeptTree();
	}

	@Override
    public List<Department> getOrgAndPersonTree() throws CustomException {
		List<Department> list = this.getDeptByUserAndRoleForSelect("");
		return pottingOrgAndPersonTree(list,"");
	}

	@Override
    public List<Department> getOrgAndPersonTree(String orgId) throws CustomException {
		List<Department> list = this.getDeptByUserAndRoleForSelect(orgId);
		return pottingOrgAndPersonTree(list,orgId);
	}

	private List<Department> pottingOrgAndPersonTree(List<Department> list,String orgId) throws CustomException  {
		List<Department> newList = new ArrayList<>();
		User user = new User();
		user.setDeleteFlag(0);
		user.addOrderByField("t.ORDER_NO");
		if (orgId != null && !"".equals(orgId)) {
			user.setOrgId(orgId);
		}
		List<User> uList = userService.queryAll(user);
		if (list != null && list.size() > 0) {
			if (uList != null && uList.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Department deptTmp = list.get(i);
					for (int j = 0; j < uList.size(); j++) {
						User uTmp = uList.get(j);
						if (deptTmp.getId().equals(uTmp.getDeptId())) {
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
    public List<DeptTree> getOrgAndPersonDeptTree() throws CustomException {
		List<Department> list = this.getDeptByUserAndRoleForSelect("");
		return pottingOrgAndPersonDeptTree(list,"");
	}

	@Override
    public List<DeptTree> getOrgAndPersonDeptTree(String orgId) throws CustomException {
		List<Department> list = this.getDeptByUserAndRoleForSelect(orgId);
		return pottingOrgAndPersonDeptTree(list,orgId);
	}

	private List<DeptTree> pottingOrgAndPersonDeptTree(List<Department> list,String orgId) throws CustomException  {
		List<DeptTree> newList = new ArrayList<>();
		User user = new User();
		user.setDeleteFlag(0);
		user.addOrderByField("t.ORDER_NO");
		if (orgId != null && !"".equals(orgId)){
			user.setOrgId(orgId);
		}
		List<User> uList = userService.queryAll(user);
		if (list != null && list.size() > 0) {
			if (uList != null && uList.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Department deptTmp = list.get(i);
					DeptTree deptTree = new DeptTree();
					for (int j = 0; j < uList.size(); j++) {
						User uTmp = uList.get(j);
						if (deptTmp.getId().equals(uTmp.getDeptId())) {
							UserTree userTree = new UserTree();
							userTree.setId(uTmp.getId());
							userTree.setDeptId(uTmp.getDeptId());
							userTree.setDisplayName(uTmp.getDisplayName());
							userTree.setIsCheck(uTmp.getIsCheck());
							deptTree.addUser(userTree);
						}
					}
					deptTree.setId(deptTmp.getId());
					deptTree.setParentId(deptTmp.getParentId());
					deptTree.setDeptType(deptTmp.getDeptType());
					deptTree.setIsChecked(deptTmp.getIsChecked());
					deptTree.setName(deptTmp.getName());
					newList.add(deptTree);
				}
			}
		}
		return newList;
	}

	@Override
    public List<Department> getDeptByUserIds(Department dept) {
		return departmentDao.getDeptByUserIds(dept);
	}

	@Override
    public User getDeptLeader(String deptId) {
		Department dept = new Department();
		dept.setId(deptId);
		List<Department> deptList = departmentDao.queryAll(dept);
		if (deptList == null || deptList.size() == 0) {
            return null;
        }
		User uTmp = userService.getUser(deptList.get(0).getLeaderId());
		return uTmp;
	}

	@Override
	public List<Department> getManageOrgByUserId(String userId) {
		AdminSide adminSide = new AdminSide();
		adminSide.setUserId(userId);
		List<AdminSide> asList = null;
		try {
			asList = adminSideService.queryAll(adminSide);
		} catch (CustomException e) {
			logger.error(e);
		}
		List<Department> deptList = new ArrayList<>();
		//循环添加机构
		for (AdminSide as : asList) {
			//添加自己
			Department d = new Department();
			d.setId(as.getDeptId());
			if (!isExist(d, deptList)) {
				d = departmentDao.get(d);
				d.setIsChecked(as.getIsChecked());
				d.setUserType(2);
				deptList.add(d);
			}
		}
		return deptList;
	}

	public List<Department> getDeptByDeptIdForDel(String id) {
		return departmentDao.getDeptByDeptIdForDel(id);
	}

	@Override
    public List<Department> getAllParentDeptAndBrotherDeptByDeptId(String deptId) {
		List<Department> resultList = new ArrayList<>();
		Department selfDepartment = new Department();
		selfDepartment.setId(deptId);
		selfDepartment = departmentDao.queryOne(selfDepartment);
		fillUpSameLevelDeptList(selfDepartment, resultList);
		return resultList;
	}

	private void fillUpSameLevelDeptList(Department department, List<Department> deptList) {
		if (!department.getId().equals("100001")) {
			//直系父级部门
			department.setId(department.getParentId());
			Department parentDepartment = departmentDao.queryOne(department);
			List<Department> sameLevelDepts = departmentDao.getSameLevelDeptByParentId(department.getParentId());
			deptList.addAll(sameLevelDepts);
			fillUpSameLevelDeptList(parentDepartment, deptList);
		}
	}

	@Override
    public List<Department> getAllChildDeptAndBrohterDeptByDeptId(String deptId) {
		List<Department> resultList = new ArrayList<>();
		Department selfDepartment = new Department();
		selfDepartment.setId(deptId);
		selfDepartment = departmentDao.queryOne(selfDepartment);
		//同级部门
		if (!"100001".equals(deptId)) {
			List<Department> sameLevelDepts = departmentDao.getSameLevelDeptByParentId(selfDepartment.getParentId());
			resultList.addAll(sameLevelDepts);
		}
		//子集部门
		selfDepartment = departmentDao.queryOne(selfDepartment);
		fillDownSameLevelDeptList(selfDepartment, resultList);
		return resultList;
	}

	@Override
    public List<Department> getAllChildDeptAndUserByDeptId(String deptId) throws NullPointerException {
		List<Department> resultList = new ArrayList<>();
		Department selfDepartment = new Department();
		selfDepartment.setId(deptId);
		selfDepartment = departmentDao.queryOne(selfDepartment);
		if (selfDepartment == null) {
			throw new NullPointerException("部门id不正确");
		}
		fillDownSameLevelDeptList(selfDepartment, resultList);
		for (Department department : resultList) {
			List<User> users = userService.getUsersByDeptId(department.getId());
			department.setUsers(users);
		}
		return resultList;
	}

	private void fillDownSameLevelDeptList(Department department, List<Department> resultList) {
		List<Department> deptList = departmentDao.getSameLevelDeptByParentId(department.getId());
		if (deptList.size() > 0) {
			resultList.addAll(deptList);
			for (Department dept : deptList) {
				fillDownSameLevelDeptList(dept, resultList);
			}
		}
	}

	@Override
    public List<Department> getSubChildDeptAndUserByDeptId(String id) {
		List<Department> departmentList = departmentDao.getSameLevelDeptByParentId(id);
		for (Department department : departmentList) {
			List<User> users = userService.getUsersByDeptId(department.getId());
			department.setUsers(users);
		}
		return departmentList;
	}

	@Override
    public List<Department> getSameLevelDeptAndUserByDeptId(String id) {
		Department department = new Department();
		department.setId(id);
		department = departmentDao.queryOne(department);
		List<Department> deptList = departmentDao.getSameLevelDeptByParentId(department.getParentId());
		for (Department dept : deptList) {
			List<User> users = userService.getUsersByDeptId(dept.getId());
			dept.setUsers(users);
		}
		return deptList;
	}

	@Override
    public List<User> getAllUsersByDeptId(String deptId) throws NullPointerException {
		List<User> resultList = new ArrayList<>();
		List<Department> deptList = new ArrayList<>();
		Department selfDepartment = new Department();
		selfDepartment.setId(deptId);
		selfDepartment = departmentDao.queryOne(selfDepartment);
		if (selfDepartment == null && !"0".equals(deptId)) {
			throw new NullPointerException("部门id不正确");
		} else {
			selfDepartment = new Department();
			selfDepartment.setId(deptId);
		}
		fillDownSameLevelDeptList(selfDepartment, deptList);
		deptList.add(selfDepartment);
		for (Department department : deptList) {
			List<User> users = userService.getUsersByDeptId(department.getId());
			resultList.addAll(users);
		}
		return resultList;
	}

	@Override
    public Map<String, Object> getUserAndSubChildDeptByDeptId(String deptId) throws NullPointerException {
		Map<String, Object> resultList = new HashMap<>(10);
		if (!"0".equals(deptId)) {
			//1.获取部门人员集合
			List<User> users = new ArrayList<User>();
			resultList.put("users", users);
			//2.获取直属下级部门集合
			List<Department> subDeptList = departmentDao.getSameLevelDeptByParentId(deptId);
			resultList.put("departments", subDeptList);
			//3.获取部门下所有人员数
			List<User> allUsers = getAllUsersByDeptId(deptId);
			resultList.put("userCount", allUsers.size());
		} else {
			//1.获取部门人员集合
			List<User> users = userService.getUsersByDeptId(deptId);
			resultList.put("users", users);
			//2.获取直属下级部门集合
			List<Department> subDeptList = departmentDao.getSameLevelDeptByParentId(deptId);
			resultList.put("departments", subDeptList);
			//3.获取部门下所有人员数
			List<User> allUsers = getAllUsersByDeptId(deptId);
			resultList.put("userCount", allUsers.size());
		}
		return resultList;
	}
	
	@Override
	public List<Department> queryAll(Department department) {
		List<Department> departmentList = dao.queryAll(department);
		List<Department> userCounts = departmentDao.getUserCountForAll();
		Map<String,Department> userCountsById = new HashMap<>();
		Map<String,Department> userCountsByPId = new HashMap<>();
		for (Department deptById:userCounts) {
			userCountsById.put(deptById.getId(),deptById);
		}
		for (Department deptByPId:userCounts) {
			userCountsByPId.put(deptByPId.getParentId() + "-" + deptByPId.getId(),deptByPId);
		}
		for (Department dept : departmentList) {
			dept.setUserCount(getAllUsersByDeptId(dept.getId(), userCountsById, userCountsByPId));
		}
		return departmentList;
	}

	private int getAllUsersByDeptId(String deptId, Map<String, Department> userCountsById, Map<String, Department> userCountsByPId) {
		int userCount = 0;
		userCount = userCount + userCountsById.get(deptId).getUserCount();
		for (Map.Entry<String,Department> pDept : userCountsByPId.entrySet()) {
			if (pDept.getKey().indexOf(deptId + "-") != -1) {
				userCount = getAllUsersByDeptId(pDept.getValue().getId(),userCountsById,userCountsByPId);
			}
		}
		return userCount;
	}

	@Override
	public Department getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		Department param = new Department();
		param.setId(id);
		return this.getDeptById(param);
	}

	@Override
	public Department getByCode(String code) {
		if (StringUtil.isEmpty(code)) {
			return null;
		}
		Department param = new Department();
		param.setCode(code);
		List<Department> deptList = this.departmentDao.queryAll(param);
		if (deptList != null && deptList.size() > 0) {
			return deptList.get(0);
		}
		return null;
	}
}