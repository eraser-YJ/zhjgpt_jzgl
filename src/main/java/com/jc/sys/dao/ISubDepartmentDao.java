package com.jc.sys.dao;

import com.jc.sys.domain.SubDepartment;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;


/**
 *@ClassName ISubDepartmentDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
 
public interface ISubDepartmentDao extends IBaseDao<SubDepartment>{

    public SubDepartment queryOne(SubDepartment department);

    public List<SubDepartment> getDeptsByDeptId(String id);

    public void deleteAll(SubDepartment department);
}
