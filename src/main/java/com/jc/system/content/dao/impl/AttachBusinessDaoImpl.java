package com.jc.system.content.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.system.content.dao.IAttachBusinessDao;
import com.jc.system.content.domain.AttachBusiness;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class AttachBusinessDaoImpl extends BaseClientDaoImpl<AttachBusiness> implements IAttachBusinessDao{

	public AttachBusinessDaoImpl(){}

	@Override
    public Integer deleteForAttachAndBusiness(AttachBusiness attachBusiness) {
		return getTemplate().update(getNameSpace(attachBusiness) + ".deleteForAttachAndBusiness", attachBusiness);
	}

	@Override
	public List<AttachBusiness> queryForAttachAndBusiness(AttachBusiness attachBusiness) {
		return getTemplate().selectList(getNameSpace(attachBusiness) + ".queryForAttachAndBusiness", attachBusiness);
	}
}