package com.jc.archive.dao;

import com.jc.archive.domain.Version;
import com.jc.foundation.dao.IBaseDao;


/**
 * @title  GOA2.0源代码
 * @description  dao接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-01
 */
 
public interface IVersionDao extends IBaseDao<Version>{

	/**
	 * 方法描述：获取文档最大版本号
	 * @param backUpId
	 * @return
	 * @author zhangligang
	 * @version  2014年7月3日下午3:03:04
	 * @see
	 */
	public Integer getMaxVersion(String backUpId);

	
}
