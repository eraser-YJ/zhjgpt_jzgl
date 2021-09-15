package com.jc.archive.dao.impl;

import com.jc.archive.dao.IDocumentDao;
import com.jc.archive.domain.Document;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
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
public class DocumentDaoImpl extends BaseClientDaoImpl<Document> implements IDocumentDao{

	public DocumentDaoImpl(){}

	@Override
	public int deleteDocToRecycle(Document doc) {
		return getTemplate().update(getNameSpace(doc)+".deleteDocToRecycle", doc);
	}

	@Override
	public String getSeq(Document d) {
		if(1 == d.getModel()) {
			//my document
			return getTemplate().selectOne(getNameSpace(new Document())+".getMySeq",d);
		} else {
			return getTemplate().selectOne(getNameSpace(new Document())+".getSeq",d);
		}
	}
	
	/**
	 * @description 根据文档id获取未关联文档的集合
	 * @param Document 实体类
	 * @param page 分页信息 
	 * @return map：list 查询的记录 ，page分页信息
	 * @author 
	 * @version 2014-07-02
	 */
	@Override
	public PageManager queryByRelate(Document document,final PageManager page) {
		return queryByPage(document,page,"queryByRelateCount","queryByRelate");
	}
	
	/**
	 * @description 销毁归档数据
	 * @param Document doc 实体类
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-15
	 */
	public int deleteList(Document doc) {
		return getTemplate().delete(getNameSpace(doc)+".deleteList", doc);
	}

}