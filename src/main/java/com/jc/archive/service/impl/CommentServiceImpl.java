package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.dao.ICommentDao;
import com.jc.archive.domain.Comment;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.ICommentService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements ICommentService{

	private ICommentDao commentDao;
	
	public CommentServiceImpl(){}
	
	@Autowired
	public CommentServiceImpl(ICommentDao commentDao){
		super(commentDao);
		this.commentDao = commentDao;
	}

}