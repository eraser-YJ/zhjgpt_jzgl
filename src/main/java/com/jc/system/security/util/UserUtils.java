package com.jc.system.security.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户缓存
 * @author Administrator
 * @date 2020-06-30
 */
public class UserUtils {

    private UserUtils() {
        throw new IllegalStateException("UserUtils class");
    }

    protected static transient final Logger logger = Logger.getLogger(UserUtils.class);

    private static IUserService userService = SpringContextHolder.getBean(UserServiceImpl.class);

    private static Map<String,Object> uniqueMap = new HashMap<String, Object>();

    public static Object getUnique(String key){
        return uniqueMap.get(key);
    }

    public static void setUnique(String key, Object value) {
        uniqueMap.put(key,value);
    }

    public static void clearUnique(){
        uniqueMap.clear();
    }

    private static Map<String,Object> userSecretMap = new HashMap<String, Object>();

    public static Object getUserSecret(String key){
        return userSecretMap.get(key);
    }
    public static void setUserSecret(String key, Object value) {
        userSecretMap.put(key,value);
    }


    private static Map<String,Object> userDicMap = new HashMap<String, Object>();

    public static Object getUserDic(String key){
        return userDicMap.get(key);
    }

    public static void setUserDic(String key, Object value) {
        userDicMap.put(key,value);
    }

    public static void clearUserDic(){
        userDicMap.clear();
    }

    /**
     * 根据userId获得user对象
     * @param id
     * @return
     */
    public static User getUser(String id) {
        User user = new User();
        if(id != null){
            user.setId(id);
            user.setDeleteFlag(null);
            return userService.getUserById(user);
        }
        return null;
    }

    public static User getNewUser(String id){
        User user = new User();
        if(id != null){
            user.setId(id);
            user.setDeleteFlag(null);
            return userService.getUser(id);
        }
        return null;
    }

    public static User getNewUserForCode(String code){
        User user = new User();
        if(code != null){
            user.setCode(code);
            user.setDeleteFlag(null);
            try {
                return userService.getUser(user);
            } catch (CustomException e) {
                logger.error(e);
            }
        }
        return null;
    }

    public static User getUserWithLogic(String id) {
        User user = new User();
        if(id != null){
            user.setId(id);
            user.setDeleteFlag(null);
            return userService.getWithLogic(user);
        }
        return null;
    }

    public static List<User> getUsers(){
        User user = new User();
        return userService.queryUserIdAndName(user);
    }

}
