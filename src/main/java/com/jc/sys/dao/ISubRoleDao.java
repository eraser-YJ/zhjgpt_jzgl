package com.jc.sys.dao;

import com.jc.sys.domain.SubRole;

import com.jc.foundation.dao.IBaseDao;


/**
 *@ClassName ISubRoleDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
 
public interface ISubRoleDao extends IBaseDao<SubRole>{

    public void deleteAll(SubRole vo);
}
