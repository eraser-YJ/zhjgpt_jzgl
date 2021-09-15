package com.jc.system.content.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.content.domain.AttachBusiness;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachBusinessDao extends IBaseDao<AttachBusiness>{

    /**
     * @param attachBusiness
     * @return
     */
    Integer deleteForAttachAndBusiness(AttachBusiness attachBusiness);

    /***
     * @param attachBusiness
     * @return
     */
    List<AttachBusiness> queryForAttachAndBusiness(AttachBusiness attachBusiness);
}
