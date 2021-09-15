package com.jc.system.security.dao;

import com.jc.system.security.domain.Logo;

import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ILogoDao extends IBaseDao<Logo>{

    public void updateInit();
}
