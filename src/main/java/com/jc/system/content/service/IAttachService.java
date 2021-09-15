package com.jc.system.content.service;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.domain.Attach;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachService extends IBaseService<Attach>{
	/**
	 * 使用业务id拼接的字符串查询所有的附件
	 * @param attach
	 * @return
	 * @throws Exception
	 */
	List<Attach> queryAttachByBusinessIds(Attach attach) throws Exception;

	/**
	 * 查询用户头像
	 * @param o
	 * @return
	 */
	List<Attach> queryUserPhoto(Attach o);

	/**
	 * 保存用户头像
	 * @param o
	 * @return
	 * @throws CustomException
	 */
	Integer saveUserPhoto(Attach o) throws CustomException;

	/**
	 * 获取附件id，逗号分隔
	 * @param businessId
	 * @param businessTable
	 * @return
	 */
	String getAttachIds(String businessId, String businessTable);
}