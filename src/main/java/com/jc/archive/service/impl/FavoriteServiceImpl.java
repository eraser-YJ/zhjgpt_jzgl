package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.dao.IFavoriteDao;
import com.jc.archive.domain.Favorite;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.IFavoriteService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class FavoriteServiceImpl extends BaseServiceImpl<Favorite> implements IFavoriteService{

	private IFavoriteDao favoriteDao;
	
	public FavoriteServiceImpl(){}
	
	@Autowired
	public FavoriteServiceImpl(IFavoriteDao favoriteDao){
		super(favoriteDao);
		this.favoriteDao = favoriteDao;
	}

}