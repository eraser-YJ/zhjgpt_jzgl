package com.jc.system.content.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.domain.Attach;
import com.jc.system.content.dao.IAttachUserPhotoDao;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class AttachUserPhotoDaoImpl extends BaseServerDaoImpl<Attach> implements IAttachUserPhotoDao{

	public AttachUserPhotoDaoImpl(){}

	@Override
    public Integer deleteForAttachAndBusiness(Attach attach) {
		return getTemplate().update(getNameSpace(attach) + ".deleteForAttachAndBusiness", attach);
	}
}