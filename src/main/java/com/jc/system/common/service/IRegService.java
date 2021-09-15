package com.jc.system.common.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IRegService {
	/**
	 * 返回注册表文件内容方法
	 * @param request
	 * @return
	 */
	String getRegStr(HttpServletRequest request);

	/**
	 * 压缩辅助安装程序
	 */
	void zipSetupFile();
}
