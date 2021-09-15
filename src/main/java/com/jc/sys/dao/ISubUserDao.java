package com.jc.sys.dao;

import com.jc.sys.domain.SubUser;

import com.jc.foundation.dao.IBaseDao;

/**
 *@ClassName ISubUserDao
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
 
public interface ISubUserDao extends IBaseDao<SubUser>{

    public void deleteAll(SubUser vo);

    public void reTheme(SubUser vo);
}
