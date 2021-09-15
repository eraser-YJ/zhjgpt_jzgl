package com.jc.system.content.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.content.domain.AttachBusiness;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachBusinessUserPhotoDao extends IBaseDao<AttachBusiness>{
	Boolean deleteUserPhoto(Long userId);
}
