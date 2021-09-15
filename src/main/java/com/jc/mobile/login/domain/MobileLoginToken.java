package com.jc.mobile.login.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 移动端登录token
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class MobileLoginToken extends BaseBean {
    private static final long serialVersionUID = 1L;
    public MobileLoginToken(){}
    private String userId;
    private String token;
    private Long expireTime;
    /**传递给前台的加密串*/
    private String accessToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
