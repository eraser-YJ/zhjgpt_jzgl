package com.jc.system.util;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.service.IClientServiceExt;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class InitClazzUtils {
    protected transient final Logger logger = Logger.getLogger(this.getClass());
    private IClientServiceExt userServiceExt;

    private static Map<String,String> clazzMap = new HashMap<String, String>();

    /**
     * 注入格式要求 模块名-方法名 例如：user-delete 表示注入到用户表的delete方法中
     * @param key 例如 user-delete
     * @param clazzUrl 例如 com.jc.system.util.**Impl.class
     */
    public static void setClazzMap(String key,String clazzUrl){
        clazzMap.put(key,clazzUrl);
    }

    public static Class<?> getClazzMap(String key) throws ClassNotFoundException {
        return clazzMap.get(key)!=null?Class.forName(clazzMap.get(key)):null;
    }

    public static String getClazzStr(String key) {
        return clazzMap.get(key) == null ? null : clazzMap.get(key);
    }

    public IClientServiceExt getUserServiceExt(String key) {
        if (userServiceExt == null) {
            try {
                userServiceExt = (IClientServiceExt) SpringContextHolder.getBean(getClazzMap(key));
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        return userServiceExt;
    }
}
