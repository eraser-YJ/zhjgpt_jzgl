package com.jc.pin.dao;

import com.jc.pin.domain.PinReSubDepartment;
import com.jc.pin.domain.PinSubDepartment;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
public interface IPinSubDepartmentDao extends IBaseDao<PinSubDepartment>{

    /**
     * 提取部门初始化数据列表
     * @param pinDepartment
     * @return
     */
    List<PinReSubDepartment> queryPinDepartment(PinSubDepartment pinDepartment);
}
