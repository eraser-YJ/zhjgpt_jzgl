package com.jc.supervise.warning.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.supervise.warning.dao.ICmSupervisionMessageDao;
import com.jc.supervise.warning.domain.CmSupervisionMessage;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-督办管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmSupervisionMessageDaoImpl extends BaseClientDaoImpl<CmSupervisionMessage> implements ICmSupervisionMessageDao {
	public CmSupervisionMessageDaoImpl(){}
}
