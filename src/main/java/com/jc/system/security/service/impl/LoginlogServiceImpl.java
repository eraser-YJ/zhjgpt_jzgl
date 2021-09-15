package com.jc.system.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.system.security.dao.ILoginlogDao;
import com.jc.system.security.domain.Loginlog;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ILoginlogService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class LoginlogServiceImpl extends BaseServiceImpl<Loginlog> implements ILoginlogService{

	private ILoginlogDao loginlogDao;
	
	public LoginlogServiceImpl(){}
	
	@Autowired
	public LoginlogServiceImpl(ILoginlogDao loginlogDao){
		super(loginlogDao);
		this.loginlogDao = loginlogDao;
	}

	@Override
    public void setLoginUserInfo(User user, String ip, int inOrout, int loginDevice) throws CustomException {
		Loginlog loginlogVo = new Loginlog();
		loginlogVo.setUserId(user.getId());
		loginlogVo.setLoginName(user.getLoginName());
		loginlogVo.setDisplayName(user.getDisplayName());
		loginlogVo.setIp(ip);
		loginlogVo.setLoginDevice(loginDevice);
		if(inOrout == 1){
			loginlogVo.setLoginTime(DateUtils.getSysDate());
			loginlogDao.save(loginlogVo);
		}else if(inOrout == 2){
			loginlogVo.setLogoutTime(DateUtils.getSysDate());
			loginlogDao.save(loginlogVo);
		}
	}
}