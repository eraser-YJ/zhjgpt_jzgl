package com.jc.archive.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.dao.IRelateDao;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Relate;
import com.jc.archive.service.IDocumentService;
import com.jc.archive.service.IRelateService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Service
public class RelateServiceImpl extends BaseServiceImpl<Relate> implements IRelateService{

	@Autowired
	private IDocumentService documentService;
	private IRelateDao relateDao;
	
	public RelateServiceImpl(){}
	
	@Autowired
	public RelateServiceImpl(IRelateDao relateDao){
		super(relateDao);
		this.relateDao = relateDao;
	}
	/**
	 * 根据选择的文档批量保存关联关系方法
	 * @param Relate relate 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 * @author
	 * @version  2014-07-01
	 */	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer saveByDocumentIds(String documentId,String documentIds) throws Exception {
		Integer result = -1;
		try {
			Document document = new Document();
			document.setIds(documentIds);
	        List<Document> documentList = documentService.queryAll(document);
	        List<Relate> relateList = new ArrayList<Relate>();
	        for(Document d : documentList) {
	        	Relate relate = new Relate();
	        	relate.setDocumentId(documentId);
	        	relate.setDmRelateId(d.getId());
	        	relate.setFolderId(d.getFolderId());
	        	relate.setCreateDate(d.getCreateDate());
	        	propertyService.fillProperties(relate,false);
	        	relateList.add(relate);
	        }
	        result = relateDao.save(relateList);
		} catch (Exception e) {
			log.error(e.getStackTrace());
			throw new Exception();
		}
		return result;
	}

	public void deleteRelateDM(Relate relate) {
		relateDao.deleteRelateDM(relate);
	}
	
}