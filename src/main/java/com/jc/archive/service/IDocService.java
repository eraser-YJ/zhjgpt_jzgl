package com.jc.archive.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.domain.Attach;

public interface IDocService  {
	
	/**
	 * 将XML文件保存服务器
	 * @param str
	 * @throws IOException
	 */
	public void strChangeXML(String str, HttpServletRequest request) throws IOException;

	/**
	 * 根据id和表名获取附件
	 * @param id
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public List<Attach> getAttach(Long id, String table) throws Exception;
    
    /**
     * 删除目录下的文件
     * @param path
     */
    public  boolean deleteAllFilesOfDir(File path);
}
