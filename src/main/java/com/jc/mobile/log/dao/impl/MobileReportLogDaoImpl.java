package com.jc.mobile.log.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.mobile.log.dao.IMobileReportLogDao;
import com.jc.mobile.log.domain.MobileReportLog;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-移动端上报日志DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class MobileReportLogDaoImpl extends BaseClientDaoImpl<MobileReportLog> implements IMobileReportLogDao {
	public MobileReportLogDaoImpl(){}
}
