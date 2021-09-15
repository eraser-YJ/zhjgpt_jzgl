package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

/**
 * 建设管理-项目管理Dao
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectInfoDao extends IBaseDao<CmProjectInfo> {
    /**
     * 根据项目编号完结项目
     * @param projectNumbers
     * @return
     */
    int finishByProjectNumbers(String projectNumbers);
    /**
     * 根据工程报监查询未申报的项目
     * @param CmProjectInfo
     * * @Author limin  li
     *  * @Date 2020/7/6 14:29
     * @return
     */
     PageManager queryProjectSafety(CmProjectInfo o, PageManager page) ;

}
