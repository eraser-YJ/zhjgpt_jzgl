package com.jc.system.gateway.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.gateway.dao.ISysUserTokenDao;
import com.jc.system.gateway.domain.SysUserToken;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.gateway.utils.ApiResponse;
import com.jc.system.gateway.utils.TokenGenerator;
import com.jc.system.gateway.web.DingLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends BaseServiceImpl<SysUserToken> implements ISysUserTokenService {
    private static final Logger logger = LoggerFactory.getLogger(SysUserTokenServiceImpl.class);

    @Resource
    private ISysUserTokenDao sysUserBindingDao;
    private TokenGenerator tokenGenerator;

    @Autowired
    public SysUserTokenServiceImpl(ISysUserTokenDao dao) {
        super(dao);
        this.sysUserBindingDao = dao;
        tokenGenerator = TokenGenerator.getInstance();
    }

    public SysUserTokenServiceImpl() {

    }

    //12小时后过期
    private final int EXPIRE = 3600 * 12;


    @Override
    public ApiResponse createToken(String userId, String clientType) {
        logger.info("start create token ,params : userId -> {},clientType -> {}", userId, clientType);
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //生成一个token
        String token = tokenGenerator.generateToken(userId, clientType, expireTime);

        SysUserToken sysUserToken = new SysUserToken();
        sysUserToken.setUserId(userId);
        sysUserToken.setClientType(clientType);
        //判断是否生成过token
        try {
            SysUserToken tokenEntity;
            tokenEntity = sysUserBindingDao.get(sysUserToken);

            if (tokenEntity == null) {
                tokenEntity = new SysUserToken();
                tokenEntity.setUserId(userId);
                tokenEntity.setToken(token);
                tokenEntity.setClientType(clientType);
                tokenEntity.setUpdateTime(now);
                tokenEntity.setExpireTime(expireTime);

                tokenEntity.setCreateDate(now);
                tokenEntity.setModifyDate(now);

                //保存token
                sysUserBindingDao.save(tokenEntity);
            } else {
                tokenEntity.setToken(token);
                tokenEntity.setUpdateTime(now);
                tokenEntity.setExpireTime(expireTime);

                tokenEntity.setModifyDateNew(now);
                //更新token
                sysUserBindingDao.update(tokenEntity);
            }
        } catch (CustomException e) {
            logger.error("create Token failed: " + e.getMessage());
        }
        return ApiResponse.ok().put("token", token).put("expire", EXPIRE);
    }

    @Override
    public void logout(String userId) {
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setUserId(userId);
        tokenEntity = sysUserBindingDao.get(tokenEntity);
        try {
            if (tokenEntity != null)
                sysUserBindingDao.delete(tokenEntity);
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public SysUserToken queryByToken(String token) {
//        return tokenMap.get(token);
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setToken(token);
        tokenEntity = sysUserBindingDao.get(tokenEntity);
        return tokenEntity;
    }
}
