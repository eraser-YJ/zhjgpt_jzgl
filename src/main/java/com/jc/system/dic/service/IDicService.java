package com.jc.system.dic.service;

import java.util.List;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IDicService {

	Dic saveDic(Dic dic) throws CustomException;

	Integer update(Dic dic) throws CustomException;

	Integer save(Dic dic) throws CustomException;

	Dic get(Dic dic);

	PageManager query(Dic dic, PageManager page);

	/**
	 * @description 删除功能，如果这个字典同时也是字典类型，删除时只需把字典标志置为0即可
	 * @param dic
	 */
	Integer delete(Dic dic) throws CustomException;
	
	/**
	 * @description 批量删除功能 逻辑删除 删除字典类型下数据字典
	 * @param dic
	 * @return
	 * @throws CustomException
	 */
	Integer delForDicList(Dic dic) throws CustomException;

	List<Dic> query(Dic dic);

	List<Dic> getDicByDuty(Dic dic);
}