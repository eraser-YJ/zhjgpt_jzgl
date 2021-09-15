package com.jc.archive.service;

import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Audithis;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.IBaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-30
 */

public interface IAudithisService extends IBaseService<Audithis>{
	/**
	 * 方法描述：
	 * @param request 
	 * @param dataId 操作对象ID
	 * @param dataType 操作对象类型：0 文件夹，1 文档
	 * @param auditType 审计类型
	 * @param desc 描述
	 * @throws ArchiveException
	 * @author zhangligang
	 * @version  2014年7月1日上午10:50:29
	 * @see
	 */
	public void audithis(HttpServletRequest request, String dataId, String dataName, Integer dataType,
                         String auditType, String desc) throws ArchiveException;
	
	
	PageManager queryByPermission(Audithis audithis, PageManager page);
}