package com.jc.archive.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.IBaseService;
import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Filing;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-09
 */

public interface IFilingService extends IBaseService<Filing>{

	/**
	 * 下载附件
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException 
	 */
	public void downLoad(String id, String dmName, HttpServletRequest request,
                         HttpServletResponse response) throws ArchiveException;
	
	/**
	 * 下载附件check
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException 
	 */
	public int downLoadCheck(String id, HttpServletRequest request) throws ArchiveException;
	

	/**
	 * 下载附件check
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException 
	 */
	public void getDeleteFiling(List<Filing> file) throws ArchiveException;

	public String filing2Pdf(Filing filing, HttpServletRequest request);
	public Integer updateformContent(Filing o) throws DBException;
}