package com.jc.system.sys.web;

import com.jc.foundation.util.PluginUtil;
import com.jc.foundation.web.BasePluginListener;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.sys.util.PinDeptAndUserInit;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class SpellPluginListener extends BasePluginListener {
    @Override
    protected void regPlugin() {
        Plugin plugin = PluginUtil.deSerializeFromXml(this.getClass().getClassLoader().getResourceAsStream("plugin-spell.xml"));
        this.pluginContext.addPlugin(plugin);
    }

    @Override
    protected void afterRegPlugin() {
        PinDeptAndUserInit.init();
    }
}
