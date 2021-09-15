package com.jc.system.security.utils;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.service.ISettingService;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.service.impl.SettingServiceImpl;
import com.jc.system.security.service.impl.SystemServiceImpl;
import com.jc.system.util.SettingUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SettingInit {

    private SettingInit() {
        throw new IllegalStateException("SettingInit class");
    }

    private transient static final Logger log = Logger.getLogger(SettingInit.class);
    private static ISettingService settingService = null;
    private static ISystemService systemService = null;

    /**
     * 初始化函数
     */
    public static void init(){
        log.debug("初始化系统参数开始");
        settingService = SpringContextHolder.getBean(SettingServiceImpl.class);
        systemService = SpringContextHolder.getBean(SystemServiceImpl.class);
        refresh();
        log.debug("初始化系统参数结束");
    }

    /**
     * 刷新系统参数内存
     */
    public static void refresh(){
        try {
            List<Setting> list = settingService.queryAll(new Setting());
            List<Setting> list1 = systemService.queryServerSetting(new Setting());
            for(Setting setting:list){
                SettingUtils.setSetting(setting.getSettingKey(), setting.getSettingValue());
            }
            for(Setting setting:list1){
                SettingUtils.setSetting(setting.getSettingKey(), setting.getSettingValue());
            }
        } catch (CustomException e) {
            log.error(e);
        }
    }
}
