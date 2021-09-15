package com.jc.system.content.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.system.content.dao.IAttachBusinessUserPhotoDao;
import com.jc.system.content.domain.AttachBusiness;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class AttachBusinessUserPhotoDaoImpl extends BaseServerDaoImpl<AttachBusiness> implements IAttachBusinessUserPhotoDao{

	private static final Logger logger = Logger.getLogger(AttachBusinessUserPhotoDaoImpl.class);

	public AttachBusinessUserPhotoDaoImpl(){}

	@Override
    public Boolean deleteUserPhoto(Long userId) {
		try {
			AttachBusiness attachBusiness = new AttachBusiness();
			attachBusiness.setBusinessId(userId.toString());
			attachBusiness.setBusinessTable("tty_sys_user");
			attachBusiness.setBusinessSource("0");
			List<AttachBusiness> attbus = this.queryAll(attachBusiness);
			if (attbus.size() > 0) {
				String [] attachs = new String[attbus.size()];
				for (int i = 0 ; i < attbus.size() ; i++) {
					AttachBusiness business = attbus.get(i);
					attachs[i] = business.getId();
				}
				AttachBusiness business = new AttachBusiness();
				business.setPrimaryKeys(attachs);
				this.delete(business, false);
			}
		} catch (CustomException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
}