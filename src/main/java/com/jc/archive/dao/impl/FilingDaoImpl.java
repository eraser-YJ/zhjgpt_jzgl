package com.jc.archive.dao.impl;

import java.util.List;

import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.archive.dao.IFilingDao;
import com.jc.archive.domain.Filing;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-07-09
 */
@Repository
public class FilingDaoImpl extends BaseClientDaoImpl<Filing> implements IFilingDao{

	public FilingDaoImpl(){}

	/**
	 * @description 下载路径取得
	 * @param Filing filing 实体类
	 * @return List<Filing>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-09
	 */
	public List<Filing> getDownLoad(Filing filing) {
		return getTemplate().selectList(getNameSpace(filing) + ".queryDownLoad", filing);
	}

	public Integer updateformContent(Filing o) throws DBException {
		Integer result = Integer.valueOf(-1);

		try {
			result = Integer.valueOf(this.getTemplate().update(this.getNameSpace(o) + "." + "updateformContent", o));
		} catch (Exception var5) {
			DBException exception = new DBException(var5);
			exception.setLogMsg("数据库更新数据发生错误");
			throw exception;
		}

		if(result.intValue() == 0) {
			throw new ConcurrentException("数据已被修改，请刷新后重新操作");
		} else {
			return result;
		}
	}
	/**
	 * @description 销毁归档数据
	 * @param Filing filing 实体类
	 * @return List<Filing>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-15
	 */
	public void getDeleteFiling(List<Filing> filingList) {
		for(int i=0;i<filingList.size();i++){
			Filing filing = filingList.get(i);
			getTemplate().delete(getNameSpace(filing) + ".deleteList", filing);
		}
	}
}