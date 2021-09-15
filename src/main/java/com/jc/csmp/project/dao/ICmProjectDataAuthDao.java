package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectDataAuth;
import com.jc.foundation.dao.IBaseDao;

/**
 * 建设管理-项目数据权限管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectDataAuthDao extends IBaseDao<CmProjectDataAuth> {
    /**
     * 删除
     * @param businessId
     * @return
     */
    Integer deleteByBusinessId(String businessId);
}
