package com.jc.archive.web;

import com.jc.foundation.util.PluginUtil;
import com.jc.foundation.web.BasePluginListener;
import com.jc.system.plugin.domain.Plugin;

/**
 * Created by Scott on 2016/2/3.
 */
public class ArchivePluginListener extends BasePluginListener {
    @Override
    protected void regPlugin() {
        Plugin plugin = PluginUtil.deSerializeFromXml(this.getClass().getClassLoader().getResourceAsStream("plugin-archive.xml"));
        this.pluginContext.addPlugin(plugin);
    }
}
