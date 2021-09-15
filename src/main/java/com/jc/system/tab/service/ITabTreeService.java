package com.jc.system.tab.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.tab.domain.TabTree;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ITabTreeService extends IBaseService<TabTree>{
	/**
	 * 根据主键删除多条记录方法
	 * @param tabTree
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(TabTree tabTree) throws CustomException;
}