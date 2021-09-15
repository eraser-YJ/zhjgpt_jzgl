package com.jc.csmp.warn.info.dao;

import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.foundation.dao.IBaseDao;


/**
 * @title  
 * @version  
 */
 
public interface IWarnInfoDao extends IBaseDao<WarnInfo>{

	public Integer updateResult(WarnInfo entity);

}
