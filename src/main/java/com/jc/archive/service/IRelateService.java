package com.jc.archive.service;

import com.jc.foundation.service.IBaseService;
import com.jc.archive.domain.Relate;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public interface IRelateService extends IBaseService<Relate>{
	/**
	 * 根据选择的文档批量保存关联关系方法
	 * @param Relate relate 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 * @author
	 * @throws DocException 
	 * @version  2014-07-01
	 */	
	public Integer saveByDocumentIds(String documentId, String documentIds) throws Exception;
	
	public void deleteRelateDM(Relate relate);
}