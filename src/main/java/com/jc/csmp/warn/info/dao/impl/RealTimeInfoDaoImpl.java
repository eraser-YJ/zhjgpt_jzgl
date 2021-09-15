package com.jc.csmp.warn.info.dao.impl;

import com.jc.csmp.warn.info.dao.IRealtimeInfoDao;
import com.jc.csmp.warn.info.dao.IWarnInfoDao;
import com.jc.csmp.warn.info.domain.RealtimeInfo;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.domain.WarnStatisInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @title
 */
@Repository
public class RealTimeInfoDaoImpl extends BaseClientDaoImpl<RealtimeInfo> implements IRealtimeInfoDao {
    public RealTimeInfoDaoImpl() {
    }

    public List<RealtimeInfo> queryStatis(RealtimeInfo o) throws Exception {
        try {
            return  this.getTemplate().selectList(    "RealtimeInfoDao.query_statis" , o);
        } catch (Exception var5) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WarnStatisInfo> queryWarnProcessStatis(WarnStatisInfo o) throws Exception {
        try {
            return  this.getTemplate().selectList(    "RealtimeInfoDao.query_warn" , o);
        } catch (Exception var5) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WarnStatisInfo> queryWarnStatis(WarnStatisInfo o) throws Exception {
        try {
            return  this.getTemplate().selectList(    "RealtimeInfoDao.query_warn_statis" , o);
        } catch (Exception var5) {
            return new ArrayList<>();
        }
    }

}