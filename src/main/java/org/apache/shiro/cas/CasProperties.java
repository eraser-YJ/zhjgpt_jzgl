package org.apache.shiro.cas;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.Subsystem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class CasProperties {

    private String localUrl;
    private String casServerUrl;
    private String loginUrl;
    private String logoutUrl;
    private String kickoutUrl;
    private String timeoutUrl;
    private String casStart;
    private String loginFilter;
    private String casRealm;
    private String casSessionManager;
    private static CasProperties casProperties;

    public static CasProperties getInstance() {
        if (casProperties == null) {
            casProperties = new CasProperties();
        }
        return casProperties;
    }

    public String getLocalUrl() {
        this.localUrl = null;
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            return "";
        }
        if(RequestContextHolder.getRequestAttributes() == null){
            return "";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request!=null && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"))<0){
            for(int i=0; i<10; i++){
                if(!StringUtil.isEmpty(GlobalContext.getProperty("cas.localUrl"+i)) && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"+i))>-1){
                    this.localUrl =GlobalContext.getProperty("cas.localUrl"+i);
                    break;
                }
            }
        }
        if(this.localUrl == null){
            this.localUrl =GlobalContext.getProperty("cas.localUrl");
        }
        return this.localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getCasServerUrl() {
        this.casServerUrl = null;
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            return "";
        }
        if(RequestContextHolder.getRequestAttributes() == null){
            return "";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request!=null && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"))<0){
            for(int i=0; i<10; i++){
                if(!StringUtil.isEmpty(GlobalContext.getProperty("cas.casServerUrl"+i)) && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"+i))>-1){
                    this.casServerUrl = GlobalContext.getProperty("cas.casServerUrl"+i);
                    break;
                }
            }
        }
        if(this.casServerUrl == null){
            this.casServerUrl = GlobalContext.getProperty("cas.casServerUrl");
        }
        return this.casServerUrl;
    }

    public void setCasServerUrl(String casServerUrl) {
        this.casServerUrl = casServerUrl;
    }

    public String getLoginUrl() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            return "/login";
        }
        loginUrl = this.getCasServerUrl()+"/login?service="+this.getLocalUrl()+"/login";
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            return "/logout";
        }
        logoutUrl = this.getCasServerUrl()+"/logout?service="+this.getLocalUrl()+"/login";
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getCasStart() {
        return casStart;
    }

    public void setCasStart(String casStart) {
        this.casStart = casStart;
    }

    public String getLoginFilter() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            this.loginFilter = "authc";
        }else{
            this.loginFilter = "casFilter";
        }
        return this.loginFilter;
    }

    public void setLoginFilter(String loginFilter) {
        this.loginFilter = loginFilter;
    }

    public String getCasRealm() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            this.casRealm = "systemRealm";
        }else{
            this.casRealm = "casRealm";
        }
        return casRealm;
    }

    public void setCasRealm(String casRealm) {
        this.casRealm = casRealm;
    }

    public String getCasSessionManager() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            this.casSessionManager = "sessionManager";
        }else{
            this.casSessionManager = "casSessionManager";
        }
        return casSessionManager;
    }

    public void setCasSessionManager(String casSessionManager) {
        this.casSessionManager = casSessionManager;
    }

    public String getKickoutUrl() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            kickoutUrl = "/sys/login?kickout=true";
        }else{
            kickoutUrl = this.getCasServerUrl()+"/logout?kickout=true&service="+this.getLocalUrl()+"/login";
        }
        return kickoutUrl;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public String getTimeoutUrl() {
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            timeoutUrl = "/login";
        }else{
            timeoutUrl = this.getCasServerUrl()+"/logout?timeout=true&service="+this.getLocalUrl()+"/login";
        }
        return timeoutUrl;
    }

    public static String getSubsystemUrl(Subsystem subsystem){
        String subsystemUrl = subsystem.getUrl();
        if(!"true".equals(GlobalContext.getProperty("cas.start"))){
            return subsystemUrl;
        }
        if(RequestContextHolder.getRequestAttributes() == null){
            return subsystemUrl;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request!=null && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"))<0){
            Map<String,String> subsystemMap = new HashMap<String,String>();
            subsystemMap.put("URL1",subsystem.getExtStr1());
            subsystemMap.put("URL2",subsystem.getExtStr2());
            subsystemMap.put("URL3",subsystem.getExtStr3());
            subsystemMap.put("URL4",subsystem.getExtStr4());
            subsystemMap.put("URL5",subsystem.getExtStr5());
            for(int i=1; i<=5; i++){
                if(!StringUtil.isEmpty(GlobalContext.getProperty("cas.localUrl"+i)) && request.getRequestURL().indexOf(GlobalContext.getProperty("cas.localUrl"+i))>-1){
                    subsystemUrl =subsystemMap.get("URL"+i);
                    break;
                }
            }
        }
        return subsystemUrl;
    }

    public void setTimeoutUrl(String timeoutUrl) {
        this.timeoutUrl = timeoutUrl;
    }
}
