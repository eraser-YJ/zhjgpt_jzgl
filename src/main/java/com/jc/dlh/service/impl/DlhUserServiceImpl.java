package com.jc.dlh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.dlh.cache.CacheDlhUser;
import com.jc.dlh.dao.IDlhUserDao;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhUserService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @title 统一数据资源中心
 * @description 业务服务类 Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved Company 长春奕迅
 * @author lc
 * @version 2020-03-13
 */
@Service
public class DlhUserServiceImpl extends BaseServiceImpl<DlhUser> implements IDlhUserService {

	private IDlhUserDao dlhUserDao;

	public DlhUserServiceImpl() {
	}

	@Autowired
	public DlhUserServiceImpl(IDlhUserDao dlhUserDao) {
		// 清理缓存
		super(dlhUserDao);
		this.dlhUserDao = dlhUserDao;
	}

	public Integer update(DlhUser u) throws CustomException {
		Integer v = super.update(u);
		// 清理缓存
		CacheDlhUser.clear();
		return v;
	}

	public Integer delete(DlhUser u) throws CustomException {
		Integer v = super.delete(u);
		// 清理缓存
		CacheDlhUser.clear();
		return v;
	}

	public Integer delete(DlhUser u, boolean logic) throws CustomException {

		Integer v = super.delete(u, logic);
		// 清理缓存
		CacheDlhUser.clear();
		return v;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhUser dlhUser 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2020-03-13
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhUser dlhUser) throws CustomException {
		Integer result = -1;
		try {
			result = dlhUserDao.delete(dlhUser, false);
			// 清理缓存
			CacheDlhUser.clear();
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(dlhUser);
			throw ce;
		}
		return result;
	}

	@Override
	public DlhUser queryByUserName(String userName) {
		if (userName == null || userName.trim().length() <= 0) {
			return null;
		}
		DlhUser cond = new DlhUser();
		cond.setDlhUsername(userName.trim());
		List<DlhUser> userList = dlhUserDao.queryAll(cond);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}

}