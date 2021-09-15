package com.jc.foundation.web;

import com.jc.foundation.util.PluginUtil;
import com.jc.system.plugin.domain.Plugin;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
public class WebDefaultPluginListener extends BasePluginListener {
    @Override
    public void regPlugin() {
        Plugin defaultPlugin = PluginUtil.deSerializeFromXml(this.getClass().getClassLoader().getResourceAsStream("plugin-default.xml"));
        if (defaultPlugin != null) {
        	defaultPlugin.setIsDbReady(1);
            pluginContext.addFirstPlugin(defaultPlugin);
        }
    }
}
