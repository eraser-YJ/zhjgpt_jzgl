package com.jc.sys.util;

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
public class SubUserUtil {

    private SubUserUtil() {
        throw new IllegalStateException("SubUserUtil class");
    }

    private static Map<String,Object> subUserDeptMap = new HashMap<String, Object>();

    public static Object getSubUserDept(String key){
        return subUserDeptMap.get(key);
    }

    public static void setSubUserDept(String key, Object value) {
        subUserDeptMap.put(key,value);
    }
}
