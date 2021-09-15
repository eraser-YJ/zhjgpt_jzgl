package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.dao.IReadinfoDao;
import com.jc.archive.domain.Readinfo;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.IReadinfoService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class ReadinfoServiceImpl extends BaseServiceImpl<Readinfo> implements IReadinfoService{

	private IReadinfoDao readinfoDao;
	
	public ReadinfoServiceImpl(){}
	
	@Autowired
	public ReadinfoServiceImpl(IReadinfoDao readinfoDao){
		super(readinfoDao);
		this.readinfoDao = readinfoDao;
	}

}