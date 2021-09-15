package com.jc.archive.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.archive.dao.IFavoriteDao;
import com.jc.archive.domain.Favorite;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Repository
public class FavoriteDaoImpl extends BaseClientDaoImpl<Favorite> implements IFavoriteDao{

	public FavoriteDaoImpl(){}

}