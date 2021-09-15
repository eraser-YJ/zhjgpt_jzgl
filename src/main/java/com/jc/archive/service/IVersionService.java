package com.jc.archive.service;

import com.jc.foundation.service.IBaseService;
import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Version;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-01
 */

public interface IVersionService extends IBaseService<Version>{


	/**
	 * 方法描述：根据文档生成新版本信息.版本号为Document实例中版本号。版本描述需要调用方再次设置。
	 * @param document
	 * @return version 版本实例
	 * @author zhangligang
	 * @version  2014年7月3日下午2:16:02
	 * @throws ArchiveException 
	 * @see
	 */
	public Version createVersion(Document document) throws ArchiveException;
	
	/**
	 * 方法描述：获取文档的最新版本号
	 * @param id 文档Id
	 * @return
	 * @author zhangligang
	 * @version  2014年7月8日下午4:23:09
	 * @see
	 */
	public Integer getMaxVersion(String id);
}