package com.jc.foundation.util;

import com.jc.foundation.config.AppPropertyPlaceholderConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 全局变量
 * @author Administrator
 * @date 2020-06-30
 */
public  class GlobalContext {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(GlobalContext.class);
	public final static String FALSE = "false";
	public final static String TRUE = "true";
	public final static String SIGN_0 = "0";
	public final static String SIGN_1 = "1";
	public final static String SIGN_LESS_1 = "-1";
	public final static Integer NUM_SIGN_LESS_1 = -1;
	public final static Integer CUSTOM_SIGN_ERROR = -100;
	public final static String SUCCESS = "success";

	/**时间格式*/
	public static  String DATE_FORMAT = null;
	public static  String TIME_FORMAT = null;
	public static  String DATE_TIME_FORMAT = null;
	public static  String TIMESTAMP_FORMAT = null;
	/**页面分页信息 默认系那是页面数*/
	public static  Integer ROWS_DEFAULT = 10;
	/**session中存放menu的常量*/
	public static  String SESSION_MENU_LIST = null;
	/**session中存放token的常量*/
	public static  String SESSION_TOKEN = null;
	/**密码初始值*/
	public static  String PASSWORD_DEFAULT_VALUE = null;
	/**用户来源*/
	public static  String USER_SOURCE_OA = null;
	/**返回成功*/
	public static  String RESULT_SUCCESS = null;
	/**返回错误信息*/
	public static  String RESULT_ERRORMESSAGE = null;
	/**返回成功信息*/
	public static  String RESULT_SUCCESSMESSAGE = null;
	/**label类错误*/
	public static  String RESULT_LABELERRORMESSAGE = null;
	/**超级管理员登录名称*/
	public static  String ADMIN_NAME = null;
	/**系统管理员登录名称*/
	public static  String MANAGER_NAME = null;
	/**安全保密员登录名称*/
	public static  String SECURITY_NAME = null;
	/**安全审计员登录名称*/
	public static  String AUDIT_NAME = null;
	/**用户启用状态*/
	public static  String USER_STATUS_0 = null;
	/**用户禁用状态*/
	public static  String USER_STATUS_1 = null;
	/**用户锁定状态*/
	public static  String USER_STATUS_2 = null;
	/**用户删除状态*/
	public static  String USER_STATUS_3 = null;
	/**系统根文件目录*/
	public static  String basePath = getBasePath();
	/**管理中心系统标识*/
	public static  String SYS_CENTER = "center";
	/**子系统标识字段*/
	public static  String SYS_SUBSYSTEM_FIELD = "subsystem.id";
	public static  String SUBSYSTEM_EXTEND_PROPERTIES_FILE = "subsystem.extend.properties.file";
	private static PropertiesLoader loader  = null;

	/**
	 * properties文件中的系统变量
	 */
	static {
		loader = new PropertiesLoader("jcap.properties", "log.properties");
		String files = loader.getProperty(SUBSYSTEM_EXTEND_PROPERTIES_FILE);
		if(!StringUtils.isEmpty(files)){
			loader.addPropertiesFile(files.split(","));
		}
	}

	public static String getBasePath(){
		try {
			return GlobalContext.class.getClassLoader().getResource("/").getPath();
		} catch(Exception e) {
			return GlobalContext.class.getClass().getResource("/").getPath();
		}
	}

	/**
	 * 获取系统变量
	 * @param property 系统变量名
	 * @return
	 */
	public static String getProperty(String property) {
		if (System.getenv("ENABLED_CONFIG_CENTER") != null && "apollo".equals(System.getenv("ENABLED_CONFIG_CENTER"))){
			String reValue = AppPropertyPlaceholderConfigurer.getProperty(property);
			if (reValue == null && loader != null) {
				reValue = loader.getProperty(property);
			}
			return reValue;
		} else {
			if (SYS_SUBSYSTEM_FIELD.equals(property)) {
				if (loader.getProperty(property) == null || "".equals(loader.getProperty(property))) {
					return SYS_CENTER;
				}
			}
			return loader.getProperty(property);
		}
	}

	public static  Long COMMON_LOG_TIME = loader.getLong("commonLogTime");
	public static  Integer COMMON_LOG_NUM = loader.getInteger("cmmmonLogNum");
	public static  Integer USER_ACTIVE = loader.getInteger("useActivemq");
	public static  Integer USER_DSIRUPTOR = loader.getInteger("useDisruptor");

	/**
	 * 获取是否服务中心
	 * @return
	 */
	public static boolean isSysCenter() {
		String sys_id = getProperty(SYS_SUBSYSTEM_FIELD);
		if (!StringUtil.trimIsEmpty(sys_id) && SYS_CENTER.equals(sys_id)) {
			return true;
		}
		return false;
	}
}
