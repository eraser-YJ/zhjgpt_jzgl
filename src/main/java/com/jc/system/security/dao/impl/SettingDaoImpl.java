package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.system.security.dao.ISettingDao;
import com.jc.system.security.domain.Setting;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class SettingDaoImpl extends BaseClientDaoImpl<Setting> implements ISettingDao{

	public SettingDaoImpl(){}

}