package com.jc.archive.service.impl;

import com.jc.archive.ArchiveException;
import com.jc.archive.dao.IFilingDao;
import com.jc.archive.domain.Filing;
import com.jc.archive.service.IFilingService;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author
 * @version  2014-07-09
 */
@Service
public class FilingServiceImpl extends BaseServiceImpl<Filing> implements IFilingService{

	private IFilingDao filingDao;

	public FilingServiceImpl(){}

	@Autowired
	public FilingServiceImpl(IFilingDao filingDao){
		super(filingDao);
		this.filingDao = filingDao;
	}

	/**
	 * 下载附件
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException
	 */
	@Override
	public void downLoad(String id,String dmName, HttpServletRequest request,
						 HttpServletResponse response) throws ArchiveException {
		String path =  GlobalContext.getProperty("gxwj");
		Filing fil = new Filing();
		fil.setDocumentId(id);
		fil.setFileType(1);
		List<Filing> listFil = filingDao.getDownLoad(fil);
		int s = 0;
		if(listFil != null)
			s = listFil.size();
		File[] file1 = new File[s];
		if (listFil != null) {
			for (int i = 0; i < listFil.size(); i++) {
				Filing filing = listFil.get(i);
				if(filing.getFileType()==1){
					file1[i] = new File(path + filing.getFilePath().split("//")[0]+"/"+filing.getFormContent()+".pdf");

				}else{
					file1[i] = new File(path + filing.getFilePath());
				}

			}
		}
		// 生成的ZIP文件名为Demo.zip
		String reg = "\\\\|/|:|\\?|\\*|\\||<|>|\"|\'|,|\\.";
		dmName = dmName.replaceAll(reg,"");
		if(dmName.length() > 27) {
			dmName = dmName.substring(0,27);
		}
		String tmpFileName = dmName;
		byte[] buffer = new byte[1024];
		String strZipPath = path + tmpFileName;
		ZipOutputStream out = null;
		try {
			tmpFileName = URLEncoder.encode(tmpFileName,"UTF-8");
			tmpFileName = tmpFileName + ".zip";
			strZipPath = URLEncoder.encode(strZipPath,"UTF-8");
			strZipPath = strZipPath + ".zip";
			out = new ZipOutputStream(new FileOutputStream(tmpFileName));
			for (int i = 0; i < file1.length; i++) {
				FileInputStream fis = new FileInputStream(file1[i]);
				if(listFil.get(i).getFileName() != null && listFil.get(i).getFileName().indexOf(".") > -1) {
					out.putNextEntry(new ZipEntry(listFil.get(i).getFileName()));
				} else {
					out.putNextEntry(new ZipEntry(file1[i].getName()));
				}
				// 设置压缩文件内的字符编码，不然会变成乱码
				out.setEncoding("GBK");

				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
			}
			out.close();

			File file = new File(tmpFileName);
			if (file.exists()) {

				response.setCharacterEncoding("UTF-8");
				response.setContentType("multipart/form-data");
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + file.getName() + "");

				InputStream inputStream;
				inputStream = new FileInputStream(file);
				OutputStream os = response.getOutputStream();
				byte[] b = new byte[1024];
				int length;
				while ((length = inputStream.read(b)) > 0) {
					os.write(b, 0, length);
				}
				os.close();
				inputStream.close();
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ArchiveException ae = new ArchiveException();
			ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
			throw ae;
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				ArchiveException ae = new ArchiveException();
				ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
				throw ae;
			}
		}
	}

	/**
	 * 下载附件check
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException
	 */
	@Override
	public int downLoadCheck(String id, HttpServletRequest request) throws ArchiveException {
		String path = GlobalContext.getProperty("gxwj");
		Filing fil = new Filing();
		fil.setDocumentId(id);
		fil.setFileType(1);
		List<Filing> listFil = filingDao.getDownLoad(fil);
		int s = 0;
		if(listFil != null)
			s = listFil.size();
		File[] file1 = new File[s];
		String check = "";
		if (listFil != null && s > 0) {
			for (int i = 0; i < listFil.size(); i++) {
				Filing filing = listFil.get(i);
				file1[i] = new File(path + filing.getFilePath());
				if(!file1[i].exists()){
					check = "1";
					break;
				}
			}
			if("1".equals(check)) {
				///附件在服务器上不存在
				return 1;
			} else {
				return 0;
			}
		} else {
			//没有附件
			return 2;
		}
		/*if("1".equals(check) || listFil.size()<1){
			return false;
		} else {
			return true;
		}*/
	}

	/**
	 * 下载附件check
	 * @param Filing filing 实体类
	 * @author 闻瑜
	 * @version  2014-07-10
	 * @throws ArchiveException
	 */
	@Override
	public void getDeleteFiling(List<Filing> file) throws ArchiveException {
		filingDao.getDeleteFiling(file);
	}

	@Override
	public String filing2Pdf(Filing filing, HttpServletRequest request){
		String path = GlobalContext.getProperty("gxwj");
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyyMMdd");
		String da = format1.format(new Date());
		Date date = new Date();
		String filename = String.valueOf((date.getTime()));
		path ="C:";
		File file = new File(path);
		OutputStreamWriter  outSTr = null;
		BufferedWriter Buff = null;
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(path +"/"+ filename+".html");
		try {
			//outSTr = new FileWriter(file);
			outSTr = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			Buff = new BufferedWriter(outSTr);
			Buff.write(getHtml(filing.getFormContent()));
			Buff.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outSTr.close();
				Buff.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		exec(path +"/"+  filename+".html",path +"/"+  filename+".pdf");
		int all=0;
		boolean hasfile=true;
		while(hasfile){
			File file1 = new File(path +"/"+  filename+".pdf");
			hasfile = file1.exists();
			all++;
			if(all>6){
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return path  +"/"+ filename+".pdf";

	}

	private boolean exec(String srcPath, String destPath){
		StringBuilder cmd = new StringBuilder();
		cmd.append("wkhtmltopdf");
		cmd.append(" ");
		cmd.append("--page-size A4");// 参数
		cmd.append(" ");
		cmd.append(srcPath);
		cmd.append(" ");
		cmd.append(destPath);
		boolean result = true;
		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());


			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null) {
				System.out.println(line);
			}
			int exitVal = proc.waitFor();
			if (exitVal == 0) {
				System.out.println("执行完毕");
			} else {
				System.out.println("执行失败");
			}
            /*HtmlToPdfInter error = new HtmlToPdfInter(
                    proc.getErrorStream());
            HtmlToPdfInter output = new HtmlToPdfInter(
                    proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();*/
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	private String getHtml(String html){
		String head ="<html class=\"jcGOA js no-touch no-android chrome no-firefox no-iemobile no-ie no-ie10 no-ie11 no-ios\" id=\"content\">\n" +
				"<head>\n" +
				"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n" +
				"<meta name=\"renderer\" content=\"ie-stand\">\n" +
				"<link href=\""+ GlobalContext.getProperty("cas.localUrl") +"/css/font-face.css\" rel=\"stylesheet\" type=\"text/css\"> <!--<![endif]-->\n" +
				"<link href=\""+ GlobalContext.getProperty("cas.localUrl") +"/css/standard/standard/blue/jcap.css?v=7b89ce1005\" rel=\"stylesheet\" type=\"text/css\">\n" +
				"<link href=\""+ GlobalContext.getProperty("cas.localUrl") +"/css/jctree/jctree.min.css?v=1561561\" rel=\"stylesheet\" type=\"text/css\">\n" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\""+ GlobalContext.getProperty("cas.localUrl") +"/goa/css/webupload/webuploader.css\">\n" +
				"<style>\n" +
				".memu{width:100%;border:1px solid #fb6b5b;}\n" +
				".memu.b-btm-1{border-bottom:1px solid #fb6b5b;}\n" +
				".memu th,.memu td{padding:8px;line-height:20px;text-align:left;border-top:1px solid #fb6b5b}\n" +
				".memu th{font-weight:bold}\n" +
				".memu thead th{vertical-align:bottom}\n" +
				".memu caption+thead tr:first-child th,.memu caption+thead tr:first-child td,.memu colgroup+thead tr:first-child th,.memu colgroup+thead tr:first-child td,.memu thead:first-child tr:first-child th,.memu thead:first-child tr:first-child td{border-top:0}\n" +
				".memu tbody+tbody{border-top:1px solid #fb6b5b;}\n" +
				".memu .table{background-color:#fff}\n" +
				".memu th,.memu td{border-left:1px solid #fb6b5b}\n" +
				".memu caption+thead tr:first-child th,.memu caption+tbody tr:first-child th,.memu caption+tbody tr:first-child td,.memu colgroup+thead tr:first-child th,.memu colgroup+tbody tr:first-child th,.memu colgroup+tbody tr:first-child td,.memu thead:first-child tr:first-child th,.memu tbody:first-child tr:first-child th,.memu tbody:first-child tr:first-child td{border-top:0}\n" +
				".panel .memu thead>tr>th,.panel .memu>tbody>tr>td{height:45px;font-weight: bold;}\n" +
				".panel .memu>tbody>tr>td>ul>li>a{font-weight:normal;}\n" +
				"   .panel-heading,.modal-heading{font-size:18px;margin:0;line-height:18px;border:0 none;padding:24px 20px 15px;}\n" +
				"</style>\n" +
				"</head>\n" +
				"<body ondragstart=\"return false\" class=\"modal-open\">\n" +
				"<section class=\"panel m-t-md clearfix\" style=\"margin-bottom: 20px;\">";
		String endhtml = "</section>\n" +
				"</body>" +
				"</html>";

		return head+html+endhtml;

	}

	public Integer updateformContent(Filing o) throws DBException{

		return filingDao.updateformContent(o);

	}
}