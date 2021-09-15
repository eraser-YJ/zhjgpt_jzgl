package com.jc.system.content.service.impl;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.system.content.dao.IAttachDao;
import com.jc.system.content.dao.IAttachUserPhotoDao;
import com.jc.system.content.domain.AttachBusiness;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.content.service.IAttachService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class AttachServiceImpl extends BaseServiceImpl<Attach> implements IAttachService{

	private IAttachDao attachDao;
	
	@Autowired
	private IAttachUserPhotoDao attachUserPhotoDao;
	
	@Autowired
	public AttachServiceImpl(IAttachDao attachDao){
		super(attachDao);
		this.attachDao = attachDao;
	}
	
	public AttachServiceImpl(){
		
	}

	@Override
	public List<Attach> queryAttachByBusinessIds(Attach attach) throws Exception {
		return attachDao.queryAttachByBusinessIds(attach);
	}

	@Override
    public List<Attach> queryUserPhoto(Attach o) {
        return attachUserPhotoDao.queryAll(o);
    }

	@Override
	public Integer saveUserPhoto(Attach o) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(o,false);
			result = attachUserPhotoDao.save(o);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(o);
			throw ce;
		}
		return result;
	}

	@Override
	public String getAttachIds(String businessId, String businessTable) {
		Attach param = new Attach();
		param.setBusinessTable(businessTable);
		param.setBusinessIdArray(new String[]{businessId});
		String attachIds = "";
		try {
			List<Attach> attachList = queryAttachByBusinessIds(param);

			if (attachList != null) {
				int length = 0;
				for (Attach attach : attachList) {
					if (length > 0) {
						attachIds += ",";
					}
					attachIds += attach.getId();
					length++;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return attachIds;
	}
}