package com.jc.dlh.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.dlh.dao.IDlhUserDao;
import com.jc.dlh.domain.DlhUser;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;

/**
 * @title 统一数据资源中心
 * @description  dao实现类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-03-13
 */
@Repository
public class DlhUserDaoImpl extends BaseClientDaoImpl<DlhUser> implements IDlhUserDao{

	public DlhUserDaoImpl(){}

}