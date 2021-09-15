package com.jc.system.gateway.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SysUserBinding extends BaseBean {
    private String userId;
    private String wxUnionId;
    private String wxAppOpenId;
    private String weWorkOpenId;
    private String weChatOpenId;
    private String dingUserId;
    private String weWorkUserId;
    private String weLinkUserId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getWxAppOpenId() {
        return wxAppOpenId;
    }

    public void setWxAppOpenId(String wxAppOpenId) {
        this.wxAppOpenId = wxAppOpenId;
    }

    public String getWeWorkOpenId() {
        return weWorkOpenId;
    }

    public void setWeWorkOpenId(String weWorkOpenId) {
        this.weWorkOpenId = weWorkOpenId;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public String getWeWorkUserId() {
        return weWorkUserId;
    }

    public void setWeWorkUserId(String weWorkUserId) {
        this.weWorkUserId = weWorkUserId;
    }

    public String getWeLinkUserId() {
        return weLinkUserId;
    }

    public void setWeLinkUserId(String weLinkUserId) {
        this.weLinkUserId = weLinkUserId;
    }
}
