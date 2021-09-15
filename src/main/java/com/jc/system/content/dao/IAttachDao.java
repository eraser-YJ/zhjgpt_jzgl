package com.jc.system.content.dao;

import java.util.List;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachDao extends IBaseDao<Attach>{
	/**
	 * 使用业务id拼接的字符串查询所有的附件
	 * @param attach
	 * @return
	 * @throws Exception
	 */
	List<Attach> queryAttachByBusinessIds(Attach attach) throws Exception;
}
