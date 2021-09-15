package com.jc.system.common.service.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jc.system.common.service.IRegService;
import com.jc.system.common.util.FileUtil;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.common.util.Utils;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class RegServiceImpl implements IRegService {
	
	protected transient final Logger log = Logger.getLogger(this.getClass());

	@Override
	public String getRegStr(HttpServletRequest request){
		String ipStr = Utils.getRemoteIp();
		return getTemplate(ipStr);
	}

	private String getTemplate(String ipStr){
		StringBuffer str = new StringBuffer();
		str.append("Windows Registry Editor Version 5.00\n");
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows]\n");
		//弹出窗口阻止程序的注册表项 
		str.append("\"PopupMgr\"=\"no\"\n");
		//我的电脑选项
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\0]\n");
		//其他：显示混合内容
		str.append("\"1609\"=dword:00000000\n");
		//本地Internet
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\1]\n");
		//其他：显示混合内容
		str.append("\1609\"=dword:00000000\n");
		//可信站点
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\2]\n");
		//其他：显示混合内容
		str.append("\"1609\"=dword:00000000\n");
		//ActiveX 控件和插件：下载已签署的 ActiveX 控件 
		str.append("\"1001\"=dword:00000000\n");
		//ActiveX 控件和插件：下载未签署的 ActiveX 控件 
		str.append("\"1004\"=dword:00000000\n");
		//ActiveX 控件和插件：运行 ActiveX 控件和插件 
		str.append("\"1200\"=dword:00000000\n");
		//ActiveX 控件和插件：对标记为可安全执行脚本的 ActiveX 控件执行脚本 
		str.append("\"1405\"=dword:00000000\n");
		//ActiveX 控件和插件：对没有标记为可安全执行脚本的 ActiveX 控件进行初始化和脚本运行 
		str.append("\"1201\"=dword:00000000\n");
		//Internet 区域
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\3]\n");
		//其他：显示混合内容
		str.append("\"1609\"=dword:00000000\n");
		//受限制的站点区域
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\4]\n");
		//其他：显示混合内容
		str.append("\"1609\"=dword:00000000\n");
		//设置可信任站点（针对于ip）
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range1]\n");
		//协议：http
		str.append("\"http\"=dword:00000002\n");
		str.append("\":Range\"=\""+ipStr+"\"\n");
		//弹出窗口阻止程序允许站点
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows\\Allow]\n");
		str.append("\""+ipStr+"\"=hex:\n");
		//IE8下是否停止脚本运行延时
		str.append("[HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\Styles]\n");
		str.append("\"MaxScriptStatements\"=dword:FFFFFFFF\n");
		
		return str.toString();
	}

	@Override
    public void zipSetupFile(){
		//删除打包文件
		FileUtil.delAllFile(GlobalContext.basePath+"../../install/setup.zip");
		File file = new File(GlobalContext.basePath+"../../install/setup"+File.separator+"IE.reg");
		if(file.exists()){
			boolean flag = file.delete();
			if(flag){
				log.info("删除打包文件success");
			}
		}
		String ipStr = Utils.getRemoteIp();
		FileUtil.createFile(GlobalContext.basePath+"../../install/setup"+File.separator+"IE.reg", getTemplate(ipStr));
		//打包文件
		FileUtil.zip(GlobalContext.basePath+"../../install/setup");
	}
}
