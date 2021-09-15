package com.jc.system.gateway.domain;

import com.jc.foundation.domain.BaseBean;

import java.util.Date;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SysUserToken extends BaseBean {
    private static final long serialVersionUID = 1L;
    private String userId;
    /**token*/
    private String token;
    /**终端类型*/
    private String clientType;
    /**过期时间*/
    private Date expireTime;
    /**更新时间*/
    private Date updateTime;

    /**
     * 设置：用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * 获取：用户ID
     */
    public String getUserId() {
        return userId;
    }
    /**
     * 设置：token
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * 获取：token
     */
    public String getToken() {
        return token;
    }
    /**
     * 设置：过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    /**
     * 获取：过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
