package com.jc.digitalchina.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 与神州用户关系iao
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmUserRelation extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmUserRelation(){}
    private String userId;
    private String uuid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
