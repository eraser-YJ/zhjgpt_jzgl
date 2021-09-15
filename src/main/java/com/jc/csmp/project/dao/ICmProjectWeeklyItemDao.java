package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.util.Result;

/**
 * 建设管理-周报事项Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectWeeklyItemDao extends IBaseDao<CmProjectWeeklyItem> {
    /**
     * 根据id更新weeklyId
     * @param entity
     * @return
     */
    Integer updateWeeklyByIds(CmProjectWeeklyItem entity);
}
