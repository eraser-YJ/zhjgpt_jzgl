package com.jc.archive.facade;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jc.archive.domain.Archive;
import com.jc.archive.domain.Document;

/**
 *
 * @title GOA V2.0 文档管理门面类
 * @description 提供归档等服务 Copyright (c) 2014 yixunnet.com Inc. All Rights
 *              Reserved Company 长春嘉诚网络工程有限公司
 * @author zhangligang
 * @version 2014年7月8日 上午11:01:30
 */
public interface IArchiveFacadeService {

	/**
	 * 方法描述：归档
	 *
	 * @param Archive
	 *            归档参数对象
	 * @param request
	 * @return resultMap
	 *         归档成功：GlobalContext.RESULT_SUCCESS为true;归档失败：GlobalContext
	 *         .RESULT_SUCCESS为false,同时GlobalContext.RESULT_SUCCESSMESSAGE会有错误消息
	 * @author zhangligang
	 * @version 2014年7月8日上午11:50:49
	 * @see
	 */
	public Map<String, Object> archiveFile(Archive archive,
                                           HttpServletRequest request);
	/**
	 * 方法描述:新归档
	 *
	 * @param Archive
	 *            归档参数对象
	 * @param request
	 * @return resultMap
	 *         归档成功：GlobalContext.RESULT_SUCCESS为true;归档失败：GlobalContext
	 *         .RESULT_SUCCESS为false,同时GlobalContext.RESULT_SUCCESSMESSAGE会有错误消息
	 * @author xuwp
	 * @version 2016年7月8日上午11:50:49
	 * @see
	 */
	public Map<String, Object> archiveFiling(Archive archive,
                                             HttpServletRequest request) throws Exception;
}
