package com.jc.supervise.point.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.util.Result;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;

/**
 * 建设管理-监察点数据来源管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmSupervisionPointColumnDao extends IBaseDao<CmSupervisionPointColumn> {
    /**
     * 修改supervisionId
     * @param newSupervisionId
     * @param oldSupervisionId
     * @return
     */
    Integer updateSupervisionId(String newSupervisionId, String oldSupervisionId);
}
