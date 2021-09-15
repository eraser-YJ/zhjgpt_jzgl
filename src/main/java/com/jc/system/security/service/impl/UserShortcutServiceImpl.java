package com.jc.system.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.SystemSecurityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUserShortcutDao;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.UserShortcut;
import com.jc.system.security.service.IUserShortcutService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserShortcutServiceImpl extends BaseServiceImpl<UserShortcut> implements IUserShortcutService{
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	private IUserShortcutDao userShortcutDao;
	public UserShortcutServiceImpl(){}
	@Autowired
	public UserShortcutServiceImpl(IUserShortcutDao userShortcutDao){
		super(userShortcutDao);
		this.userShortcutDao = userShortcutDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(UserShortcut userShortcut) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(userShortcut,true);
			result = userShortcutDao.delete(userShortcut);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(userShortcut);
			throw ce;
		}
		return result;
	}

	@Override
    public Integer deleteByShortcutId(String shortcutids) throws CustomException {
		UserShortcut userShortcut = new UserShortcut();
		userShortcut.setPrimaryKeys(shortcutids.split(","));
		return userShortcutDao.deleteByShortcutId(userShortcut);
	}

	@Override
    public Map<String, Object> saveorupdate(String shortcutid, String sequence) throws CustomException {
		Map<String, Object> resultMap = new HashMap<>();
		int flag = 0;
		User userInfo = SystemSecurityUtils.getUser();
		UserShortcut userShort = new UserShortcut();
		userShort.setUserid(userInfo.getId());
		flag = userShortcutDao.deleteByUserId(userShort);
		if(shortcutid != null && !"".equals(shortcutid)){
			String[] shortcutids = shortcutid.substring(3).split("@@@");
			String[] sequences = sequence.substring(3).split("@@@");
			List<UserShortcut> usershortcuts = new ArrayList<>();
			for (int i = 0; i < shortcutids.length; i++) {
				UserShortcut userShortcut = new UserShortcut();
				propertyService.fillProperties(userShortcut,false);
				userShortcut.setUserid(userInfo.getId());
				userShortcut.setShortcutid(Integer.parseInt(shortcutids[i]));
				userShortcut.setQueue(Integer.parseInt(sequences[i]));
				usershortcuts.add(userShortcut);
			}
			try {
				flag = userShortcutDao.save(usershortcuts);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		if (flag > 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		return resultMap;
	}
	
	@Override
    public List<Shortcut> getShortcutListByUserId(String userid) throws CustomException {
		return userShortcutDao.getShortcutListByUserId(userid);
	}

}