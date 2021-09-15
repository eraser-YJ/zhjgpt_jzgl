package com.jc.foundation.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 废弃
 */
public class GlobalContextBak {

    private static final Logger logger = Logger.getLogger(GlobalContext.class);
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    public static final Integer ROWS_DEFAULT = Integer.valueOf(10);
    public static final String SESSION_MENU_LIST = "menuList";
    public static final String SESSION_TOKEN = "token";
    public static final String PASSWORD_DEFAULT_VALUE = "12345678";
    public static final String USER_SOURCE_OA = "1";
    public static final String RESULT_SUCCESS = "success";
    public static final String RESULT_ERRORMESSAGE = "errorMessage";
    public static final String RESULT_SUCCESSMESSAGE = "successMessage";
    public static final String RESULT_LABELERRORMESSAGE = "labelErrorMessage";
    public static final String ADMIN_NAME = "admin";
    public static final String USER_STATUS_0 = "status_0";
    public static final String USER_STATUS_1 = "status_1";
    public static final String USER_STATUS_2 = "status_2";
    public static final String USER_STATUS_3 = "status_3";
    public static final String basePath = GlobalContext.class.getClassLoader().getResource("/").getPath();
    public static final String SYS_CENTER = "center";
    public static final String SYS_SUBSYSTEM_FIELD = "subsystem.id";
    public static final String SUBSYSTEM_EXTEND_PROPERTIES_FILE = "subsystem.extend.properties.file";
    private static PropertiesLoader loader = null;

    public static Long COMMON_LOG_TIME;
    public static Integer COMMON_LOG_NUM;

    public static Integer USER_ACTIVE;
    public static Integer USER_DSIRUPTOR;

    public static String MANAGER_NAME = "manager";
    public static String SECURITY_NAME = "security";
    public static String AUDIT_NAME = "audit";


    public GlobalContextBak() {
    }

    public static String getProperty(String property) {
        return !SYS_SUBSYSTEM_FIELD.equals(property) || loader.getProperty(property) != null && !"".equals(loader.getProperty(property)) ? loader.getProperty(property) : SYS_CENTER;
    }

    public static boolean isSysCenter() {
        String var0 = loader.getProperty(SYS_SUBSYSTEM_FIELD);
        return StringUtil.trimIsEmpty(var0) || SYS_CENTER.equals(var0);
    }

    static {
        loader = new PropertiesLoader(new String[]{"jcap.properties", "log.properties"});
        String var0 = loader.getProperty(SUBSYSTEM_EXTEND_PROPERTIES_FILE);
        if (!StringUtils.isEmpty(var0)) {
            loader.addPropertiesFile(var0.split(","));
        }
        COMMON_LOG_TIME = loader.getLong("commonLogTime");
        COMMON_LOG_NUM = loader.getInteger("cmmmonLogNum");
        USER_ACTIVE = loader.getInteger("useActivemq");
        USER_DSIRUPTOR = loader.getInteger("useDisruptor");
    }
}
