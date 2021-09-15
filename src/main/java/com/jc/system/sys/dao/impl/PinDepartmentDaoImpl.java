package com.jc.system.sys.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.sys.domain.PinReDepartment;
import org.springframework.stereotype.Repository;

import com.jc.system.sys.dao.IPinDepartmentDao;
import com.jc.system.sys.domain.PinDepartment;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class PinDepartmentDaoImpl extends BaseServerDaoImpl<PinDepartment> implements IPinDepartmentDao{

	public PinDepartmentDaoImpl(){}

	@Override
	public List<PinReDepartment> queryPinDepartment(PinDepartment pinDepartment) {
		return getTemplate().selectList(getNameSpace(new PinDepartment()) + ".queryPinDepartment",pinDepartment);
	}
}