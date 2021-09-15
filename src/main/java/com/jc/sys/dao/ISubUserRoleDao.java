package com.jc.sys.dao;

import com.jc.foundation.exception.CustomException;
import com.jc.sys.domain.SubUserRole;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;


/**
 *@ClassName ISubUserRoleDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
 
public interface ISubUserRoleDao extends IBaseDao<SubUserRole>{

    /**
     * @description 根据用户、部门ID返回子系统角色集合
     * @param   subUserRole
     * @return void
     * @author
     * @version 2018-04-12
     */
    public List<SubUserRole> getRolesByUserAndDeptId(SubUserRole subUserRole) throws CustomException;

    public void deleteAll(SubUserRole vo);
}
