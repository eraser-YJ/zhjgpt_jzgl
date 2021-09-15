package com.jc.common.kit;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 临时文件目录服务
 * 
 * @author lc  liubq
 * @since 2017年12月22日
 */
public class DirectoryUtil {

	// 可下载文件目录
	private static File downloadDir = null;

	// 可上传文件目录
	private static File uploadDir = null;

	// 模型目录
	private static File cacheModelDir = null;

	// 模板
	private static File cacheTemplateDir = null;

	// 初始化SQL目录
	private static File cacheInitSqlDir = null;

	// 结束SQL目录
	private static File cacheEndSqlDir = null;

	// tomcat 目录
	private static File tomcatDir = null;

	// 工程目录
	private static File projectDir = null;

	// 初始化
	static {
		try {
			// 取得classes目录
			URL u = DirectoryUtil.class.getResource("/");
			File classFile = new File(u.toURI());
			// 去到tomcat目录
			tomcatDir = classFile.getParentFile().getParentFile().getParentFile().getParentFile();
			projectDir = classFile.getParentFile().getParentFile();
			downloadDir = new File(projectDir.getAbsolutePath() + "/download");
			downloadDir.mkdirs();
			uploadDir = new File(projectDir.getAbsolutePath() + "/upload");
			uploadDir.mkdirs();
			cacheModelDir = new File(projectDir.getAbsolutePath() + "/cache/model");
			cacheModelDir.mkdirs();
			cacheTemplateDir = new File(projectDir.getAbsolutePath() + "/cache/template");
			cacheTemplateDir.mkdirs();
			cacheInitSqlDir = new File(projectDir.getAbsolutePath() + "/cache/init_sql");
			cacheInitSqlDir.mkdirs();
			cacheEndSqlDir = new File(projectDir.getAbsolutePath() + "/cache/end_sql");
			cacheEndSqlDir.mkdirs();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Tomcat目录
	 * 
	 * @return 例如：D:/tomcat
	 */
	public static File getTomcatDir() {
		return tomcatDir;
	}

	/**
	 * 工程目录
	 * 
	 * @return 例如：D:/tomcat/webapps/con_assets
	 */
	public static File getProjectDir() {
		return projectDir;
	}

	/**
	 * 删除目录
	 * 
	 * @param dir 目录
	 */
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			for (File subFile : dir.listFiles()) {
				deleteDir(subFile);
			}
		}
		dir.delete();
	}

	/**
	 * 可下载文件目录
	 * 
	 * @return 可下载文件目录
	 */
	public static File getDownloadDir() {
		return downloadDir;
	}

	/**
	 * 可上载文件目录
	 * 
	 * @return 可上载文件目录
	 */
	public static File getUploadDir() {
		return uploadDir;
	}

	/**
	 * 模型目录
	 * 
	 * @return 模型目录
	 */
	public static File getCacheModelDir() {
		return cacheModelDir;
	}

	/**
	 * 模板目录
	 * 
	 * @return 模板目录
	 */
	public static File getCacheTemplateDir() {
		return cacheTemplateDir;
	}

	/**
	 * 初始化SQL目录
	 * 
	 * @return 初始化SQL目录
	 */
	public static File getCacheInitSqlDir() {
		return cacheInitSqlDir;
	}

	/**
	 * 结束SQL目录
	 * 
	 * @return 结束SQL目录
	 */
	public static File getCacheEndSqlDir() {
		return cacheEndSqlDir;
	}

}
