package com.jc.csmp.warn.info.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.warn.info.dao.IWarnInfoDao;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title
 */
@Repository
public class WarnInfoDaoImpl extends BaseClientDaoImpl<WarnInfo> implements IWarnInfoDao {

    public WarnInfoDaoImpl() {
    }

    public Integer updateResult(WarnInfo entity) {
        return this.getTemplate().update(this.getNameSpace(entity) + ".updateResult", entity);
    }
}