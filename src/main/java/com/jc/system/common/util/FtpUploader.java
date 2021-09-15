package com.jc.system.common.util;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.PropertiesLoader;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class FtpUploader {
	private FtpUploader() {
		throw new IllegalStateException("FtpUploader class");
	}
	static Logger logger = Logger.getLogger(FtpUploader.class);
	private static PropertiesLoader loader  = new PropertiesLoader("jcap.properties");

	/**
	 * @description 连接ftp服务器
	 * @return 服务器客户端
	 * @throws IOException
	 */
	public static FTPClient getFtpClient() throws IOException {
		FTPClient ftp = new FTPClient();
		int reply;
		// 连接FTP服务器
		ftp.connect(loader.getProperty("ftpHost"), loader.getInteger("ftpPort"));
		// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.login(loader.getProperty("ftpUser"), loader.getProperty("ftpPwd"));
		ftp.setBufferSize(10000);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			logger.warn("ftp 连接异常 ftp 地址:" + loader.getProperty("ftpHost"));
			return null;
		}
		return ftp;
	}

	/**
	 * @description 向FTP服务器上传文件
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = null;
		try {
			ftp = getFtpClient();
			if (ftp == null) {
                return false;
            }
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			String fileName = filename;
			if (!"".equals(path)) {
				makeDir(ftp, path);
				fileName = path + File.separator + filename;
			}
			ftp.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			logger.error("FTP上传文件异常" + e, e);
		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("FTP连接关闭异常" + ioe, ioe);
				}
			}
		}
		return success;
	}
	
	private static void makeDir(FTPClient ftp, String remote) {
		String[] paths = remote.split("/");
		
		for (String path : paths) {
			if (!"".equals(path)) {
				try {
					ftp.makeDirectory(path);
					ftp.changeWorkingDirectory(path);
				} catch (IOException e) {
					logger.error(e.getMessage());
					break;
				}
			}
		}
	}

	/**
	 * 删除fpt服务器上指定的文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		boolean success = false;
		FTPClient ftp = null;
		try {
			ftp = getFtpClient();
			if (ftp == null) {
                return false;
            }
			success = ftp.deleteFile(path);
		} catch (SocketException e) {
			logger.error("FTP删除文件异常,文件路径为" + path + ";" + e, e);
		} catch (IOException e) {
			logger.error("FTP删除文件异常,文件路径为" + path + ";" + e, e);
		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("FTP连接关闭异常" + ioe, ioe);
				}
			}
		}
		return success;
	}

	/**
	 * 从FTP服务器多文件下载
	 * @param remotePath [] FTP服务器上的相对路径集合
	 * @param response 请求回答
	 * @return 执行状态
	 */
	public static boolean downFile(Map<String, String> remotePath, HttpServletResponse response) {
		boolean success = false;
		FTPClient ftp = null;
		try {
			ftp = getFtpClient();
			if (ftp == null) {
                return false;
            }
			ZipOutputStream zipout = new ZipOutputStream(response.getOutputStream());
			Set<String> key = remotePath.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				String filepath = iter.next();
				zipout.putNextEntry(new org.apache.tools.zip.ZipEntry(remotePath.get(filepath)));
				InputStream in = ftp.retrieveFileStream(filepath);
				byte[] buffer = new byte[Integer.parseInt(GlobalContext.getProperty("STREAM_SLICE"))];
				// 向压缩文件中输出数据
				int nNumber;
				while ((nNumber = in.read(buffer)) != -1) {
					zipout.write(buffer, 0, nNumber);
				}
				ftp.completePendingCommand();
				in.close();
			}
			zipout.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			logger.error("FTP删除文件下载异常,文件路径为" + remotePath + ";" + e, e);
		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("FTP连接关闭异常" + ioe, ioe);
				}
			}
		}
		return success;
	}

	/**
	 * 从FTP服务器单文件下载
	 * @param remotePath FTP服务器上的相对路径
	 * @param response 请求回答
	 * @return 执行状态
	 */
	public static boolean downFile(String remotePath, HttpServletResponse response) {
		boolean success = false;
		FTPClient ftp = null;
		try {
			ftp = getFtpClient();
			if (ftp == null) {
                return false;
            }
			ftp.retrieveFile(remotePath, response.getOutputStream());
			ftp.logout();
			success = true;
		} catch (IOException e) {
			logger.error("FTP删除文件下载异常,文件路径为" + remotePath + ";" + e, e);
		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("FTP连接关闭异常" + ioe, ioe);
				}
			}
		}
		return success;
	}

	/**
	 * 从FTP服务器单文件下载
	 * @param remotePath FTP服务器上的相对路径
	 * @return 执行状态
	 */
	public static InputStream downFile(String remotePath) {
		InputStream inputStream = null;
		FTPClient ftp = null;
		try {
			ftp = getFtpClient();
			if (ftp != null) {
				inputStream = ftp.retrieveFileStream(remotePath);
				ftp.logout();
			}
		} catch (IOException e) {
			logger.error("FTP删除文件下载异常,文件路径为" + remotePath + ";" + e, e);
		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("FTP连接关闭异常" + ioe, ioe);
				}
			}
		}
		return inputStream;
	}
}
