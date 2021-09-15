package com.jc.csmp.equipment.info.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.equipment.info.dao.IEquipmentInfoDao;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class EquipmentInfoDaoImpl extends BaseClientDaoImpl<EquipmentInfo> implements IEquipmentInfoDao{

	public EquipmentInfoDaoImpl(){}

}