package com.jc.system.content.dao.impl;

import java.util.List;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

import com.jc.foundation.domain.Attach;
import com.jc.system.content.dao.IAttachDao;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class AttachDaoImpl extends BaseClientDaoImpl<Attach> implements IAttachDao{
	@Override
	public List<Attach> queryAttachByBusinessIds(Attach attach) throws Exception {
		return  getTemplate().selectList(getNameSpace(attach) + ".queryAttachByBusinessIds", attach);
	}
}