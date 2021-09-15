package com.jc.system.sys.dao;

import com.jc.system.sys.domain.PinDepartment;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.sys.domain.PinReDepartment;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPinDepartmentDao extends IBaseDao<PinDepartment>{

    /**
     * 提取部门初始化数据列表
     * @param pinDepartment
     * @return
     */
    List<PinReDepartment> queryPinDepartment(PinDepartment pinDepartment);
}
