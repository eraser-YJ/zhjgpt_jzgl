package com.jc.system.gateway.model;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class AccessToken implements AuthenticationToken {
    private String token;

    public AccessToken(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
