package com.jc.archive.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.archive.dao.IVersionDao;
import com.jc.archive.domain.Version;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-01
 */
@Repository
public class VersionDaoImpl extends BaseClientDaoImpl<Version> implements IVersionDao{

	public VersionDaoImpl(){}

	@Override
	public Integer getMaxVersion(String backUpId){
		return getTemplate().selectOne(getNameSpace(new Version())+".getMaxVersion",backUpId);
	}
}