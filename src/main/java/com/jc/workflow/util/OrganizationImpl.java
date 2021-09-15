package com.jc.workflow.util;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.dao.IDepartmentDao;
import com.jc.system.security.dao.IRoleDao;
import com.jc.system.security.dao.ISysUserRoleDao;
import com.jc.system.security.dao.IUserDao;
import com.jc.system.security.dao.impl.DepartmentDaoImpl;
import com.jc.system.security.dao.impl.RoleDaoImpl;
import com.jc.system.security.dao.impl.UserDaoImpl;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.RoleCacheUtil;
import com.jc.system.security.util.UserUtils;
import com.jc.workflow.external.OrganizationInterface;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 工作流组织接口实现
 * @author Administrator
 * @date 2020-07-01
 */
public class OrganizationImpl implements OrganizationInterface{

    private Logger logger = Logger.getLogger(OrganizationImpl.class);

    private IUserDao userDao = (IUserDao) SpringContextHolder.getBean(UserDaoImpl.class);
    private IDepartmentDao departmentDao = (IDepartmentDao) SpringContextHolder.getBean( DepartmentDaoImpl.class);
    private IDepartmentService departmentService = (IDepartmentService) SpringContextHolder.getBean(IDepartmentService.class);
    private IRoleDao roleDao = (IRoleDao) SpringContextHolder.getBean(RoleDaoImpl.class);
    private ISysUserRoleDao sysUserRoleDao = (ISysUserRoleDao) SpringContextHolder.getBean(ISysUserRoleDao.class);
    @Override
    public String getUserNameById(String id) {
        User u = UserUtils.getUser(id);
        return u == null ? "" : u.getDisplayName();
    }

    @Override
    public String getUserOrg(String id) {
        User u = UserUtils.getUser(id);
        return u == null ? "" : u.getOrgId().toString();
    }

    @Override
    public List<String> getUserRoleIds(String userId) {
        List<String>  result = new ArrayList<String>();
        String roles = RoleCacheUtil.getRolesbyUserId(userId);
        if(!StringUtil.isEmpty(roles)){
            String[] roleArr = roles.split(",");
            Collections.addAll(result,roleArr);
        }
        return result;
    }

    @Override
    public String getDepartmentNameById(String id) {
        Department department = new Department();
        department.setId(id);
        department = departmentDao.get(department);
        return department == null ? "" : department.getName();
    }

    @Override
    public String getRoleNameById(String roleId) {
        Role role = new Role();
        role.setId(roleId);
        role = roleDao.get(role);
        return role == null ? "" : role.getName();
    }

    @Override
    public List<Map<String,Object>> getUsersByDepartmentId(String id) {
        User user = new User();
        user.setDeptId(id);
        List<User> userList = userDao.queryAll(user);

        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();

        for(User item:userList){
            Map<String,Object> user1 = new HashMap<String, Object>();
            user1.put("id",item.getId());
            user1.put("name",item.getDisplayName());
            result.add(user1);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>> getUsersByRoleId(String id) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(id);
        List<User> userList = sysUserRoleDao.queryUserByRole(sysUserRole);
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(User item:userList){
            Map<String,Object> user = new HashMap<String, Object>();
            user.put("id",item.getId());
            user.put("name",item.getDisplayName());
            result.add(user);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>> getUsersByOrgId(String id) {
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        return result;
    }

    @Override
    public List<Map<String,Object>> getDepartmentByOrgId(String orgId) {
        return getDepartmentByDeptId(orgId);
    }

    @Override
    public List<Map<String,Object>> getDepartmentByDeptId(String deptId) {
        Department dept = new Department();
        dept.setParentId(deptId);
        List<Department> departments = departmentDao.queryTree(dept);
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(Department item:departments){
            if(item.getDeptType()==0){
                Map<String,Object> deptMap = new HashMap<String, Object>();
                deptMap.put("id",item.getId());
                deptMap.put("name",item.getName());
                deptMap.put("type","dept");
                result.add(deptMap);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAllRoles() {
        return null;
    }

    @Override
    public List<Map<String,Object>> getDepartmentTreeByOrgId(String orgId) {
        List<Department> departments = departmentDao.getDeptByOrgId(orgId);
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        putDeptToTree(departments,orgId,result,false);
        return result;
    }

    /**
     * 将部门list组装成部门树
     * @param departments 部门列表
     * @param parentId 父id
     * @param children 拼装的list
     * @param authFlag 是否判断权限(根据用户id查询机构时用到)
     */
    private void putDeptToTree(List<Department> departments, String parentId, List<Map<String,Object>> children, boolean authFlag){
        for(Department dept:departments){
            if(dept.getParentId().equals(parentId)){
                Map<String,Object> deptMap = new HashMap<String,Object>();
                deptMap.put("id",dept.getId());
                deptMap.put("name",dept.getName());
                if(authFlag){
                    deptMap.put("auth",dept.getIsChecked());
                }else{
                    deptMap.put("auth",1);
                }
                if(dept.getDeptType()==0){
                    deptMap.put("type","dept");
                }else{
                    deptMap.put("type","org");
                }
                children.add(deptMap);
                List<Map<String,Object>> deptChildren = new ArrayList<Map<String,Object>>();
                deptMap.put("children",deptChildren);
                putDeptToTree(departments,dept.getId(),deptChildren,authFlag);
            }
        }
    }

    @Override
    public List<Map<String,Object>> getRolesByOrgId(String id) {
        return getRolesByDepartmentId(id);
    }

    @Override
    public String getRoleOrgId(String id) {
        Role role = new Role();
        role.setId(id);
        role = roleDao.get(role);
        String orgId = getOrgIdByDeptId(role.getDeptId());
        return orgId;
    }

    /**
     * 根据部门获得机构id
     * @param deptId
     * @return
     */
    private String getOrgIdByDeptId(String deptId){
        String result ="";
        try {
            Department dept = DeptCacheUtil.getDeptById(deptId);
            if(dept.getDeptType()==1){
                result = dept.getId().toString();
            }else{
            	String parentId = dept.getParentId();
                result = getOrgIdByDeptId(parentId);
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>> getRolesByDepartmentId(String id) {
        Role role = new Role();
        role.setDeptIds(id);
        List<Role> roleList = roleDao.queryAll(role);
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(Role item:roleList){
            Map<String,Object> roleMap = new HashMap<String,Object>();
            roleMap.put("id",item.getId());
            roleMap.put("name",item.getName());
            result.add(roleMap);
        }
        return result;
    }


    @Override
    public String getLoginUser() {
        User user = SystemSecurityUtils.getUser();
        if(user == null){
            return null;
        }
        return user.getId().toString();
    }

    @Override
    public List<Map<String,Object>> getLeader(String userId) {
        User u = new User();
        u.setId(userId);
        u = (User)this.userDao.get(u);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        if(u.getLeaderId()!=null&&!StringUtil.isEmpty(u.getLeaderId().toString())){
            Map<String,Object> userMap = new HashMap<String,Object>();
            userMap.put("id",u.getLeaderId().toString());
            userMap.put("name",getUserNameById(u.getLeaderId().toString()));
            result.add(userMap);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>> getDeptLeader(String userId) {
        User u = new User();
        u.setId(userId);
        u = (User)this.userDao.get(u);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        if(u.getDeptLeader()!=null&&!StringUtil.isEmpty(u.getDeptLeader().toString())){
            Map<String,Object> userMap = new HashMap<String,Object>();
            userMap.put("id",u.getDeptLeader().toString());
            userMap.put("name",getUserNameById(u.getDeptLeader().toString()));
            result.add(userMap);
        }
        return result;
    }

    @Override
    public boolean isSuperWorkflowAdmin(String userId) {
        User user = UserUtils.getUser(userId);
        return user.getIsSystemManager();
    }

    @Override
    public List<String> getOrgWorkflowAdminInfo(String userId) {
        User user = UserUtils.getUser(userId);
        List<String> result = new ArrayList<String>();
        if(user.getIsAdmin() == 1){
            List<Department> departments = departmentService.getManageOrgByUserId(userId);
            for(Department department:departments){
                if("1".equals(department.getIsChecked())){
                    result.add(department.getId().toString());
                }
            }
        }
        return result;
    }

    /**
     * 获得所有机构树
     * @return
     */
    @Override
    public List<Map<String, Object>> getOrgTree() {
        Department department = new Department();
        department.setDeptType(1);
        List<Department> departments = departmentDao.queryAll(department);
        ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        putDeptToTree(departments,"0",result,false);
        return result;
    }

    /**
     * 获得指定用户所能管理的机构树
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> getOrgTree(String userId) {
        List<Department> departments = departmentService.getManageOrgByUserId(userId);
        ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        putDeptToTree(departments,"0",result,true);
        return result;
    }

    @Override
    public String getUserDeptId(String userId) {
        if(StringUtil.isEmpty(userId)){
            return "";
        }
        return UserUtils.getUser(userId).getDeptId().toString();
    }
}
