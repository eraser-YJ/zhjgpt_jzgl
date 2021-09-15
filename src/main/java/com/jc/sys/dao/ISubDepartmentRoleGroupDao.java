package com.jc.sys.dao;

import com.jc.sys.domain.SubDepartmentRoleGroup;

import com.jc.foundation.dao.IBaseDao;


/**
 *@ClassName ISubDepartmentRoleGroupDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
 
public interface ISubDepartmentRoleGroupDao extends IBaseDao<SubDepartmentRoleGroup>{

    public void deleteAll(SubDepartmentRoleGroup vo);
}
