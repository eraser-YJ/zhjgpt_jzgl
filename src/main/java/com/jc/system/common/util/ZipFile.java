package com.jc.system.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import javax.servlet.ServletOutputStream;

import com.jc.foundation.util.GlobalContext;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class ZipFile {

	private static Logger log = Logger.getLogger(ZipFile.class);

	/**
	 * 压缩单个文件
	 * @param file 要压缩的文件名
	 * @param zipfile 生成的zip文件
	 * @return 执行状态
	 */
	public boolean zip(String file, String zipfile) {
		boolean result = false;
		File srcFile = new File(file);
		if (srcFile.exists()) {
			ZipOutputStream out = null;
			FileInputStream in = null;
			try {
				out = new ZipOutputStream(new FileOutputStream(
						zipfile));
				out.setEncoding("GBK");
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(srcFile
						.getName()));
				in = new FileInputStream(file);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				result = true;
			} catch (Exception e) {
				log.error("压缩文件:" + srcFile.getName() + "异常", e);
			} finally {
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
				if(in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		return result;
	}

	/**
	 * 压缩多个文件
	 * @param files 要压缩的文件名数组
	 * @param zipfile 生成的zip文件
	 * @return 执行状态
	 */
	public boolean zip(List<String> files, String zipfile) {
		boolean result = true;
		if (files != null && files.size() > 0) {
			ZipOutputStream out = null;
			try {
				out = new ZipOutputStream(new FileOutputStream(
						zipfile));
				out.setEncoding("GBK");
				for (int i = 0; i < files.size(); i++) {
					String file = files.get(i);
					File srcFile = new File(file);
					pushZipList(out,srcFile);
				}
			} catch (Exception e) {
				result = false;
				log.error("压缩文件:" + zipfile + "异常", e);
			}finally {
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 2019-04-17 新增 为满足代码质量bug检查
	 * @param out
	 * @param srcFile
	 */
	private static void pushZipList(ZipOutputStream out,File srcFile) {
		FileInputStream in = null;
		try {
			byte[] buffer = new byte[Integer.parseInt(GlobalContext
					.getProperty("STREAM_SLICE"))];
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(srcFile
					.getName()));
			in = new FileInputStream(srcFile);
			int nNumber;
			while ((nNumber = in.read(buffer)) != -1) {
				out.write(buffer, 0, nNumber);
			}
		}catch(Exception e) {
			log.error(e.getMessage());
		}finally {
			try {
				if(in!=null) {
					in.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * @description 压缩多个文件
	 * @param files <文件下载相对路径,文件下载名称> 要压缩的文件名数组
	 * @param out 输出流
	 * @return 执行状态
	 */
	public static boolean zip(Map<String, String> files, OutputStream out,
			String rootDirectory) {
		boolean result = true;
		if (files != null && files.size() > 0) {
			try {
				ZipOutputStream zipout = new ZipOutputStream(out);
				Set<String> key = files.keySet();
				Iterator<String> iter = key.iterator();
				while (iter.hasNext()) {
					String filepath = iter.next();
					File srcFile = new File(rootDirectory + filepath);
					pushZipList(zipout, srcFile);
				}
				zipout.close();
				out.close();
			} catch (Exception e) {
				result = false;
				log.error(e.getMessage());
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * @description 压缩多个文件
	 * @param files 要压缩的文件名数组
	 * @param out 生成的zip文件
	 * @return 执行状态
	 */
	public boolean zip(List<String> files, ServletOutputStream out) {
		boolean result = true;
		if (files != null && files.size() > 0) {
			try {

				ZipOutputStream zipout = new ZipOutputStream(out);
				for (int i = 0; i < files.size(); i++) {
					String file = files.get(i);
					File srcFile = new File(file);
					pushZipList(zipout, srcFile);
				}
				zipout.close();
				out.close();
			} catch (Exception e) {
				result = false;
				log.debug(e.getMessage());
			}
		} else {
			result = false;
		}
		return result;
	}

	
	private static String unzipNewForFile(org.apache.tools.zip.ZipFile zip,ZipEntry entry,String dest) {
		String path = "";
		byte[] buffer = new byte[Integer.parseInt(GlobalContext
				.getProperty("STREAM_SLICE"))];
		int length = -1;
		InputStream input = null;
		BufferedOutputStream bos = null;
		File file = null;
		if ("index.html".equals(entry
				.toString()
				.substring(entry.toString().lastIndexOf('/') + 1,
						entry.toString().length()))) {
			path = entry.toString();
		}
		if (entry.isDirectory()) {
			file = new File(dest, entry.getName());
			if (!file.exists()) {
				file.mkdir();
			}
			return "";
		}
		try {
			input = zip.getInputStream(entry);
			file = new File(dest, entry.getName());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			bos = new BufferedOutputStream(new FileOutputStream(file));

			while (true) {
				length = input.read(buffer);
				if (length == -1) {
					break;
				}
				bos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if(bos!=null) {
					bos.close();
				}
				if(input!=null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return path;
	}
	
	/**
	 * @description 解压文件
	 * @param zipFile 要解压的.zip文件
	 * @param dest 文件解压后的路径
	 * @return 解压的目录
	 */
	public static String unzipNew(String zipFile, String dest) {
		org.apache.tools.zip.ZipFile zip = null;
		String path = "error";
		try {
			zip = new org.apache.tools.zip.ZipFile(
					zipFile, "GBK");
			Enumeration<ZipEntry> en = zip.getEntries();
			ZipEntry entry = null;
			while (en.hasMoreElements()) {
				entry = (ZipEntry) en.nextElement();
				String indexpath = unzipNewForFile(zip,entry,dest);
				if(!indexpath.equals("")) {
					path = indexpath;
				}
			}
		}catch (IOException e){
			log.error(e.getMessage());
		} finally {
			try {
				if(zip != null){
					zip.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return path;
	}

	/**
	 * @description 解压文件
	 * @param unzipfile 要解压的.zip文件
	 * @param unpath 文件解压后的路径
	 */
	public void unzip(String unzipfile, String unpath) {
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
					unzipfile);
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry;
			createDirectory(unpath, "");
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(unpath + File.separator + name);
					f.mkdir();
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace("\\", "/");
					if (fileName.indexOf('/') != -1) {
						createDirectory(unpath, fileName.substring(0,
								fileName.lastIndexOf('/')));
					}
					File f = new File(unpath + zipEntry.getName());
					boolean createNewFile = f.createNewFile();
					if(createNewFile) {
						unzipForFile(zipFile,zipEntry,f);
					}
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	private static void unzipForFile(org.apache.tools.zip.ZipFile zipFile,ZipEntry zipEntry,File f) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = zipFile.getInputStream(zipEntry);
			out = new FileOutputStream(f);
			byte[] by = new byte[Integer.parseInt(GlobalContext
					.getProperty("STREAM_SLICE"))];
			int c;
			while ((c = in.read(by)) != -1) {
				out.write(by, 0, c);
			}
		}catch(Exception e){
			log.error(e);
		}finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * @description 创建目录
	 * @param directory 创建的目录
	 * @param subDirectory 创建的子目录
	 */
	private static void createDirectory(String directory, String subDirectory) {
        String[] dir;
		File fl = new File(directory);
		try {
			if ((subDirectory==null||"".equals(subDirectory)) && !fl.exists()) {
                fl.mkdir();
            } else if (subDirectory!=null&&!"".equals(subDirectory)) {
				dir = subDirectory.replace("\\", "/").split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (!subFile.exists()) {
                        subFile.mkdir();
                    }
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			log.error(ex);
		}
	}

	/**
	 * @description 从文件服务器上下载文件
	 * @param openPath http连接
	 * @param downloadpath 下载目录
	 * @param zipName 压缩的文件名
	 * @return 压缩的文件名
	 */
	public String download(String openPath, String downloadpath, String zipName) {
		String newZipindex = "";
		HttpURLConnection hc = null;
		log.debug("-打开一个http连接---" + openPath);
		InputStream instream = null;
		// 创建这个文件输出流
		String tempFileName;
		tempFileName = downloadpath + zipName + ".zip";
		log.debug("需要下载的文件名称" + tempFileName);
		FileOutputStream fos = null;
		try {
			URL url = new URL(openPath);
			hc = (HttpURLConnection) url.openConnection();
			instream = hc.getInputStream();
			fos = new FileOutputStream(tempFileName);
			// 定义一个大小为STREAM_SLICE的字节数组
			byte[] buf = new byte[Integer.parseInt(GlobalContext
					.getProperty("STREAM_SLICE"))];
			// 从输入流中读出字节到定义的字节数组
			int len = instream.read(buf, 0, Integer.parseInt(GlobalContext
					.getProperty("STREAM_SLICE")));
			// 循环读入字节，然后写到文件输出流中
			while (len != -1) {
				fos.write(buf, 0, len);
				len = instream.read(buf, 0, Integer.parseInt(GlobalContext
						.getProperty("STREAM_SLICE")));
			}
			newZipindex = unzipNew(tempFileName, downloadpath
					+ zipName);// 解压
		} catch (IOException e) {
			newZipindex = "ioerror";
			log.error("要解压的.zip文件: " + tempFileName + ",解压的输出路径:"
					+ downloadpath + zipName + ";出现异常" + e);
		} catch (Exception e1) {
			newZipindex = "ioerror";
			log.error("要解压的.zip文件: " + tempFileName + ",解压的输出路径:"
					+ downloadpath + zipName + ";出现异常" + e1);

		}finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		return newZipindex;
	}
}
