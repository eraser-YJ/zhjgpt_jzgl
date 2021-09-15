package com.jc.dlh.dao;

import com.jc.dlh.domain.DlhUserRole;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.DBException;


/**
 * @title 统一数据资源中心
 * @description  dao接口类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-03-18
 */
 
public interface IDlhUserRoleDao extends IBaseDao<DlhUserRole>{

	Integer deleteByUserId(DlhUserRole dlhUserRole) throws DBException;
}
