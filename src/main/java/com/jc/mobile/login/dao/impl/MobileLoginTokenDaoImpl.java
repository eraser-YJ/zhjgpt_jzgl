package com.jc.mobile.login.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.mobile.login.dao.IMobileLoginTokenDao;
import com.jc.mobile.login.domain.MobileLoginToken;
import org.springframework.stereotype.Repository;

/**
 * 移动端登录tokenDaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class MobileLoginTokenDaoImpl extends BaseClientDaoImpl<MobileLoginToken> implements IMobileLoginTokenDao {
	public MobileLoginTokenDaoImpl(){}
}
