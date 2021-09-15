package com.jc.system.security;

import com.google.common.collect.Maps;
import com.jc.crypto.utils.SM2Utils;
import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.cache.CacheUtils;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.applog.domain.Operlog;
import com.jc.system.applog.service.IOperlogService;
import com.jc.system.common.util.Digests;
import com.jc.system.common.util.Encodes;
import com.jc.system.common.util.Utils;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.dao.ISystemDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.ISysUserRoleService;
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserIpService;
import com.jc.system.security.service.IUserService;
import com.jc.system.util.SettingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.cas.client.session.HashMapBackedSessionMappingStorage;
import org.cas.client.session.SingleSignOutFilter;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SystemSecurityUtils {

    private SystemSecurityUtils() {
        throw new IllegalStateException("SystemSecurityUtils class");
    }

    protected static transient final Logger logger = Logger.getLogger(SystemSecurityUtils.class);
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    private static final String CACHE_USER = "user";

    private static IUserService userService = SpringContextHolder.getBean(IUserService.class);
    private static IUserIpService userIpService = SpringContextHolder.getBean(IUserIpService.class);
    private static ISysUserRoleService userRoleService = SpringContextHolder.getBean(ISysUserRoleService.class);
    private static SessionDAO sessionDAO;
    private static ISystemDao systemDao = SpringContextHolder.getBean(ISystemDao.class);
    private static IUniqueService uniqueService = SpringContextHolder.getBean(IUniqueService.class);
    private static IOperlogService operlogService = SpringContextHolder.getBean(IOperlogService.class);

    public static final String CACHE_USER_INFO = "user_info_";
    public static final String CACHE_DEPT_INFO = "dept_info_";

    public static SessionDAO getSessionDAO() {
        if (sessionDAO == null) {
            sessionDAO = SpringContextHolder.getBean(SessionDAO.class);
        }
        return sessionDAO;
    }

    private static boolean manualValue = true;
    private static String userIdValue = "0";

    /**
     * 是否为手动调用 0为自动 其他值为手动 且手动值为用户Id
     */
    public static boolean isManualValue() {
        return manualValue;
    }

    public static void setManualValue(boolean manualValue) {
        SystemSecurityUtils.manualValue = manualValue;
    }

    public static String getUserIdValue() {
        return userIdValue;
    }

    public static void setUserIdValue(String userIdValue) {
        SystemSecurityUtils.userIdValue = userIdValue;
    }

    /**
     * 获取用户信息
     */
    public static User getUser() {
        try{
            User user = (User)getCache(CACHE_USER);
            if(user == null && isManualValue()){
                Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
                if (principal != null) {
                    user = userService.getUser(principal.getId());
                    try {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(user.getId());
                        user.setSysUserRole(userRoleService.queryAll(userRole));
                    } catch (CustomException e) {
                        logger.error(e);
                    }
                    putCache(CACHE_USER, user);
                }
            } else if(user == null && !isManualValue()){
                user = userService.getUser(getUserIdValue());
                try {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(user.getId());
                    user.setSysUserRole(userRoleService.queryAll(userRole));
                } catch (CustomException e) {
                    logger.error(e);
                }
            }
            return user;
        } catch(Exception e){
            return null;
        }
    }

    /**
     * 获取唯一标识
     */
    public static int getUniqueForCode(String userCode,String name){
        Unique unique = new Unique();
        unique.setUuid(userCode);
        int reValue = 0;
        try {
            if(isNotEmpty(userCode) && isNotName(name)){
                reValue = uniqueService.queryAll(unique).size();
            } else {
                reValue = 2;
            }
            if(isName(name)){
                reValue = 1;
            }
            return reValue;
        } catch (CustomException e) {
            logger.error(e);
        }
        return reValue;
    }

    private static boolean isNotEmpty(String val){
        return val != null && !val.isEmpty();
    }

    private static boolean isName(String val){
        if(val == null){
            return false;
        }
        if("manager".equals(val) || "security".equals(val) || "audit".equals(val)){
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNotName(String val){
        if(val != null && !"manager".equals(val)
                && !"security".equals(val) && !"audit".equals(val)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改密码后设置登陆时间
     */
    public static void setFirstLoginState(){
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            principal.setModifyPwdFlag(1);
        }
    }

    /**
     * 判断是否是第一次登陆
     */
    public static boolean isFirstLogin() {
        /*Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            User user = userService.getUser(principal.getId());
            String valPwd = GlobalContext.getProperty("sys.valPassword");
            if (valPwd == null){
                valPwd = "yes";
            }
            if(principal.getModifyPwdFlag() == 0){
                return true;
            } else if("yes".equals(valPwd) && user.getExtDate1() != null && DateUtils.pastDays(user.getExtDate1()) > 7) {
                return true;
            } else {
                return false;
            }
        }*/
        return false;
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword,String pubKey) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt,
                HASH_INTERATIONS);
        String shaPwd = Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
        String smPwd = shaPwd;

        try {
            smPwd =  SM2Utils.encrypt(pubKey,shaPwd);
            return smPwd;
        } catch (IOException e) {
            logger.error(e,e.getCause());
        } finally {
            return smPwd;
        }
    }

    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password,String priKey) {
        boolean returnVal = false;
        if (password.trim().length() > 64){
            try {
                String shaPwd = new String(SM2Utils.decrypt(priKey,password));
                byte[] salt = Encodes.decodeHex(shaPwd.substring(0,16));
                byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
                returnVal = shaPwd.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
                return returnVal;
            } catch (IOException e) {
                logger.error(e,e.getCause());
            } finally {
                return returnVal;
            }
        } else {
            byte[] salt = Encodes.decodeHex(password.substring(0,16));
            byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
            return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
        }
    }

    public static byte[] getSalt(String password,String priKey) {
        byte[] salt = null;
        if (password.trim().length() > 64){
            try {
                String shaPwd = new String(SM2Utils.decrypt(priKey,password));
                salt = Encodes.decodeHex(shaPwd.substring(0,16));
                return salt;
            } catch (IOException e) {
                logger.error(e,e.getCause());
            } finally {
                return salt;
            }
        }else {
            salt = Encodes.decodeHex(password.substring(0,16));
            return salt;
        }
    }

    public static String getShaPassword(String password,String priKey) {
        String hashPassword = "";
        if (password.trim().length() > 64){
            try {
                String shaPwd = new String(SM2Utils.decrypt(priKey,password));
                hashPassword = shaPwd.substring(16);
                return hashPassword;
            } catch (IOException e) {
                logger.error(e,e.getCause());
            } finally {
                return hashPassword;
            }
        } else {
            hashPassword = password.substring(16);
            return hashPassword;
        }
    }


    /**
     * 获取在线人数判断license时调用
     */
    public static Integer getOnlineCountForLicense(String ip) {
        List<String> ipList = new ArrayList<String>();
        Collection<Session> sessions = getSessionDAO().getActiveSessions();
        if(sessions == null){
            return 0;
        } else {
            int sessionCount = 0;
            for(Session session : sessions){
                PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if(principalCollection != null){
                    Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
                    if(!isIpExist(principal.getIp(), ipList)){
                        ipList.add(principal.getIp());
                        sessionCount ++ ;
                    }
                }
            }
            return sessionCount;
        }
    }

    public static boolean isIpExist(String ip, List<String> list){
        for(String str : list){
            if(str.equals(ip)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取在线人数过滤机构
     */
    public static Integer getOnlineCount() {
    	
    	List<Principal> list = new ArrayList<Principal>();
		
		String casStart = GlobalContext.getProperty("cas.start");
		if(casStart==null || !"true".equals(casStart)){

			Collection<Session> sessions = getSessionDAO().getActiveSessions();
			if(sessions == null){
				return 0;
			} else {
				User user = getUser();
				int sessionCount = 0;
				for(Session session : sessions){
					PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
					if(principalCollection != null){
						Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
						if(user!=null&&user.getOrgId().equals(principal.getOrgId())){
							if(!isExist(principal, list)){
								list.add(principal);
								sessionCount ++ ;
							}
						}
					}
				}
				return sessionCount;
			}
		}else{
	        User user = getUser();
	        int sessionCount = 0;
	        HashMapBackedSessionMappingStorage sessionStorage = (HashMapBackedSessionMappingStorage) SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
	        Map<String, HttpSession> sessionMap =sessionStorage.getManagedSessions();
	        for (Map.Entry<String, HttpSession> entry : sessionMap.entrySet()) {
	            //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	            PrincipalCollection principalCollection = (PrincipalCollection) entry.getValue().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
	            if(user!=null && principalCollection != null){
	                Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
	                if(user.getIsSystemAdmin() || user.getIsSystemManager()){
	                    if(!isExist(principal, list)){
	                        list.add(principal);
	                        sessionCount ++ ;
	                    }
	                }else {
	                    if(user!=null && user.getOrgId().equals(principal.getOrgId())){
	                        if(!isExist(principal, list)){
	                            list.add(principal);
	                            sessionCount ++ ;
	                        }
	                    }
	                }
	            }
	        }
	        return sessionCount;
		}
    }

    public static boolean isExist(Principal principal, List<Principal> list){
        for(Principal p : list){
            if(p.getLoginName().equals(principal.getLoginName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取在线用户--人员控件显示用--返回userBean（机构隔离）
     * @return
     */
    public static List<UserBean> getOnlineUserBean(){
        Collection<Session> sessions = getSessionDAO().getActiveSessions();
        List<UserBean> userBeanList = new ArrayList<>();
        User user = SystemSecurityUtils.getUser();
        for(Session session : sessions){
            PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if(principalCollection != null){
                Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
                if(user != null && user.getOrgId().equals(principal.getOrgId())){
                    UserBean uBean = new UserBean();
                    uBean.setDisplayName(principal.getDisplayName());
                    userBeanList.add(uBean);
                }
            }
        }
        removeDuplicateWithOrder(userBeanList);
        return userBeanList;
    }

    /**
     * 去除集合中的重复数据
     * @param list
     */
    private static void removeDuplicateWithOrder(List<UserBean> list) {
        Set<String> set = new HashSet<String>();
        List<UserBean> newList = new ArrayList<UserBean>();
        for (Iterator<UserBean> iter = list.iterator(); iter.hasNext();) {
            UserBean element = iter.next();
            if (set.add(element.getId())){
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    public static List<Session> getSessionByUsername(String username){
        Collection<Session> sessions = getSessionDAO().getActiveSessions();
        List<Session> sessionList = new ArrayList<Session>();
        for(Session session : sessions){
            PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if(principalCollection != null){
                Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
                if(username.equals(principal.getLoginName())){
                    sessionList.add(session);
                }
            }
        }
        return sessionList;
    }

    public static void kickOutUser(String username){
        List<Session> sessionList = getSessionByUsername(username);
        for (Session session : sessionList) {
            session.setAttribute("kickout", true);
        }
    }

    public static void logout4M(Serializable sessionId){
        Collection<Session> sessions = getSessionDAO().getActiveSessions();
        for (Session session : sessions) {
            if (session.getId() == sessionId) {
                session.stop();
                break;
            }
        }
    }

    /**
     * 获取在线用户登录名
     */
    public static List<Principal> getOnlineUsers(){
        List<Principal> principalList = new ArrayList<>();
        String casStart = GlobalContext.getProperty("cas.start");
		if (casStart == null || !"true".equals(casStart)) {
			Collection<Session> sessions = getSessionDAO().getActiveSessions();
			User user = getUser();
			for(Session session : sessions){
				PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(principalCollection != null){
					Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
					if(user!=null&&user.getOrgId().equals(principal.getOrgId())){
						principalList.add(principal);
					}
				}
			}
			return principalList;
		} else {
	        User user = getUser();
	        HashMapBackedSessionMappingStorage sessionStorage = (HashMapBackedSessionMappingStorage) SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
	        Map<String, HttpSession> sessionMap =sessionStorage.getManagedSessions();
	        for (Map.Entry<String, HttpSession> entry : sessionMap.entrySet()) {
	            PrincipalCollection principalCollection = (PrincipalCollection) entry.getValue().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
	            if (principalCollection != null && user!=null) {
	                Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
	                if (user.getIsSystemAdmin() || user.getIsSystemManager()) {
	                    if (!isExist(principal, principalList)) {
	                        principalList.add(principal);
	                    }
	                } else {
	                    if (user != null && user.getOrgId().equals(principal.getOrgId())) {
	                        if(!isExist(principal, principalList)){
	                            principalList.add(principal);
	                        }
	                    }
	                }
	            }
	        }
	
	        return principalList;
		}
    }
    
    /**
     * 获取在线用户登录名
     */
    public static List<Principal> getOnlineUsers(String orgId){
    	List<Principal> principalList = new ArrayList<Principal>();
    	String casStart = GlobalContext.getProperty("cas.start");
		if(casStart==null || !"true".equals(casStart)){
    		Collection<Session> sessions = getSessionDAO().getActiveSessions();
			User user = getUser();
			for(Session session : sessions){
				PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(user!=null&&  principalCollection != null){
					Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
					if(user.getIsSystemAdmin() || user.getIsSystemManager()){
	                    principalList.add(principal);
	                }else {
	                    if (orgId != null && !"".equals(orgId)){
	                        if(orgId.equals(principal.getOrgId())){
	                            principalList.add(principal);
	                        }
	                    } else {
	                        if(user!=null&&user.getOrgId().equals(principal.getOrgId())){
	                            principalList.add(principal);
	                        }
	                    }
	                }
				}
			}
			return principalList;
    	}else{
	        User user = getUser();
	        HashMapBackedSessionMappingStorage sessionStorage = (HashMapBackedSessionMappingStorage) SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
	        Map<String, HttpSession> sessionMap =sessionStorage.getManagedSessions();
	        for (Map.Entry<String, HttpSession> entry : sessionMap.entrySet()) {
	            PrincipalCollection principalCollection = (PrincipalCollection) entry.getValue().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
	            if(user!=null&& principalCollection != null){
	                Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
	                if(user.getIsSystemAdmin() || user.getIsSystemManager()){
	                    principalList.add(principal);
	                }else {
	                    if (orgId != null && !"".equals(orgId)){
	                        if(orgId.equals(principal.getOrgId())){
	                            principalList.add(principal);
	                        }
	                    } else {
	                        if(user!=null && user.getOrgId().equals(principal.getOrgId())){
	                            principalList.add(principal);
	                        }
	                    }
	                }
	            }
	        }
	        return principalList;
    	}
    }

    /**
     * 判断用户是否是登录状态
     */
    public static Map<String, Object> loginState(String ids){
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> onLineList = new ArrayList<String>();
        List<String> noOnLineList = new ArrayList<String>();

        if(StringUtils.isNotEmpty(ids)){
            String[] id = ids.split(",");
            List<Principal> principalList =  SystemSecurityUtils.getOnlineUsers("");
            for(String idStr : id){
                boolean state = true;
                for(Principal principal : principalList){
                    if(principal.getId().equals(idStr)){
                        state = false;
                        onLineList.add(principal.getDisplayName());
                        break;
                    }
                }
                if(state){
                    noOnLineList.add(idStr);
                }
            }
        }

        StringBuffer onLineSb = new StringBuffer();
        for(int i=0;i<onLineList.size();i++){
            onLineSb.append(onLineList.get(i));
            if(i != onLineList.size()-1) {
                onLineSb.append(",");
            }
        }
        result.put("onLine", onLineSb.toString());

        StringBuffer noOnlineSb = new StringBuffer();
        for(int i=0;i<noOnLineList.size();i++){
            noOnlineSb.append(noOnLineList.get(i));
            if(i != noOnLineList.size()-1) {
                noOnlineSb.append(",");
            }
        }
        result.put("noOnLine", noOnlineSb.toString());
        return result;
    }

    /**
     * 判断登录IP
     */
    public static boolean determineIp(String userId, String host){
        try {
            UserIp userIp = new UserIp();
            userIp.setUserId(userId);
            List<UserIp> ipList = userIpService.queryAll(userIp);
            boolean state = false;
            if(ipList != null && ipList.size() > 0){
                for(UserIp ui : ipList){
                    if(ui.getBindType() == 1){
                        if(host.trim().equals(ui.getUserStartIp().trim())){
                            state = true;
                            break;
                        }
                    } else {
                        if(Utils.ipExistsInRange(host, ui.getUserStartIp().trim() + "-" + ui.getUserEndIp())){
                            state = true;
                            break;
                        }
                    }
                }
            } else {
                state = true;
            }
            return state;

        } catch (CustomException e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * 登录密码错误次数
     */
    public static int passwordError(String userId,String userIp){
        int count = 0;
        try {
            User user = new User();
            user.setId(userId);
            user = userService.get(user);
            if(user != null){
                Date lockDate = user.getLockStartDate() == null ? new Date() : user.getLockStartDate();
                count = user.getWrongCount() == null ? 0 : user.getWrongCount();

                User u = new User();
                u.setId(userId);
                u.setLockStartDate(DateUtils.getSysDate());
                if(DateUtils.subtractionDays(DateUtils.getSysDate(), lockDate) == 0){
                    if(SettingUtils.getSetting("maxErrorCount")==null) {
                        return 0;
                    }
                    if(count >= Integer.parseInt(SettingUtils.getSetting("maxErrorCount").toString()) -1 ){
                        u.setLockType(0);
                        u.setStatus(GlobalContext.USER_STATUS_2);
                        u.setWrongCount(0);
                        userService.update2(u);
                        //记录日志
                        recordOperlog(user,count,userIp);
                        return count;
                    } else{
                        u.setWrongCount(count+1);
                        userService.update2(u);
                    }
                } else {
                    u.setWrongCount(1);
                    userService.update2(u);
                }
            }

        } catch (CustomException e) {
            logger.error(e);
        }
        return count;
    }

    /**
     * 记录登录时间
     */
    public static void logInMes(){
        User user = SystemSecurityUtils.getUser();
        if(user != null){
            User u = new User();
            u.setId(user.getId());
            u.setStatus(GlobalContext.USER_STATUS_0);
            u.setWrongCount(0);
            u.setLastLonginDate(DateUtils.getSysDate());
            userService.update2(u);
        }
    }

    /**
     * 记录日志
     */
    public static void recordOperlog(User user,int count,String userIp){
        Operlog operLog=new Operlog();
        operLog.setCreateDate(DateUtils.getSysDate());
        operLog.setUserId(user.getId());
        operLog.setUniqueId(user.getCode());
        operLog.setLoginName(user.getLoginName());
        operLog.setDisplayName(user.getDisplayName());
        operLog.setIp(userIp);
        if(count == 0){
            operLog.setFunName("CheckForUserDisabled");
            operLog.setOperDesc("系统定时解锁登录");
            operLog.setOperType("解锁");
            operLog.setOperResultShow("系统解锁登录账号");
            operLog.setExtStr2("系统解锁账号操作");
        } else {
            operLog.setFunName("passwordError");
            operLog.setOperDesc("登录密码连续错误");
            operLog.setOperType("锁定");
            operLog.setOperResultShow("登录异常锁定");
            operLog.setExtStr2("登录锁定操作");
        }
        operLog.setModifyDate(DateUtils.getSysDate());
        operLog.setIsAdmin(String.valueOf(user.getIsAdmin()));
        operLog.setCreateUser(user.getId());
        operLog.setCreateUserDept(user.getDeptId());
        String subSystemId = GlobalContext.getProperty("subsystem.id");
        if (subSystemId != null && !"".equals(subSystemId)){
            operLog.setExtStr4(subSystemId);
        }
        try {
            operlogService.save(operLog);
        } catch (CustomException e) {
            logger.error(e,e);
        }
    }

    /**
     * 获取session
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 删除session中menu列表
     */
    public static void removeSessionAttribute(final String name) {
        SystemSecurityUtils.getSession().removeAttribute(name);
    }

    /**
     * 登出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getCacheMap().get(key);
        return obj==null?defaultValue:obj;
    }

    public static void putCache(String key, Object value) {
        getCacheMap().put(key, value);
    }

    public static void removeCache(String key) {
        getCacheMap().remove(key);
    }

    public static Map<String, Object> getCacheMap(){
        Map<String, Object> map = Maps.newHashMap();
        try{
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal)subject.getPrincipal();
            return principal!=null?principal.getCacheMap():map;
        }catch (UnavailableSecurityManagerException e) {
            return map;
        }
    }

    /**
     * 根据用户id集合拼装人员控件数据
     * @param ids
     * @return
     */
    public static List<Map<String, String>> getUsersByUserIdsToSelectControl(String ids){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if(ids != null && ids.length() > 0){
            String[] idArray = ids.split("\\,");
            for(String id : idArray){
            	User u = (User) JsonUtil.json2Java(CacheClient.getCache(CACHE_USER_INFO + id),User.class);
                /*User u = (User) CacheUtils.get(CACHE_USER_INFO + id);*/
                if(u == null){
                    u = userService.getUser(id);
                    if(u != null) {
                        CacheClient.putCache(CACHE_USER_INFO + u.getId(), JsonUtil.java2Json(u));
                    }
                }
                if(u != null) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", u.getId().toString());
                    map.put("text", u.getDisplayName());
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**getDeptByDeptIdsToSelectControl
     * 根据组织id集合拼装组织控件数据
     * @param ids
     * @return
     */
    public static List<Map<String, String>> getDeptByDeptIdsToSelectControl(String ids){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if(ids != null && ids.length() > 0){
            String[] idArray = ids.split("\\,");
            for(String id : idArray){
                Object obj = CacheUtils.get(CACHE_DEPT_INFO + id);
                Department d = (Department)obj;
                if(d != null) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", d.getId().toString());
                    map.put("text", d.getName());
                    list.add(map);
                }
            }
        }
        return list;
    }
    
    
    /**
	 * 获取菜单列表
	 */
	public static List<Menu> getMenuList() {
		List<Menu> menuList = null;
		User user = getUser();
		if (user != null) {
			menuList = systemDao.queryMenu(String.valueOf(user.getId()));
			return menuList;
		}
		return null;
	}

    public static String getRandomCharAndNumr(Integer length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) {
                str += (char) (65 + random.nextInt(26));
            } else {
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }
}
