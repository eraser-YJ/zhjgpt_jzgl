package com.jc.csmp.common.tool;

import com.jc.foundation.util.GlobalContext;

/**
 * @author 常鹏
 * @version 1.0
 * @date 2020/10/14 10:58
 */
public class AuthUtil {
    public static String checkSuperAuth(String code) {
        String allRoleDeptCode = GlobalContext.getProperty("allRoleDeptCode");
        if (allRoleDeptCode == null || code == null) {
            return code;
        }
        if (allRoleDeptCode.indexOf("," + code + ",") > -1) {
            return "0000000200010001";
        }
        return code;
    }
}
