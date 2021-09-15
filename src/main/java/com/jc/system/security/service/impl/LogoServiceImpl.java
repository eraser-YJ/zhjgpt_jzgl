package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.SystemException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.dao.ILogoDao;
import com.jc.system.security.domain.Logo;
import com.jc.system.security.service.ILogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class LogoServiceImpl extends BaseServiceImpl<Logo> implements ILogoService{

	private ILogoDao logoDao;

	@Autowired
	private IUploadService uploadService;
	
	public LogoServiceImpl(){}
	
	@Autowired
	public LogoServiceImpl(ILogoDao logoDao){
		super(logoDao);
		this.logoDao = logoDao;
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer save(Logo logo) throws CustomException {
		try {
			propertyService.fillProperties(logo,false);
			logo.setLogoFlag(0);
			if (logo.getLogoPath() == null){
				logo.setLogoPath("");
			}
			logoDao.save(logo);
			uploadService.managerForAttach(logo.getId(),"tty_sys_logo",logo.getFileids(), logo.getDelattachIds(),0);
		} catch (Exception e) {
			SystemException se = new SystemException(e);
			se.setLogMsg(MessageUtils.getMessage("JC_SYS_002"));
			throw se;
		}
		return 1;
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer update(Logo logo) throws CustomException {
		try {
			propertyService.fillProperties(logo,true);
			if (logo.getLogoPath() == null){
				logo.setLogoPath("");
			}
			logoDao.update(logo);
			uploadService.managerForAttach(logo.getId(),"tty_sys_logo",logo.getFileids(), logo.getDelattachIds(),0);
		} catch (Exception e) {
			SystemException se = new SystemException(e);
			se.setLogMsg(MessageUtils.getMessage("JC_SYS_002"));
			throw se;
		}
		return 1;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(Logo logo) throws CustomException{
		Integer result = -1;
		String[] ids = logo.getPrimaryKeys();
		if (ids.length > 0){
			Logo logoVo = new Logo();
			logoVo.setId(ids[0]);
			logoVo.setLogoFlag(1);
			if(logoDao.get(logoVo) != null ){
				return result;
			}
		}
		try{
			propertyService.fillProperties(logo,true);
			result = logoDao.delete(logo);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(logo);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateSet(String id) throws CustomException {
		Integer result = -1;
		logoDao.updateInit();
		Logo logo = new Logo();
		logo.setId(id);
		logo = logoDao.get(logo);
		propertyService.fillProperties(logo,true);
		logo.setLogoFlag(1);
		result = logoDao.update(logo);
		return result;
	}

}