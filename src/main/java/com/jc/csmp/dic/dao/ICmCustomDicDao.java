package com.jc.csmp.dic.dao;

import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.foundation.dao.IBaseDao;

/**
 * 建设管理-自定义字典项Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmCustomDicDao extends IBaseDao<CmCustomDic> {
    /**
     * 根据父编码获取数量
     * @param entity
     * @return
     */
    long queryCountByParentIds(CmCustomDic entity);
}
