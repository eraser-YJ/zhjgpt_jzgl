package com.jc.archive.dao.impl;

import com.jc.archive.dao.ICommentDao;
import com.jc.archive.domain.Comment;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Repository
public class CommentDaoImpl extends BaseClientDaoImpl<Comment> implements ICommentDao{

	public CommentDaoImpl(){}

}