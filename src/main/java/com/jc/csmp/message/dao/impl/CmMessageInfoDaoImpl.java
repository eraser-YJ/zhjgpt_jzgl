package com.jc.csmp.message.dao.impl;

import com.jc.csmp.message.dao.ICmMessageInfoDao;
import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-消息管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmMessageInfoDaoImpl extends BaseClientDaoImpl<CmMessageInfo> implements ICmMessageInfoDao {
	public CmMessageInfoDaoImpl(){}
}
