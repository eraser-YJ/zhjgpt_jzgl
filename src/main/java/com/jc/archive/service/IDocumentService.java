package com.jc.archive.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Filing;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public interface IDocumentService extends IBaseService<Document>{
	/**
	 * 方法描述：文件复制保存关联表数据
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	public int insertCollection(Document ment, String id) throws ArchiveException;
	/**
	 * 方法描述：文件复制保存关联表数据
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	public int updateCollection(Document ment, String id) throws ArchiveException;
	
	/**
	 * 方法描述：删除文档到垃圾箱
	 * @param doc Document对象， 将删除primaryKeys指定的文
	 * @return	 受影响记录数
	 * @author zhangligang
	 * @version  2014年6月20日上午8:48:05
	 * @see
	 */
	public int deleteDocToRecycle(Document doc);
	
	/**
	 * 方法描述：生成当前年度文档流水号
	 * @return
	 * @author zhangligang
	 * @version  2014年7月1日下午7:54:57
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
	 * 方法描述：获取新文件名，已处理文件名重复的情况
	 * @param folderId
	 * @param name
	 * @return
	 * @throws ArchiveException
	 * @author zhangligang
	 * @version  2014年7月2日上午10:22:30
	 * @see
	 */
	public String getNewFileName(String folderId, String name, String ext) throws ArchiveException;
	
	/**
	 * @获得附件信息
	 * @param filing 实体类
	 * @param page 分页信息
	 * @return map：list 查询的记录 ，page分页信息
	 * @author 
	 * @version 2014-07-09
	 */
	public List<Filing> queryFiling(Filing filing) throws CustomException;

	/**
	 * @附件下载
	 * @version 2014-07-10
	 * @throws CustomException 
	 */
	public void downLoad(String id, HttpServletRequest request, HttpServletResponse response) throws CustomException;
	
	/**
	 * @附件下载
	 * @version 2014-07-10
	 * @throws CustomException 
	 */
	public int downLoadCheck(String id, HttpServletRequest request) throws CustomException;
	
	/**
	 * @销毁归档数据
	 * @author weny
	 * @version 2014-07-15
	 * @throws CustomException 
	 */
	public void deleteFiling(String id, HttpServletRequest request) throws CustomException;
	
	PageManager  getDataByPermission(Document folder);
	String getNewFileName(String folderId, String name, String ext, String type)
			throws ArchiveException;
	
	String getNewFileName(Document d, String name) throws ArchiveException;
	String getNewFileNameByModel(String folderId, String name, String ext,
                                 int model) throws ArchiveException;
}