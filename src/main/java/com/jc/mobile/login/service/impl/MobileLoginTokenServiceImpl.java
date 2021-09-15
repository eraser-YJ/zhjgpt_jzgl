package com.jc.mobile.login.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalUtil;
import com.jc.mobile.login.dao.IMobileLoginTokenDao;
import com.jc.mobile.login.domain.MobileLoginToken;
import com.jc.mobile.login.service.IMobileLoginTokenService;
import com.jc.mobile.util.MobileContext;
import com.jc.system.gateway.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 移动端登录tokenserviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class MobileLoginTokenServiceImpl extends BaseServiceImpl<MobileLoginToken> implements IMobileLoginTokenService {
	private static final Logger logger = LoggerFactory.getLogger(MobileLoginTokenServiceImpl.class);
	private IMobileLoginTokenDao mobileLoginTokenDao;
	private TokenGenerator tokenGenerator;

	public MobileLoginTokenServiceImpl(){}

	@Autowired
	public MobileLoginTokenServiceImpl(IMobileLoginTokenDao mobileLoginTokenDao){
		super(mobileLoginTokenDao);
		this.mobileLoginTokenDao = mobileLoginTokenDao;
		tokenGenerator = TokenGenerator.getInstance();
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(MobileLoginToken entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = mobileLoginTokenDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	public MobileLoginToken getById(String id) {
		MobileLoginToken entity = new MobileLoginToken();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.mobileLoginTokenDao.queryAll(entity));
	}

	@Override
	public MobileLoginToken getByUserId(String userId) {
		MobileLoginToken entity = new MobileLoginToken();
		entity.setUserId(userId);
		return GlobalUtil.getFirstItem(this.mobileLoginTokenDao.queryAll(entity));
	}

	@Override
	public MobileLoginToken getByToken(String token) {
		MobileLoginToken entity = new MobileLoginToken();
		entity.setToken(token);
		return GlobalUtil.getFirstItem(this.mobileLoginTokenDao.queryAll(entity));
	}

	@Override
	public MobileLoginToken createToken(String userId) {
		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + MobileContext.expire_time * 1000);
		//生成一个token
		String token = tokenGenerator.generateToken(userId, MobileContext.CLIENT_ID, expireTime);
		MobileLoginToken tokenEntity = getByUserId(userId);
		try {
			if (tokenEntity == null) {
				tokenEntity = new MobileLoginToken();
				tokenEntity.setUserId(userId);
				tokenEntity.setCreateDate(now);
				tokenEntity.setModifyDate(now);
				tokenEntity.setToken(MobileContext.CLIENT_ID);
				tokenEntity.setExpireTime(expireTime.getTime());
				this.save(tokenEntity);
			} else {
				tokenEntity.setExpireTime(expireTime.getTime());
				tokenEntity.setToken(MobileContext.CLIENT_ID);
				tokenEntity.setModifyDateNew(now);
			}
		} catch (CustomException ex) {
			ex.printStackTrace();
			logger.error("create Token failed: " + ex.getMessage());
		}
		tokenEntity.setAccessToken(token);
		return tokenEntity;
	}
}
