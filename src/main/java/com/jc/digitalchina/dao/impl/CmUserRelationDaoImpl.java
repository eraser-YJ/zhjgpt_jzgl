package com.jc.digitalchina.dao.impl;

import com.jc.digitalchina.dao.ICmUserRelationDao;
import com.jc.digitalchina.domain.CmUserRelation;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-自定义字典项DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmUserRelationDaoImpl extends BaseClientDaoImpl<CmUserRelation> implements ICmUserRelationDao {
	public CmUserRelationDaoImpl(){}
}
