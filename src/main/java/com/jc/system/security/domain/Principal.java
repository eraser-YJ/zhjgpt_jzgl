package com.jc.system.security.domain;


import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.domain.PrincipalCore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class Principal extends PrincipalCore{

    private static final long serialVersionUID = 1L;
    /** 用户ID */
    private String id;
    /** 用户显示名 */
    private String displayName;
    /** 用户登录名 */
    private String loginName;
    /**职务*/
    private String dutyId;
    /**记录登录状态*/
    private Integer loginState = 0;
    /**用户头像*/
    private String photo;
    /**用户级别*/
    private String level;
    /**是否修改过密码*/
    private int modifyPwdFlag;
    /**机构ID*/
    private String orgId;
    private String ip;
    private String theme;
    private Map<String, Object> cacheMap;
    /**管理端ip*/
    private String manageHostPath="";
    private String serviceTicket;
    private String color;
    private String font;

    public Principal(User user) {
        super(user.getId(),user.getDisplayName(),user.getLoginName(),user.getOrgId());
        this.id = user.getId();
        this.loginName = user.getLoginName();
        this.displayName = user.getDisplayName();
        this.dutyId = user.getDutyId();
        this.photo = user.getPhoto();
        this.level = user.getLevel();
        this.modifyPwdFlag = user.getModifyPwdFlag();
        this.orgId = user.getOrgId();
        this.theme = user.getTheme();
        this.color = user.getColor();
        this.font = user.getFont();
        if ("true".equals(GlobalContext.getProperty("cas.start")) && !StringUtil.isEmpty(GlobalContext.getProperty("api.dataServer"))) {
            manageHostPath = GlobalContext.getProperty("api.dataServer");
        } else if("false".equals(GlobalContext.getProperty("cas.start")) && !GlobalContext.isSysCenter()) {
            if (RequestContextHolder.getRequestAttributes() != null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                if (request != null) {
                    this.photo = request.getContextPath() + user.getPhoto();
                }
            }
        }
    }
    @Override
    public String getLoginName() {
        return loginName;
    }
    @Override
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @Override
    public String getDisplayName() {
        return displayName;
    }
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    public String getDutyId() {
        return dutyId;
    }
    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }
    @Override
    public Map<String, Object> getCacheMap() {
        if (cacheMap==null){
            cacheMap = new HashMap<>();
        }
        return cacheMap;
    }
    public Integer getLoginState() {
        return loginState;
    }
    public void setLoginState(Integer loginState) {
        this.loginState = loginState;
    }
    public String getPhoto() {
        if(StringUtils.isEmpty(photo)){
            photo = "images/demoimg/userPhoto.png";
        }
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public int getModifyPwdFlag() {
        return modifyPwdFlag;
    }
    public void setModifyPwdFlag(int modifyPwdFlag) {
        this.modifyPwdFlag = modifyPwdFlag;
    }
    @Override
    public String getOrgId() {
        return orgId;
    }
    @Override
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getFont() {
        return font;
    }
    public void setFont(String font) {
        this.font = font;
    }
    public String getManageHostPath() {
        return manageHostPath;
    }
    public void setManageHostPath(String manageHostPath) {
        this.manageHostPath = manageHostPath;
    }
    public String getDutyIdValue() {
       return "";
    }
    public String getServiceTicket() {
        return serviceTicket;
    }
    public void setServiceTicket(String serviceTicket) {
        this.serviceTicket = serviceTicket;
    }
}
