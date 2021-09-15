package com.jc.archive.web;

import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Archive;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Filing;
import com.jc.archive.domain.Folder;
import com.jc.archive.facade.IArchiveFacadeService;
import com.jc.archive.service.IArchiveFolderService;
import com.jc.archive.service.IAudithisService;
import com.jc.archive.service.IDocumentService;
import com.jc.archive.service.IFilingService;
import com.jc.common.kit.vo.ResVO;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
import com.jc.system.common.util.Constants;
import com.jc.system.security.SystemSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @title 嘉诚智能政务办公平台
 * @description  提供归档等外部服务controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2016-06-21
 */

@Controller
@RequestMapping(value="/api/archive")
public class ArchiveApiController extends BaseController{

	@Autowired
	private IArchiveFolderService folderService;
	@Autowired
	private IAudithisService audithisService;
	@Autowired
	private IFilingService filingService;
	@Autowired
	private IDocumentService docService;
	@Autowired
	private IArchiveFacadeService archiveFacadeService;

	@RequestMapping(value="saveFolder.action")
	@ResponseBody
	public  ResVO saveFolder(@RequestBody Map<String,Object> data, HttpServletRequest request){

		String objUrl = (String) data.get("objUrl");
		Map<String, Object> infoMap = (Map<String, Object>) data.get("info");


		Folder checkFolder = new Folder();
		checkFolder.setFolderName((String) infoMap.get("projectName"));
		checkFolder.setFolderType(infoMap.get("folderType")+"");
		checkFolder.setDeleteFlag(0);
		checkFolder.setExtStr1(infoMap.get("projectNumber")+"");
		checkFolder.setExtStr2("-1");

		try {
			if (folderService.get(checkFolder) != null) {
				folderService.saveFolder(infoMap);

			}

		} catch (CustomException e) {
			return ResVO.buildFail("校验重名出现错误！"+e.getMessage());
		}


		return null;
	}

	@RequestMapping(value="uploadDocsApi.action")
	@ResponseBody
	public  ResVO uploadDocsApi(@RequestBody Map<String,Object> data, HttpServletRequest request){

		String fileType = (String) data.get("fileType");//文档类型0-资料管理，5-文档管理
		String fileids = (String) data.get("fileids");//文件ID用，号隔开
		String code = (String) data.get("code");//文件夹编码
//		String businessId = (String) data.get("businessId");//业务ID
		String projectNum = (String) data.get("projectNum");//项目编码


		Folder folder = new Folder();
		folder.setFileids(fileids);
		if(!folderService.checkAttachName(folder)) {
			return ResVO.buildFail(MessageUtils.getMessage("JC_OA_ARCHIVE_039"));
		}
		try {
			folder.setExtStr1(projectNum);
			folder.setExtStr2(code);
			List<Folder> folderList = folderService.queryAll(folder);
			if (folderList!=null&&folderList.size()>0){
				folder = folderList.get(0);
			}else{
				folder.setExtStr1(projectNum);
				folder.setExtStr2(code);
				folderList = folderService.queryAll(folder);
				if (folderList!=null&&folderList.size()>0){
					folder.setExtStr1(projectNum);
					folder.setExtStr2("-1");
				}else{
					return ResVO.buildFail("文件夹未找到:"+projectNum+" code:"+code);
				}

			}
			List<Document> documents = folderService.uploadDocs(folder, request);
		} catch (ArchiveException e) {
			e.printStackTrace();
			return ResVO.buildFail("上传文件错误!"+e.getMessage());
		} catch (CustomException e) {
			return ResVO.buildFail(e.getMessage());
		}


		return null;
	}



	/**
	 * @description 保存归档
	 * @param String jsonparams,request
	 * @return JsonResult
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-19
	 */
	@RequestMapping(value="saveFilingInfo.action")
	@ResponseBody
	public JsonResult saveFilingInfo(String jsonparams,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//将参数转化成map形式
		Map<String, String> params = (Map<String, String>) JsonUtil.json2Java(jsonparams, Map.class);
		//判断参数是否为空
		if(params == null) {
			return new JsonResult(-1, "jsonparams is empty");
		}
		//参数
		String folderId = params.get("folderId");
		String createUser = params.get("userId");
		String createUserName = params.get("userName");
		String createUserDept = params.get("createUserDeptId");
		String createUserDeptName = params.get("createUserDeptName");
		String orgId = params.get("createUserOrgId");
		String formContent = params.get("form");
		String docId =params.get("docId");
		String tableName =params.get("tableName");
		String title =params.get("title");
		String viewFileId =params.get("viewFileId");

		//读取传递进来的文件流
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<Filing> filingList = new ArrayList<Filing>();
		// 创建文件夹
		String tempAbsolutePath = this.getAbsolutePath();
		String absPath = GlobalContext.getProperty("gxwj") + tempAbsolutePath;//绝对路径
		File file = new File(absPath);
		if (!file.exists()||!file.isDirectory()) {
			file.mkdirs();
		}
		//循环读取文件流
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			Filing filing = new Filing();
			String key = entity.getKey();
			MultipartFile mf = entity.getValue();
			//String fileName = mf.getOriginalFilename();//文件名
			long fileSize = mf.getSize();
			BigDecimal size = new BigDecimal(fileSize);
			String fileName = key;
			//判断是否是正文文件
			if("aip".equals(key)) {
				//fileName = "公文正文.aip";//点聚
				fileName = "公文正文.pdf";//金格
				filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_BODY);
				filing.setFormContent(viewFileId);//正文在toa_doc_template_jg表中的id
			}else{
				filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_ATTACH);
			}
			//创建了一个空的文件
			File uploadFile = new File(absPath + "/" + fileName);
			try {
				//将文件流写入
				mf.transferTo(uploadFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return new JsonResult(-1, "filing fail");
			}

			// 创建正文记录
			filing.setFileName(fileName);
			//filing.setFilePath(this.getAbsolutePath() + File.separator + key);
			//goa-311  bug
			filing.setFilePath(tempAbsolutePath + File.separator + fileName);
			if(fileSize / 1024 < 1024) {
				filing.setFizeSize((size.divide(new BigDecimal(1024)).setScale(2, BigDecimal.ROUND_HALF_UP)) + "K");
			} else {
				filing.setFizeSize((size.divide(new BigDecimal(1024 * 1024)).setScale(2, BigDecimal.ROUND_HALF_UP)) + "M");
			}

			filingList.add(filing);
		}

		//将参数中数据转入到archive对象中
		Archive archive = new Archive();
		try {
			archive.setFormContent(formContent);//表单
			archive.setFolderId(folderId);//文件夹ID
			archive.setDocId(docId);
			archive.setArchiveName(title);//公文标题
			archive.setTableName(tableName);//公文表名
			archive.setCreateUserName(createUserName);
			archive.setCreateUser(createUser);
			archive.setCreateUserDeptName(createUserDeptName);
			archive.setCreateUserDept(createUserDept);
			archive.setCreateUserOrg(orgId);
			archive.setTableName(tableName);//公文表名
			archive.setFileList(filingList);
			resultMap = archiveFacadeService.archiveFiling(archive,request);
			if("false".equals(resultMap.get(GlobalContext.RESULT_SUCCESS).toString())) {
				return new JsonResult(-1, "fail",resultMap);
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put("archive", archive);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_021"));
			e.printStackTrace();
		}

		return new JsonResult(0, "Success",resultMap);
	}
	/**
	 * @description 加载归档目录结构
	 * @param jsonparams
	 * @return Map<String, Object>
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-19
	 */
	@RequestMapping(value="loadFilingInfo.action")
	@ResponseBody
	public JsonResult loadFilingInfo(String jsonparams) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, String> params = (Map<String, String>) JsonUtil.json2Java(jsonparams, Map.class);

		if(params == null) {
			return new JsonResult(-1, "jsonparams is empty");
		}
		try {
			Folder folder = new Folder();
			folder.setDeleteFlag(Constants.ARC_DM_IN_RECYCLE_NO);
			folder.setFolderType(Constants.ARC_FOLDER_TYPE_FILE_DOC);
			folder.addOrderByFieldDesc("t.Create_Date");
			folder.setCreateUserOrg(params.get("orgId"));
			List<Folder> folders = folderService.queryAll(folder);
			List<Folder> resultFolder = removeDeletedFolder(folders);
			resultMap.put("folders", resultFolder);
			//resultMap.put("send", data);

			//Map<String, String> wsftp = PropertiesUtil.getProperties("/ws_ftp.properties");
			//String pdfForder = wsftp.get("pdfForder");
			//resultMap.put("htmlFileDir", pdfForder);

			/*Attach attach = new Attach();
			attach.setBusinessId(send.getId());
			attach.setBusinessTable("TOA_DOC_SEND");
			attach.setIsPaged("1");
		//	attach.setCategory(category);
			PageManager page = new PageManager();
			page.setPageRows(999999);
			PageManager page_ = attachService.query(attach, page);
			List<Attach> attachList = (List<Attach>) page_.getData();*/
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_OA_ARCHIVE_022"));
			e.printStackTrace();
		}
		return new JsonResult(0, "Success",resultMap);
	}
	/**
	 * @description 移除父节点被删除的节点
	 * @param List<Folder> folders
	 * @return List<Folder>
	 * @author 盖旭
	 * @version 2014-06-19
	 */
	private List<Folder> removeDeletedFolder(List<Folder> folders) {
		List<Folder> resultFolder = new ArrayList<Folder>();

		//移除父节点被删除的节点
		for(int i = 0; i < folders.size(); i++) {
			Folder f = folders.get(i);
			if("0".equals(f.getParentFolderId())) {
				resultFolder.add(f);
			} else {
				if(checkUseableNode(folders,f)) {
					resultFolder.add(f);
				} else {
					//i--;
					// folders.remove(i);
				}
			}
		}
		return resultFolder;
	}
	/**
	 * @description 查找当前节点的父节点是否存在
	 * @param List<Folder> list
	 * @param Folder folder
	 * @return boolean
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-19
	 */
	private boolean checkUseableNode(List<Folder> list, Folder folder) {
		for(Folder f : list) {
			if("0".equals(folder.getParentFolderId())) {
				return true;
			} else {
				Folder parentNode = getParentNode(list, folder.getParentFolderId());
				if(parentNode != null) {
					return checkUseableNode(list, parentNode);
				} else {
					return false;
				}
			}
		}
		return false;
	}
	private Folder getParentNode(List<Folder> list, String id) {
		for(Folder f : list) {
			if(id.equals(f.getId())) {
				return f;
			}
		}
		return null;
	}
	public String getUploadBaseDir() {
		return String.valueOf(GlobalContext.getProperty("FILE_PATH"));
	}
	public String getAbsolutePath(){
		return (getUploadBaseDir() + File.separatorChar +"upload"+ File.separator +"archive"
				+File.separatorChar +getContextPath()).replace('\\','/');
	}

	public String getContextPath(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(new Date());
		return dateStr;
	}
	@RequestMapping(value="hearbeat.action")
	@ResponseBody
	public JsonResult heartbeat(){
		return new JsonResult(0,"true");
	}
}