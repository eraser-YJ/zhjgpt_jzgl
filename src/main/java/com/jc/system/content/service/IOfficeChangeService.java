package com.jc.system.content.service;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IOfficeChangeService {

	/**
	 * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice
	 * @param inputFilePath
	 * @param officeType
	 */
	void office2html(String inputFilePath,String officeType);

	/**
	 * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice
	 * @param inputFilePath
	 * @param officeType
	 */
	void office2Pdf(String inputFilePath,String officeType);
	
}
