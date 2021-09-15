package com.jc.common.kit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 文件复制
 * 
 * @author lc  liubq
 * @since 2017年12月22日
 */
public class FileCopy {
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath) throws Exception {
		File oldFile = new File(oldPath);
		File newFile = new File(newPath);
		copyFile(oldFile, newFile);
	}

	/**
	 * 复制目录
	 * 
	 * @param oldDir 原文件目录
	 * @param newDir 新文件目录
	 * @param bakDir 备份目录
	 * @throws Exception
	 */
	public static void copyDir(File oldDir, File newDir, File bakDir) throws Exception {
		if (!oldDir.exists()) {
			return;
		}
		// 目录
		if (oldDir.isDirectory()) {
			if (!newDir.exists()) {
				newDir.mkdirs();
			}
			for (File subFile : oldDir.listFiles()) {
				File subOldDir = new File(oldDir + "/" + subFile.getName());
				File subNewDir = new File(newDir + "/" + subFile.getName());
				File subBakDir = bakDir == null ? null : new File(bakDir + "/" + subFile.getName());
				copyDir(subOldDir, subNewDir, subBakDir);
			}
		}
		// 文件
		else {
			// 备份文件
			if (bakDir != null && newDir.exists()) {
				bakDir.getParentFile().mkdirs();
				copyFile(newDir, bakDir);
			}
			// 覆盖文件
			copyFile(oldDir, newDir);
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldFile 原文件
	 * @param newFile 复制后文件
	 * @return boolean
	 * @throws Exception
	 */
	public static void copyFile(File oldFile, File newFile) throws Exception {
		InputStream inStream = null;
		FileOutputStream fs = null;
		if (!oldFile.exists()) {
			return;
		}
		try {
			if (newFile.exists()) {
				if (!newFile.delete()) {
					System.out.println(newFile.getAbsolutePath() + " 删除失败");
				}
			}
			int byteSum = 0;
			int byteRead = 0;
			// 读入原文件
			inStream = new FileInputStream(oldFile);
			fs = new FileOutputStream(newFile);
			byte[] buffer = new byte[4096];
			while ((byteRead = inStream.read(buffer)) != -1) {
				// 字节数 文件大小
				byteSum += byteRead;
				fs.write(buffer, 0, byteRead);
			}
			fs.flush();
			fs.close();
			System.out.println(newFile.getAbsolutePath() + " 复制完成 size=" + byteSum);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (fs != null) {
				fs.close();
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param fileExt
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static File createFile(String fileExt, InputStream inputStream) throws Exception {
		FileOutputStream fs = null;
		try {
			File tempFile = File.createTempFile("LIUBQ", "." + fileExt);
			int byteRead = 0;
			// 读入原文件
			fs = new FileOutputStream(tempFile);
			byte[] buffer = new byte[4096];
			while ((byteRead = inputStream.read(buffer)) != -1) {
				// 字节数 文件大小
				fs.write(buffer, 0, byteRead);
			}
			fs.flush();
			fs.close();
			return tempFile;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (fs != null) {
				fs.close();
			}
		}
	}

}
