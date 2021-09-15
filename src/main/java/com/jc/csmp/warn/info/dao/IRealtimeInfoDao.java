package com.jc.csmp.warn.info.dao;

import com.jc.csmp.warn.info.domain.RealtimeInfo;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.domain.WarnStatisInfo;
import com.jc.foundation.dao.IBaseDao;

import java.util.ArrayList;
import java.util.List;


/**
 * @title  
 * @version  
 */
 
public interface IRealtimeInfoDao extends IBaseDao<RealtimeInfo>{

    public List<RealtimeInfo> queryStatis(RealtimeInfo o) throws Exception ;
    public List<WarnStatisInfo> queryWarnProcessStatis(WarnStatisInfo o) throws Exception ;
    public List<WarnStatisInfo> queryWarnStatis(WarnStatisInfo o) throws Exception;
}
