package com.jc.system.gateway.model;

/**
 * 登录表单
 * @author Administrator
 * @date 2020-07-01
 */
public class SysLoginForm {
    private String username;
    private String password;
    private String extId;
    private String extId2;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getExtId2() {
        return extId2;
    }

    public void setExtId2(String extId2) {
        this.extId2 = extId2;
    }

    @Override
    public String toString() {
        return "SysLoginForm{" +
                "username='" + username + '\'' +
                ", password='*******"  + '\'' +
                ", extId='" + extId + '\'' +
                ", extId2='" + extId2 + '\'' +
                '}';
    }
}
