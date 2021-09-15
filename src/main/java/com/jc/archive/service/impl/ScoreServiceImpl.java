package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.dao.IScoreDao;
import com.jc.archive.domain.Score;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.IScoreService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class ScoreServiceImpl extends BaseServiceImpl<Score> implements IScoreService{

	private IScoreDao scoreDao;
	
	public ScoreServiceImpl(){}
	
	@Autowired
	public ScoreServiceImpl(IScoreDao scoreDao){
		super(scoreDao);
		this.scoreDao = scoreDao;
	}

}