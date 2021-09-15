package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.SystemException;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.dao.IUserDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SystemUserServiceImpl implements ISystemUserService {

    @Autowired
    public SystemUserServiceImpl(IUserDao dao) {
        this.userDao =dao;
    }

    private IUserDao userDao;

    @Autowired
    private ISystemService systemService;
    @Autowired
    private ISysOtherDeptsService sysOtherDeptsService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private IAdminSideService adminSideService;

    /**
     * 根据用户ID查询用户信息(包括部门、机构信息)
     * @return User 查询结果
     * @throws Exception
     */
    @Override
    public User getUser(String id){
        User user = new User();
        user.setId(id);
        user = userDao.get(user);
        if(user != null){
            Department department = new Department();
            department.setId(user.getDeptId());
            department = systemService.queryOrgIdAndName(department);
            user.setOrgId(department.getOrgId());
            user.setOrgName(department.getOrgName());
        }

        return user;
    }

    /**
     * 根据用户ID查询用户信息(包括部门、机构信息)
     * @return User 查询结果
     * @throws Exception
     */
    @Override
    public User get(User user)  throws CustomException{
        return userDao.get(user);
    }

    /**
     * 查询单条记录方法
     * @return User 查询结果
     * @throws Exception
     */
    @Override
    public User getUser(User user) throws CustomException {
        try{
            user = userDao.get(user);
            if(user != null){
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

        }  catch (Exception e) {
            SystemException ce = new SystemException(e);
            ce.setLogMsg(MessageUtils.getMessage("JC_SYS_092"));
            throw ce;
        }
    }

    /**
     * 修改用户信息(修改条件不包括modifyDate)
     * @return Integer 操作结果
     * @throws Exception
     */
    @Override
    public Integer update2(User user) {
        return userDao.update2(user);
    }

    @Override
    public List<Menu> queryMenu(String userId) {
        return systemService.queryMenu(userId);
    }
}
