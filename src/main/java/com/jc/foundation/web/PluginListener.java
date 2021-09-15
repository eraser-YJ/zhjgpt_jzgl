package com.jc.foundation.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.PluginUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.service.ISubsystemService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
public class PluginListener extends BasePluginListener {

    @Override
    public void regPlugin() {
        Plugin basePlugin = PluginUtil.deSerializeFromXml(this.getClass().getClassLoader().getResourceAsStream("plugin-base.xml"));
        regSubsystem();
        regDb(basePlugin);
        pluginContext.addFirstPlugin(basePlugin);
    }

    private void regSubsystem(){
        if (GlobalContext.FALSE.equals(GlobalContext.getProperty("subsystem.init"))) {
            return;
        }
        ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
        Subsystem subsystem = new Subsystem();
        subsystem.setPermission(GlobalContext.getProperty("subsystem.id"));
        try {
            List<Subsystem> list = subsystemService.queryAll(subsystem);
            if(list.isEmpty()){
                subsystem.setName(GlobalContext.getProperty("title"));
                subsystem.setUrl(GlobalContext.getProperty("cas.localUrl"));
                subsystem.setQueue(99);
                subsystem.setIsShow(0);
                subsystem.setOpenType("2");
                subsystem.setUserSyncUrl("/api/user/sync");
                InetAddress addr = InetAddress.getLocalHost();
                String localIp=addr.getHostAddress();
                subsystem.setExtStr1(localIp);
                subsystemService.save(subsystem);
            }
        } catch (CustomException e) {
            logger.error(e);
        } catch (UnknownHostException e1) {
        	logger.error(e1);
        }
    }
}
