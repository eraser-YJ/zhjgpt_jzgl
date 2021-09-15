package com.jc.system.content.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.content.dao.IAttachBusinessDao;
import com.jc.system.content.dao.IAttachBusinessUserPhotoDao;
import com.jc.system.content.domain.AttachBusiness;
import com.jc.system.content.service.IAttachBusinessService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class AttachBusinessServiceImpl extends BaseServiceImpl<AttachBusiness> implements IAttachBusinessService {

	private static final Logger logger = Logger.getLogger(AttachBusinessServiceImpl.class);

	private IAttachBusinessDao attachBusinessDao;
	
	@Autowired
	private IAttachBusinessUserPhotoDao attachBusinessUserPhotoDao;
	
	public AttachBusinessServiceImpl(){}
	
	@Autowired
	public AttachBusinessServiceImpl(IAttachBusinessDao attachBusinessDao){
		super(attachBusinessDao);
		this.attachBusinessDao = attachBusinessDao;
	}

	@Override
    public Integer deleteForAttachAndBusiness(AttachBusiness attachBusiness) {
		return attachBusinessDao.deleteForAttachAndBusiness(attachBusiness);
	}

	@Override
	public List<AttachBusiness> queryForAttachAndBusiness(AttachBusiness attachBusiness) {
		return attachBusinessDao.queryForAttachAndBusiness(attachBusiness);
	}

	@Override
    public Boolean deleteUserPhoto(String userId) {
		try {
			AttachBusiness attachBusiness = new AttachBusiness();
			attachBusiness.setBusinessId(userId);
			attachBusiness.setBusinessTable("tty_sys_user");
			attachBusiness.setBusinessSource("0");
			List<AttachBusiness> attbus = attachBusinessUserPhotoDao.queryAll(attachBusiness);
			if (attbus.size() > 0) {
				String[] attachIds = new String[attbus.size()];
				for (int i = 0; i < attbus.size(); i++) {
					AttachBusiness business = attbus.get(i);
					attachIds[i] = business.getId();
				}
				AttachBusiness business = new AttachBusiness();
				business.setPrimaryKeys(attachIds);
				attachBusinessUserPhotoDao.delete(business, false);
			}
		} catch (CustomException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	@Override
    public Integer saveUserPhoto(AttachBusiness attachBusiness) throws CustomException {
		int result;
		if(GlobalContext.TRUE.equals(GlobalContext.getProperty("cas.start"))){
			result = attachBusinessUserPhotoDao.save(attachBusiness);
		}else{
			result = attachBusinessDao.save(attachBusiness);
		}
        return result;
    }
}