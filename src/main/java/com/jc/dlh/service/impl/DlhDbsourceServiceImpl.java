package com.jc.dlh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.dlh.cache.CacheDlhDbsource;
import com.jc.dlh.dao.IDlhDbsourceDao;
import com.jc.dlh.domain.DlhDbsource;
import com.jc.dlh.service.IDlhDbsourceService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @title 统一数据资源中心
 * @author lc
 * @version 2020-04-07
 */
@Service
public class DlhDbsourceServiceImpl extends BaseServiceImpl<DlhDbsource> implements IDlhDbsourceService {

	private IDlhDbsourceDao dlhDbsourceDao;

	public DlhDbsourceServiceImpl() {
	}

	@Autowired
	public DlhDbsourceServiceImpl(IDlhDbsourceDao dlhDbsourceDao) {
		super(dlhDbsourceDao);
		this.dlhDbsourceDao = dlhDbsourceDao;
	}

	public Integer update(DlhDbsource u) throws CustomException {
		Integer v = super.update(u);
		// 清理缓存
		CacheDlhDbsource.clear();
		return v;
	}

	public Integer delete(DlhDbsource u) throws CustomException {
		Integer v = super.delete(u);
		// 清理缓存
		CacheDlhDbsource.clear();
		return v;
	}

	public Integer delete(DlhDbsource u, boolean logic) throws CustomException {
		Integer v = super.delete(u, logic);
		// 清理缓存
		CacheDlhDbsource.clear();
		return v;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhDbsource dlhDbsource) throws CustomException {

		Integer result = -1;
		try {
			result = dlhDbsourceDao.delete(dlhDbsource, false);
			// 清理缓存
			CacheDlhDbsource.clear();
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(dlhDbsource);
			throw ce;
		}
		return result;
	}

	@Override
	public DlhDbsource queryByCode(String code) {
		if (code == null || code.trim().length() <= 0) {
			return null;
		}
		DlhDbsource cond = new DlhDbsource();
		cond.setDbCode(code);
		return dlhDbsourceDao.get(cond);
	}

}