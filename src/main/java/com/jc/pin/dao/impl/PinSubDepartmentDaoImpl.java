package com.jc.pin.dao.impl;

import com.jc.pin.domain.PinReSubDepartment;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.pin.dao.IPinSubDepartmentDao;
import com.jc.pin.domain.PinSubDepartment;

import java.util.List;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class PinSubDepartmentDaoImpl extends BaseClientDaoImpl<PinSubDepartment> implements IPinSubDepartmentDao{

	public PinSubDepartmentDaoImpl(){}

	@Override
	public List<PinReSubDepartment> queryPinDepartment(PinSubDepartment pinDepartment) {
		return getTemplate().selectList(getNameSpace(new PinSubDepartment()) + ".queryPinSubDepartment",pinDepartment);
	}
}