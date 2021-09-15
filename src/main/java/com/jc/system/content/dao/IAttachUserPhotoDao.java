package com.jc.system.content.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.Attach;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachUserPhotoDao extends IBaseDao<Attach>{

    /**
     * @param attach
     * @return
     */
    Integer deleteForAttachAndBusiness(Attach attach);
}
