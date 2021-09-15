package com.jc.archive.dao;

import java.util.List;

import com.jc.archive.domain.Filing;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.DBException;


/**
 * @title  GOA2.0源代码
 * @description  dao接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-09
 */
 
public interface IFilingDao extends IBaseDao<Filing>{

	/**
	 * @description 下载路径取得
	 * @param Filing filing 实体类
	 * @return List<Filing>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-09
	 */
	public List<Filing> getDownLoad(Filing filing);
	
	/**
	 * @description 销毁归档数据
	 * @param Filing filing 实体类
	 * @return List<Filing>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-15
	 */
	public void getDeleteFiling(List<Filing> filingList);
	public Integer updateformContent(Filing o) throws DBException;
}
