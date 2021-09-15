package com.jc.system.security.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jc.crypto.utils.SM2Utils;
import com.jc.foundation.util.*;
import com.jc.system.api.util.UserSyncUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.*;
import com.jc.system.security.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.SystemException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.content.domain.AttachBusiness;
import com.jc.system.content.service.IAttachBusinessService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.dao.IUserDao;
import com.jc.system.security.service.IAdminSideService;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IRoleService;
import com.jc.system.security.service.ISysOtherDeptsService;
import com.jc.system.security.service.ISysUserRoleService;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.service.IUserService;

/**
 * 用户接口实现
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    protected transient final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	public UserServiceImpl(IUserDao dao) {
		super(dao);
		this.userDao =dao;
	}
	public UserServiceImpl(){
	}
	
	private IUserDao userDao;
	
	@Autowired
	private ISysOtherDeptsService sysOtherDeptsService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
	private IAdminSideService adminSideService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private ISystemService systemService;
	@Autowired
	private IAttachBusinessService attachBusinessService;
	@Autowired
	private IRoleService roleService;

	public static final String SQL_QUERYALLUSERCOUNT= "queryAllUsersCount";
	
	public static final String SQL_QUERYALLUSER= "queryAllUsers";
	
	public static final String SQL_QUERYALLUSERCOUNTFORORDER= "queryAllUsersCountForOrder";
	
	public static final String SQL_QUERYALLUSERFORORDER= "queryAllUsersForOrder";
	
	public static final String CACHE_USER_INFO = "user_info_";
	
	public static final String CACHE_DEPT_INFO = "dept_info_";
	
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void saveUserPhoto(User user) throws CustomException {
		attachBusinessService.deleteUserPhoto(user.getId());
		String relt = user.getFileids();
		if (relt != null && !"".equals(relt)) {
			String[] files = user.getFileids().split(",");
			for (int i = 0; i < files.length; i++) {
	            AttachBusiness attachBusiness = new AttachBusiness();
				attachBusiness.setAttachId(files[i]);
				attachBusiness.setBusinessId(user.getId());
				attachBusiness.setBusinessTable("tty_sys_user");
				attachBusiness.setBusinessSource("0");
				attachBusinessService.saveUserPhoto(attachBusiness);
	        }
		}
	}
	
	private void saveUserPhotoNotTran(User user) throws CustomException {
		attachBusinessService.deleteUserPhoto(user.getId());
		String fileIds = user.getFileids();
		if (fileIds != null && !"".equals(fileIds)) {
			String[] files = user.getFileids().split(",");
			for (int i = 0; i < files.length; i++) {
	            AttachBusiness attachBusiness = new AttachBusiness();
				attachBusiness.setAttachId(files[i]);
				attachBusiness.setBusinessId(user.getId());
				attachBusiness.setBusinessTable("tty_sys_user");
				attachBusiness.setBusinessSource("0");
				attachBusinessService.saveUserPhoto(attachBusiness);
	        }
		}
	}

	/**
	 * 保存方法
	 * @param user
	 * @return
	 * @throws CustomException
	 */
	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer save(User user) throws CustomException {
		Integer result = -1;
		try {
			// 判断登录名是否存在
			User u = new User();
			u.setLoginName(user.getLoginName());
			u.setDeleteFlag(null);
			if (userDao.get(u) != null) {
				throw new CustomException(MessageUtils.getMessage("JC_SYS_089"));
			}
			//判断手机号是否存在
			if (StringUtils.isNotEmpty(user.getMobile())) {
				u = new User();
				u.setDeleteFlag(null);
				u.setMobile(user.getMobile());
				if (userDao.get(u) != null) {
					throw new CustomException(MessageUtils.getMessage("JC_SYS_090"));
				}
			}
			SM2Utils.generateKeyPair();
			if (!StringUtil.isEmpty(GlobalContext.getProperty("password.default.value"))) {
				user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.getProperty("password.default.value"),SM2Utils.getPubKey()));
			} else {
				user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE,SM2Utils.getPubKey()));
			}
			user.setKeyCode(SM2Utils.getPriKey());
			propertyService.fillProperties(user,false);
			if (user.getStatus().equals(GlobalContext.USER_STATUS_2)) {
				user.setLockType(1);
			}
			if (user.getPhoto() == null){
				user.setPhoto("");
			}
			result = userDao.save(user);
			saveUserPhoto(user);
			if (user.getOtherDepts() != null && user.getOtherDepts().size() > 0) {
				for(SysOtherDepts dept : user.getOtherDepts()){
					dept.setUserId(user.getId());
				}
				sysOtherDeptsService.saveList(user.getOtherDepts());
			}
			if (user.getSysUserRole() != null && user.getSysUserRole().size() > 0) {
				for(SysUserRole role : user.getSysUserRole()){
					role.setUserId(user.getId());
				}
				sysUserRoleService.saveList(user.getSysUserRole());
			}
			if (user.getIsAdmin() == 1 && user.getAdminSide() != null && user.getAdminSide().size() > 0) {
				for (AdminSide adminSide : user.getAdminSide()) {
					adminSide.setUserId(user.getId());
				}
				adminSideService.saveList(user.getAdminSide());
			}
			departmentService.clearDeptAndUserCache();
			UserSyncUtil.push(user);
		} catch (CustomException e){
			throw e;
		} catch (Exception e) {
			SystemException se = new SystemException(e);
			se.setLogMsg(MessageUtils.getMessage("JC_SYS_002"));
			throw se;
		}
		return result;
	}

	/**
	 * 修改方法
	 * @param user
	 * @return
	 * @throws CustomException
	 */
	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateUser(User user) throws CustomException {
		CacheClient.removeCache(CACHE_USER_INFO + user.getId());
		try {
			if (StringUtils.isNotEmpty(user.getMobile())) {
				User u = new User();
				u.setMobile(user.getMobile());
				if (userDao.checkMobile(user) != null) {
					throw new CustomException(MessageUtils.getMessage("JC_SYS_090"));
				}
			}
			user.setModifyDateNew(DateUtils.getSysDate());
			propertyService.fillProperties(user, true);
			if (user.getStatus().equals(GlobalContext.USER_STATUS_2)) {
				user.setLockType(1);
			}
			if (user.getPhoto() == null) {
				user.setPhoto("");
			}
			userDao.update(user);
			saveUserPhoto(user);
			sysOtherDeptsService.deleteOtherDept(user.getId());
			if (user.getOtherDepts() != null && user.getOtherDepts().size() > 0) {
				for(SysOtherDepts dept : user.getOtherDepts()){
					dept.setUserId(user.getId());
				}
				sysOtherDeptsService.saveList(user.getOtherDepts());
			}
			AdminSide as = new AdminSide();
			as.setUserId(user.getId());
			adminSideService.deleteAdminSide(as);
			if (user.getIsAdmin() == 1 && user.getAdminSide() != null && user.getAdminSide().size() > 0) {
				for (AdminSide adminSide : user.getAdminSide()) {
					adminSide.setUserId(user.getId());
				}
				adminSideService.saveList(user.getAdminSide());
			}
			departmentService.clearDeptAndUserCache();
			UserSyncUtil.push(user);
		} catch (CustomException e){
			throw e;
		} catch (Exception e) {
			SystemException ce = new SystemException(e);
			ce.setLogMsg(MessageUtils.getMessage("JC_SYS_004"));
			throw ce;
		}
		return 1;
	}

	/**
	 * 初始化用户密码
	 * @param user
	 * @return
	 * @throws CustomException
	 */
	@Override
	public Integer initPassword(User user) throws CustomException{
		try{
			SM2Utils.generateKeyPair();
			if(!StringUtil.isEmpty(GlobalContext.getProperty("password.default.value"))){
				user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.getProperty("password.default.value"), SM2Utils.getPubKey()));
			} else {
				user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE, SM2Utils.getPubKey()));
			}
			user.setModifyPwdFlag(0);
			user.setKeyCode(SM2Utils.getPriKey());
			return userDao.initPassword(user);
		} catch (Exception e) {
			SystemException ce = new SystemException(e);
			ce.setLogMsg(MessageUtils.getMessage("JC_SYS_091"));
			throw ce;
		}
	}

	/**
	 * 查询单条记录方法
	 * @param user
	 * @return
	 * @throws CustomException
	 */
	@Override
	public User getUser(User user) throws CustomException {
		try{
			user = userDao.get(user);
			if (user != null) {
				SysOtherDepts sysOtherDepts = new SysOtherDepts();
				sysOtherDepts.setUserId(user.getId());
				List<SysOtherDepts> deptList = sysOtherDeptsService.queryAll(sysOtherDepts);
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(user.getId());
				List<SysUserRole> roleList = sysUserRoleService.queryAll(sysUserRole);
				AdminSide adminSide = new AdminSide();
				adminSide.setUserId(user.getId());
				List<AdminSide> adminSideList = adminSideService.queryAll(adminSide);
				user.setOtherDepts(deptList);
				user.setSysUserRole(roleList);
				user.setAdminSide(adminSideList);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			SystemException ce = new SystemException(e);
			ce.setLogMsg(MessageUtils.getMessage("JC_SYS_092"));
			throw ce;
		}
	}

	/**
	 * 删除用户
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer delete(User user) throws SystemException {
		for(String userid :user.getPrimaryKeys()){
			CacheClient.removeCache(CACHE_USER_INFO + userid);
		}
		try {
			user.setStatus(GlobalContext.USER_STATUS_3);
			propertyService.fillProperties(user,true);
			userDao.delete(user);
			SysOtherDepts sysOtherDepts = new SysOtherDepts();
			sysOtherDepts.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(sysOtherDepts,true);
			sysOtherDeptsService.updateDeleteFlagByIds(sysOtherDepts);
			SysUserRole role = new SysUserRole();
			role.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(role,true);
			sysUserRoleService.delete(role, true);
			AdminSide adminSide = new AdminSide();
			adminSide.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(adminSide,true);
			adminSideService.delete(adminSide, true);
			departmentService.clearDeptAndUserCache();
			user.setDeleteFlag(1);
			UserSyncUtil.push(user);
		} catch (Exception e) {
			SystemException ce = new SystemException(e);
			ce.setLogMsg(MessageUtils.getMessage("JC_SYS_006"));
			throw ce;
		}
		return 1;
	}

	/**
	 * 根据部门ID查询用户
	 * @param user
	 * @return
	 */
	@Override
	public List<User> queryUserByDeptId(User user) {
		return userDao.queryUserByDeptId(user);
	}

	/**
	 * 根据机构ID查询部门用户
	 * @param user
	 * @return
	 */
	@Override
	public List<User> queryDeptUserByOrgId(User user) {
		return userDao.queryDeptUserByOrgId(user);
	}

	/**
	 * 分页查询用户不关联部门表
	 * @param user
	 * @param page
	 * @return
	 */
	@Override
	public PageManager queryAllUsers(User user, PageManager page) {
		return userDao.queryByPage(user, page, SQL_QUERYALLUSERCOUNT, SQL_QUERYALLUSER);
	}

	/**
	 * 查询下级用户(下级的下级)不包括自己
	 * @param user
	 * @return
	 */
	@Override
	public List<User> queryUserByLeader(User user) {
		try {
			List<User> resultList = new ArrayList<>();
			addChildrenUser(user, resultList, 0);
			return listSorting(resultList);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 递归添加下级用户，最多20层
	 * @param user
	 * @param resultList
	 * @param forcount
	 * @return
	 */
	private List<User> addChildrenUser(User user, List<User> resultList, int forcount){
		if (forcount >= 20) {
			return new ArrayList<>();
		} else {
			forcount ++;
		}
		User u = new User();
		u.setLeaderId(user.getId());
		List<User> userList = userDao.queryAll(u);
		for (User userTemp : userList) {
			resultList.add(userTemp);
			addChildrenUser(userTemp, resultList, forcount);
		}
		return resultList;
	}

	/**
	 * list排序 根据user.orderno
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private List<User> listSorting(List<User> list) throws Exception {
		Set<User> hset = new HashSet<>();
		for (int i = 0; i < list.size(); i++) {
			hset.add(list.get(i));
		}
        List<User> userList = new ArrayList<>();
        userList.addAll(hset);
        Collections.sort(userList, new Comparator<User>(){
            @Override
            public int compare(User arg0, User arg1) {
            	int v = arg0.getOrderNo().compareTo(arg1.getOrderNo());
            	if(v == 0){
            		return arg0.getId().compareTo(arg1.getId());
            	}
            	return v;
            }
        });
        return userList;
	}

	/**
	 * 根据ids查询用户
	 * @param ids
	 * @return
	 */
	@Override
	public List<User> queryUserByIds(String ids) {
		User user = new User();
		user.setPrimaryKeys(ids.split(","));
		return userDao.queryUserByIds(user);
	}

	/**
	 * 判断目标用户是否是源用户的上级（或上级的上级....）
	 * @param sourceUser 源用户
	 * @param targetUser 目标用户
	 * @return true-是  false-否
	 */
	@Override
    public boolean isLeader(User sourceUser, User targetUser) {
		boolean isLeader = false;
		List<User> underList = queryUserByLeader(targetUser);
		for (User user :underList) {
			if (user.getId().equals(sourceUser.getId())){
				isLeader = true;
				break;
			}
		}
		return isLeader;
	}

	/**
	 * 方法描述：判断目标用户是否是源用户的上级（或上级的上级....）
	 * @param sourceUserId 源用户ID
	 * @param targetUserId 目标用户ID
	 * @return true-是  false-否
	 */
	@Override
    public boolean isLeader(String sourceUserId, String targetUserId){
		boolean isLeader = false;
		User targetUser = new User();
		targetUser.setId(targetUserId);
		List<User> underList = queryUserByLeader(targetUser);
		for (User user :underList) {
			if (user.getId().equals(sourceUserId)){
				isLeader = true;
				break;
			}
		}
		return isLeader;
	}

	/**
	 * 缓存查询单个用户
	 * @param user
	 * @return
	 */
	@Override
    public User getUserById(User user) {
		User u = (User) JsonUtil.json2Java(CacheClient.getCache(CACHE_USER_INFO + user.getId()),User.class);
		if (u == null) {
			u = userDao.get(user);
			CacheClient.putCache(CACHE_USER_INFO + user.getId(), JsonUtil.java2Json(u));
		}
		return u;
	}

	/**
	 * 根据用户ID查询用户信息(包括部门、机构信息)
	 * @param id
	 * @return
	 */
	@Override
    public User getUser(String id){
		User user = new User();
		user.setId(id);
		user = userDao.get(user);
		if (user != null) {
			Department department = new Department();
			department.setId(user.getDeptId());
			department = systemService.queryOrgIdAndName(department);
			user.setOrgId(department.getId());
			user.setOrgName(department.getName());
			if (user.getLeaderId() != null) {
				if (UserUtils.getUser(user.getLeaderId()) != null) {
					user.setLeaderIdValue(UserUtils.getUser(user.getLeaderId()).getDisplayName());
				}
			}
		}
		return user;
	}

	/**
	 * 缓存查询所有用户
	 */
	@Override
	public void getAllUsers() {
		List<User> userList = userDao.queryAll(new User());
		if (userList == null || userList.size() == 0) {
			return;
		}
		for (User user : userList) {
			CacheClient.putCache(CACHE_USER_INFO + user.getId(), JsonUtil.java2Json(user));
		}
	}

	/**
	 * 修改用户信息(修改条件不包括modifyDate)
	 * @param user
	 * @return
	 */
	@Override
	public Integer update2(User user) {
		int flag = userDao.update2(user);
		UserSyncUtil.push(user);
		return flag;
	}

	/**
	 * 检查用户手机号码
	 * @param user
	 * @return
	 */
	@Override
	public User checkMobile(User user) {
		return userDao.checkMobile(user);
	}

	/**
	 * 根据用户IDS查询用户机构树(结果排序)
	 * @param ids
	 * @return
	 */
	@Override
    public List<Department> queryUserTreeByIds(String ids) {
		List<Department> tree = new ArrayList<>();
		try{
            String[] idArray = ids.split(",");
			for (String userId : idArray) {
				User user = new User();
				user.setId(userId);
				user = userDao.get(user);
				if (user == null) {
					continue;
				}
				//组装人员节点
				Department d = new Department();
				d.setName(user.getDisplayName());
				d.setParentId(user.getDeptId());
				d.setId(user.getId());
				//人员设置type为0
				d.setType("0");
				d.setQueue(user.getOrderNo());
				tree.add(d);
				//组装树节点
				Department dept = new Department();
				dept.setId(user.getDeptId());
				dept = departmentService.get(dept);
				if (!isExist(dept, tree)) {
					//部门设置type为1
					dept.setType("1");
					tree.add(dept);
					//递归添加父节点
					addParentDept(tree, dept);
				}
			}
			return listSorting2(tree);
		} catch(Exception e){
			logger.error(e);
		}
		return null;
	}

	/**
	 * 递归添加上级部门信息
	 * @param tree 返回的结果集
	 * @param dept 部门实体
	 * @return
	 */
	private List<Department> addParentDept(List<Department> tree, Department dept){
		try {
			if (!"0".equals(dept.getParentId())) {
				Department d = new Department();
				d.setId(dept.getParentId());
				d = departmentService.get(d);
				if (!isExist(d, tree)) {
					//部门设置type为1
					d.setType("1");
					tree.add(d);
					addParentDept(tree, d);
				}
			}
		} catch (CustomException e) {
			logger.error(e);
		}
		return tree;
	}
	
	/**
	 * 判断集合里是否存在此部门
	 * @param  d 要判断的部门
	 * @param  list 部门集合
	 * @return boolean true包含	false不包含
	 */
	private boolean isExist(Department d, List<Department> list){
		if (list == null || list.size() == 0) {
			return false;
		}
		boolean result = false;
		for (Department department : list) {
			if(department.getId().equals(d.getId())){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 集合排序
	 * @param list 部门集合
	 * @return 排序后的结果集
	 * @throws Exception
	 */
	private List<Department> listSorting2(List<Department> list) throws Exception {
		Set<Department> departmentsSet = new HashSet<>();
		for (int i = 0; i < list.size(); i++) {
			departmentsSet.add(list.get(i));
		}
        List<Department> userList = new ArrayList<>();
        userList.addAll(departmentsSet);
        Collections.sort(userList,new Comparator<Department>(){
        	@Override
            public int compare(Department arg0, Department arg1) {
        		int v = arg0.getQueue().compareTo(arg1.getQueue());
        		if(v == 0){
        			return arg0.getId().compareTo(arg1.getId());
        		}
        		return v;
            }
        });
        return userList;
	}

	/**
	 * 根据用户ID查询机构下级(下下级)的下属
	 * @param user 必须参数deptid、id
	 * @return 查询结果
	 */
	@Override
	public List<User> queryUserByLeaderAndDeptId(User user) {
		String deptIds = "";
		List<Department> deptList = departmentService.getDeptAndUserByDeptId(user.getDeptId());
		for (int i = 0; i < deptList.size(); i++) {
			Department dept = deptList.get(i);
			deptIds += dept.getId();
			if (i != deptList.size()-1) {
				deptIds += ",";
			}
		}
		if (deptIds.length() > 0) {
			User u = new User();
			u.setLeaderId(user.getId());
			u.setDeptIds(deptIds);
			return userDao.queryAll(u);
		}
		return null;
	}

	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 * @throws CustomException
	 */
	@Override
	public Integer updateUserInfo(User user) throws CustomException {
		//清空用户缓存
		CacheClient.removeCache(CACHE_USER_INFO + user.getId());
		user.setModifyDateNew(DateUtils.getSysDate());
		//保存头像附件
		saveUserPhotoNotTran(user);
		if (user.getPhoto() == null) {
			user.setPhoto("");
		}
		Integer result = userDao.updateUserInfo(user);
		//推送用户信息
		UserSyncUtil.push(user);
		return result;
	}
	
	
	@Override
	public PageManager queryAllUsersForOrder(User user, PageManager page) {
		return userDao.queryByPage(user, page, SQL_QUERYALLUSERCOUNTFORORDER, SQL_QUERYALLUSERFORORDER);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateUserOrder(String userId, String orderNo, String oUserId, String oOrderNo) {
		try{
			User user1 = new User();
			user1.setId(userId);
			user1.setOrderNo(Integer.parseInt(oOrderNo));
			userDao.update2(user1);
			//推送用户信息
			UserSyncUtil.push(user1);

			User user2 = new User();
			user2.setId(oUserId);
			user2.setOrderNo(Integer.parseInt(orderNo));
			userDao.update2(user2);
			//推送用户信息
			UserSyncUtil.push(user2);
		} catch (Exception e){
			logger.error(e);
			return -1;
		}
		return 1;
	}

	/**
	 * 恢复删除用户
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteBack(User user) throws SystemException {
		CacheClient.removeCache(CACHE_USER_INFO + user.getId());
		try {
			user.setStatus(GlobalContext.USER_STATUS_0);
			propertyService.fillProperties(user,true);
			userDao.deleteBack(user);

			SysOtherDepts sysOtherDepts = new SysOtherDepts();
			sysOtherDepts.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(sysOtherDepts,true);
			sysOtherDeptsService.deleteBack(sysOtherDepts);

			SysUserRole role = new SysUserRole();
			role.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(role,true);
			sysUserRoleService.deleteBack(role);

			AdminSide adminSide = new AdminSide();
			adminSide.setPrimaryKeys(user.getPrimaryKeys());
			propertyService.fillProperties(adminSide,true);
			adminSideService.deleteBack(adminSide);
			user.setDeleteFlag(0);
			//推送用户信息
			UserSyncUtil.push(user);
		} catch (Exception e) {
			SystemException ce = new SystemException(e);
			ce.setLogMsg("记录恢复失败");
			throw ce;
		}
		return 1;
	}

	@Override
    public User getWithLogic(User user){
		User u = (User) JsonUtil.json2Java(CacheClient.getCache(CACHE_USER_INFO + user.getId()), User.class);
		if (u == null) {
			u = userDao.getWithLogic(user);
			CacheClient.putCache(CACHE_USER_INFO + user.getId(), JsonUtil.java2Json(u));
		}
		return u;
	}

	@Override
	public List<User> getUsersByRoleids(User user) {
		return userDao.getUsersByRoleids(user);
	}

	@Override
	public List<User> queryUserIdAndName(User user) {
		return userDao.queryUserIdAndName(user);
	}

	/**
	 * 根据组织id获取本组织人员集合
	 * @param id
	 * @return
	 */
	@Override
    public List<User> getUsersByDeptId(String id) {
		return userDao.getUsersByDeptId(id);
	}

	/**
	 * 根据用户id获取用户直属领导信息
	 * @param id
	 * @return
	 * @throws CustomException
	 */
	@Override
    public User getLeader(String id) throws CustomException {
		//1.获取用户信息
		User user = new User();
		user.setId(id);
		user = userDao.get(user);

		//2.判断该用户是否为部门领导
		Department department = new Department();
		department.setId(user.getDeptId());
		department = departmentService.queryOne(department);

		if(department.getLeaderId() == null) {
            return null;
        }

		if("0".equals(department.getLeaderId())) {
            return null;
        }

		User leader = new User();
		if(id.equals(department.getLeaderId())){
			//3.如果是领导，则直接查询该部门的上级主管部门，获得上级主管领导的id
			Department leaderDept = new Department();
			leaderDept.setId(department.getParentId());
			leaderDept = departmentService.queryOne(leaderDept);
			leader.setId(leaderDept.getLeaderId());
		} else {
			//3.如果不是领导，则直接查该部门的领导并返回
			leader.setId(department.getLeaderId());
		}
		return userDao.get(leader);
	}

	/**
	 * 根据登录用户名查询用户详情
	 * @param loginName
	 * @return
	 * @throws CustomException
	 */
	@Override
    public User getUserByLoginName(String loginName) throws CustomException {
		User user = new User();
		user.setLoginName(loginName);
		User userInfo = userDao.get(user);
		if(userInfo == null){
			return null;
		}
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUserId(userInfo.getId());
		List<SysUserRole> sysUserRoleList = sysUserRoleService.queryAll(sysUserRole);

		for (SysUserRole sysUserRole2 : sysUserRoleList) {
			Role role = new Role();
			role.setId(sysUserRole2.getRoleId());
			role = roleService.get(role);
			if (role != null) {
				sysUserRole2.setUserRole(role.getName());
			}
		}
		user.setSysUserRole(sysUserRoleList);
		return user;
	}
}