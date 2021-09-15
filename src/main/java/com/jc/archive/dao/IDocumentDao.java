package com.jc.archive.dao;

import com.jc.archive.domain.Document;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;


/**
 * @title  GOA2.0源代码
 * @description  dao接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
 
public interface IDocumentDao extends IBaseDao<Document>{

	/**
	 * 方法描述：删除文档到垃圾箱
	 * @param doc Document对象，将删除primaryKeys指定的记录
	 * @return
	 * @author zhangligang
	 * @version  2014年6月20日上午8:51:58
	 * @see
	 */
	public int deleteDocToRecycle(Document doc);
	
	/**
	 * 方法描述：生成当前年度的文档流水号
	 * @param year
	 * @return 流水号
	 * @author zhangligang
	 * @version  2014年7月1日下午7:52:33
	 * @see
	 */
	public String getSeq(Document d);

	/**
	 * @description 根据文档id获取未关联文档的集合
	 * @param Document 实体类
	 * @param page 分页信息
	 * @return map：list 查询的记录 ，page分页信息
	 * @author 
	 * @version 2014-07-02
	 */
	public PageManager queryByRelate(Document document, final PageManager page);

	/**
	 * @description 销毁归档数据
	 * @param Document doc 实体类
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-15
	 */
	public int deleteList(Document doc);
}
