package com.jc.system.tab.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import org.apache.log4j.Logger;

/**
 * 子系统标签
 * @author Administrator
 * @date 2020-06-30
 */
public class TabTree extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public TabTree() {
	}
	/**子系统标识*/
	private String sysPermission;
	/**系统页签树标题*/
	private String tabTitle;
	/**系统页签树链接*/
	private String tabUrl;
	/**系统页签树位置标记*/
	private String tabFlag;
	public String getSysPermission(){
		return sysPermission;
	}
	public void setSysPermission(String sysPermission){
		this.sysPermission = sysPermission;
	}
	public String getTabTitle(){
		return tabTitle;
	}
	public void setTabTitle(String tabTitle){
		this.tabTitle = tabTitle;
	}
	public String getTabUrl(){
		return tabUrl;
	}
	public void setTabUrl(String tabUrl){
		this.tabUrl = tabUrl;
	}
	public String getTabFlag(){
		return tabFlag;
	}
	public void setTabFlag(String tabFlag){
		this.tabFlag = tabFlag;
	}
}