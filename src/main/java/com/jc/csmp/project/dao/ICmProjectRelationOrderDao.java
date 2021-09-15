package com.jc.csmp.project.dao;

import com.jc.csmp.project.domain.CmProjectRelationOrder;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.DBException;


/**
 * 建设管理-项目工程联系单Dao
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectRelationOrderDao extends IBaseDao<CmProjectRelationOrder>{

    /**
     * 保存关系
     * @param relationId
     * @param personType: 1、签收部门 2、抄送部门
     * @param deptId
     * @return
     */
	Integer insertRelationDept(String relationId, String personType, String deptId) throws DBException;
}
