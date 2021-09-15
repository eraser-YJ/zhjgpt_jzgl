package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.dao.IHistoryDao;
import com.jc.archive.domain.History;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.IHistoryService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class HistoryServiceImpl extends BaseServiceImpl<History> implements IHistoryService{

	private IHistoryDao historyDao;
	
	public HistoryServiceImpl(){}
	
	@Autowired
	public HistoryServiceImpl(IHistoryDao historyDao){
		super(historyDao);
		this.historyDao = historyDao;
	}

}