package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.common.util.Utils;
import com.jc.system.security.DepartmentConstant;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.dao.ISystemDao;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ILoginlogService;
import com.jc.system.security.service.ISystemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Stack;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SystemServiceImpl extends BaseServiceImpl<User> implements ISystemService {
	private ThreadLocal<Stack<String>> threadLocal = new ThreadLocal<Stack<String>>(){
		@Override
        protected Stack<String> initialValue() {
			return new Stack<String>();
		}
		
	};
	private Logger logger  = LoggerFactory.getLogger(SystemServiceImpl.class);
	@Autowired
	private ILoginlogService loginlogService;

	@Autowired
	public SystemServiceImpl(ISystemDao systemDao) {
		super(systemDao);
		this.systemDao= systemDao;
	}
	public SystemServiceImpl(){
		
	}
	private ISystemDao systemDao;

	@Override
    public User get(String loginName) {
		return systemDao.get(loginName);
	}
	
	@Override
	public Department queryOrgIdAndName(Department department) {
		if (DepartmentConstant.DEPARTMENT_ROOT_ID .equals(department.getId()) || DepartmentConstant.DEPARTMENT_ROOT_ID_OLD.equals(department.getId())){
			return getRoot(department);
		}
		Department dept = systemDao.queryParentDept(department);
		if (dept == null){
			logger.debug("query "+ department.getId()+" parement result null");
			return null;
		}
		if(DepartmentConstant.DEPARTMENT_TYPE_ORG.equals( dept.getDeptType()) ){
			threadLocal.get().clear();
			logger.debug("query "+ department.getId()+" parement result " + dept.getId() +" "+ dept.getName());
			return dept;
		}
		else{
			threadLocal.get().push(department.getId());
			return queryOrgIdAndName(dept);
		}
	}

	@Override
	public Department queryOrgIdAndName(Department department, List<Department> deptList) {
		if (DepartmentConstant.DEPARTMENT_ROOT_ID .equals(department.getId()) || DepartmentConstant.DEPARTMENT_ROOT_ID_OLD.equals(department.getId())){
			return getRoot(department);
		}
		Department dept = null;
		for (Department depts:deptList){
			if(department.getParentId().equals(depts.getId())){
				dept = depts;
				break;
			}
		}
		if (dept == null){
			logger.debug("query "+ department.getId()+" parement result null");
			return null;
		}
		if(DepartmentConstant.DEPARTMENT_TYPE_ORG.equals( dept.getDeptType()) ){
			threadLocal.get().clear();
			logger.debug("query "+ department.getId()+" parement result " + dept.getId() +" "+ dept.getName());
			return dept;
		}
		else{
			threadLocal.get().push(department.getId());
			return queryOrgIdAndName(dept,deptList);
		}
	}


	@Override
	public Department queryOrgIdAndNameForAllDept(Department department) {
		if (DepartmentConstant.DEPARTMENT_ROOT_ID .equals(department.getId())
				|| DepartmentConstant.DEPARTMENT_ROOT_ID_OLD.equals(department.getId())){
			return getRoot(department);
		}
		Department dept = systemDao.queryParentDeptForAllDept(department);
		if (dept == null){
			logger.debug("query "+ department.getId()+" parement result null");
			return null;
		}
		if(DepartmentConstant.DEPARTMENT_TYPE_ORG.equals( dept.getDeptType()) ){
			threadLocal.get().clear();
			logger.debug("query "+ department.getId()+" parement result " + dept.getId() +" "+ dept.getName());
			return dept;
		}
		else{
			threadLocal.get().push(department.getId());
			return queryOrgIdAndName(dept);
		}
	}

	
	@Override
	public void loginCallBack(HttpServletRequest request) throws CustomException{
		User user = SystemSecurityUtils.getUser();
		loginlogService.setLoginUserInfo(user, Utils.getIpAddr(request), 1, 1);
		SystemSecurityUtils.logInMes();
	}
	
	@Override
	public void logoutCallBack(HttpServletRequest request) throws CustomException {
		User user = SystemSecurityUtils.getUser();
		loginlogService.setLoginUserInfo(user, Utils.getIpAddr(request), 2, 1);
	}
	
	@Override
	public void loginCallBack4M(User user, HttpServletRequest request) throws CustomException {
		loginlogService.setLoginUserInfo(user, Utils.getIpAddr(request), 1, 2);
		
	}
	
	@Override
	public void logoutCallBack4M(HttpServletRequest request) throws CustomException {
		User user = SystemSecurityUtils.getUser();
		loginlogService.setLoginUserInfo(user, Utils.getIpAddr(request), 2, 2);
	}

	@Override
    public List<Menu> queryMenu(String userId) {
		return systemDao.queryMenu(userId);
	}

	@Override
	public List<Setting> queryServerSetting(Setting setting) {
		return systemDao.queryServerSetting(setting);
	}

	@Override
	public Department queryParentDept(Department department) {
		return systemDao.queryParentDept(department);
	}
	@Override
	public Department getRoot(Department department){
		return systemDao.getRoot(department);
	}
}