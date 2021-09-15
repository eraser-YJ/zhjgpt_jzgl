package com.jc.archive.web;

import com.jc.archive.domain.Doc;
import com.jc.archive.service.IDocService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.PropertiesUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.FtpUploader;
import com.jc.system.common.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @title  嘉诚智能政务办公平台
 * @description  归档转文书档案controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 曹杨
 * @version 2016-03-15
 */
@Controller
@RequestMapping(value = "/archive/doc")
public class DocController extends BaseController {
//	@Autowired
//	private ISendService sendService;
//	@Autowired
//	private IReceiveService receiveService;
	@Autowired
	private IDocService docService;
	
	private static Map<String, String> attachPath = PropertiesUtil.getProperties("/goa.properties");
	private static Map<String, String> jddpPath = PropertiesUtil.getProperties("/goa.properties");
	private static Map<String, String> localForderPath = PropertiesUtil.getProperties("/ws_ftp.properties");
	private static Map<String, String> wsdlmap = PropertiesUtil.getProperties("/ws_wsdl.properties");
	public DocController(){
	}
	
	/**
     * @description 生成xml并同时将正文、表单的pdf文件上传至ftp
     * @param Long id
     * @param String tableName
     * @param HttpServletRequest request
     * @return boolean
     * @throws Exception
     * @author 曹杨
     * @version 2016-03-15
     */
	@RequestMapping(value = "checkBuildXmlAndUploadFtp.action")
	@ResponseBody
	@ActionLog(operateModelNm="归档转文书档案",operateFuncNm="get",operateDescribe="上传FTP")
	public boolean checkBuildXmlAndUploadFtp(Long id, String tableName,HttpServletRequest request) throws Exception {
		
		String formFileName = (String) request.getSession().getAttribute("formFileName"); //表单文件名
		String contentFileName = (String) request.getSession().getAttribute("contentFileName"); //正文名称
		//附件存储名称
		String attachName = "";
		//附件路径
		String root = request.getRealPath(attachPath.get("FILE_PATH") + File.separator);
		//服务器文书档案保存路径
		String serverPath = request.getRealPath(jddpPath.get("JDDP_PATH") + File.separator);
		//本地文件夹
		String localForder = localForderPath.get("pdfForder");
		Calendar calendar = Calendar.getInstance();
		//上传ftp文件名称（使用时间戳方式）
		String folder = String.valueOf(calendar.getTimeInMillis());
		//文书档案保存至ftp文件路径
		String docPath = jddpPath.get("JDDP_PATH") + File.separator + folder + File.separator;
		//xml文件名称
		String xmlName = "archive.xml";
		//附件文件名称
		String fileName = "";
		Doc doc = new Doc();
		boolean flag = true;
		boolean uploadStatus = false;
		if("toa_doc_receive".equals(tableName)){
//			Receive r = new Receive();
//			r.setId(id);
			//查询收文数据
//			Receive receive = receiveService.get(r);
			if(formFileName != null && !"".equals(formFileName)){
				doc.setFormFileName(formFileName);
			}
			if(contentFileName != null && !"".equals(contentFileName)){
				doc.setContentFileName(contentFileName);
				}
//			if (receive.getCreateUserName() != null && !"".equals(receive.getCreateUserName())) {
//				doc.setFilingPeopleName(receive.getCreateUserName());
//			}
//			if(formFileName != null && !"".equals(formFileName)){
//				doc.setFormFileName(formFileName);
//			}
//			doc.setProof(receive.getSeqValue());
//			doc.setTitle(receive.getTitle());
//			doc.setType(tableName);
//			doc.setWrittenDate(DateUtils.formatDate(receive.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
//			receive.setSaveDiskId("save to other document system");
//			receiveService.updateDiskId(receive);
		}
		else{
//			Send t = new Send();
//			t.setId(id);
//			//查询发文数据
//			Send send =  sendService.get(t);
//			
//			if(contentFileName != null && !"".equals(contentFileName)){
//			doc.setContentFileName(contentFileName);
//			}
//			if (send.getCreateUserName() != null && !"".equals(send.getCreateUserName())) {
//				doc.setFilingPeopleName(send.getCreateUserName());
//			}
//			if(formFileName != null && !"".equals(formFileName)){
//				doc.setFormFileName(formFileName);
//			}
//			doc.setKeyWord(send.getSubject());
//			doc.setProof(send.getNoValue());
//			doc.setTitle(send.getTitle());
//			doc.setType(tableName);
//			send.setSaveDiskId("save to other document system");
//			this.sendService.updateDiskId(send);
//			doc.setWrittenDate(DateUtils.formatDate(send.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		}
		// 生成xml格式数据
		String buildXml = XmlUtil.object2Xml(doc);
		if (buildXml != null && !"".equals(buildXml.trim())) {
			try {
				// 生成XML文件
				docService.strChangeXML(buildXml,request);
				log.info("根据生成的pdf文件生成xml文件");
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
				log.error("生成xml文件错误" + e.getMessage());
			}
		}
		if (flag) {
			// 根据业务id获取附件路径
			List<Attach> list = docService.getAttach(id, tableName);
			if (list != null && list.size() > 0) {
				for (Attach attach : list) {
					try {
						attachName = attach.getResourcesName();
						String name[] = attach.getResourcesName().split("/");
						//fileName = name[1];
						 fileName = attach.getFileName();
						// 附件上传
						File file = new File(root + File.separator	+ attach.getResourcesName());
						FileInputStream input = new FileInputStream(file);
						uploadStatus = FtpUploader.uploadFile(docPath,fileName, input);
						log.info("附件上传ftp，附件名 ：" + docPath + fileName);
					} catch (Exception e) {
						log.error("附件上传ftp失败：" + e.getMessage());
						e.printStackTrace();
						flag = false;
						return flag;
					}
				}
			}
			// 表单上传
			if (uploadStatus) {
				try {
					File formFile = new File(serverPath + File.separator	+ formFileName);
					FileInputStream formInput = new FileInputStream(formFile);
					uploadStatus = FtpUploader.uploadFile(docPath,formFileName, formInput);
					log.info("表单上传ftp，表单名：" + docPath + formFileName);
				} catch (Exception e) {
					log.error("表单上传ftp失败：" + e.getMessage());
					e.printStackTrace();
					flag = false;
					return flag;
				}
			}
			// 正文上传
			if (!"toa_doc_receive".equals(tableName)) {
				if (uploadStatus) {
					try {
						File contentFile = new File(serverPath + File.separator + contentFileName);
						FileInputStream contentInput = new FileInputStream(contentFile);
						uploadStatus = FtpUploader.uploadFile(docPath,contentFileName, contentInput);
						log.info("正文上传ftp，正文名：" + docPath + contentFileName);
					} catch (Exception e) {
						log.error("正文上传ftp失败：" + e.getMessage());
						e.printStackTrace();
						flag = false;
						return flag;
					}
				}
			}
			// xml文件上传
			if (uploadStatus) {
				try {
					File xmlFile = new File(serverPath + File.separator + xmlName);
					FileInputStream xmlInput = new FileInputStream(xmlFile);
					uploadStatus = FtpUploader.uploadFile(docPath, xmlName,xmlInput);
					log.info("xml文件上传ftp，xml名：" + docPath + xmlName);
				} catch (Exception e) {
					log.error("xml文件上传ftp失败：" + e.getMessage());
					e.printStackTrace();
					flag = false;
					return flag;
				}
			}
			if(uploadStatus){
				//调用webservice
//				JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
//		        svr.setServiceClass(ArchiveWebService.class);
//		        svr.setAddress(wsdlmap.get("jddp"));
//		        ArchiveWebService hw = (ArchiveWebService) svr.create();
//		        String result = hw.archive(folder);
//		        if(("success").equals(result))
//		        {
//		        	boolean isdelete = false;
//		        	//清空服务器文件
//		        	isdelete = docService.deleteAllFilesOfDir(new File(serverPath));
//		        	if(isdelete)
//		        	{
//		        		log.info("删除服务器文件成功");
//		        	}
//		        	//清空本地文件
//		        	isdelete = docService.deleteAllFilesOfDir(new File(localForder));
//		        	if(isdelete)
//		        	{
//		        		log.info("删除本地文件成功");
//		        	}
//		        }
//		        log.info("调用Webservice :" + result);
			flag = true;
			}
			else{
				flag = false;
			}
		}
		return flag;
	}

}
