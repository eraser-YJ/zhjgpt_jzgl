package com.jc.archive.web;

import com.jc.archive.ArchiveException;
import com.jc.archive.domain.*;
import com.jc.archive.facade.IArchiveFacadeService;
import com.jc.archive.service.*;
import com.jc.archive.util.PermissionUtil;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.oa.click.ActionClick;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.Constants;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.util.UserUtils;
import com.jc.system.util.menuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * @title 嘉诚智能政务办公平台
 * @description  提供归档等外部服务controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2016-06-21
 */

@Controller
@RequestMapping(value="/archive/archive4rtx")
public class Archive4RtxController extends BaseController{

	@Autowired
	private IPermissionService permissionService;
	@Autowired
	private IDocumentService documentService;
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

	@Autowired
	IUserService userService;

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
		String absPath = GlobalContext.getProperty("gxwj") + tempAbsolutePath;
		// String absPath = request.getRealPath("/") + tempAbsolutePath;//绝对路径
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


	private void initPublicFolder(Long userId) {

	}
	@RequestMapping(value = "publicArchive.action")
	@ResponseBody
	public Map<String, Object> publicArchive(HttpServletRequest request) throws CustomException {
		Map<String,Object> map = new HashMap<String,Object>();
		// 公共文档根目录为Root目录
		Folder folder = new Folder();
		folder.setFolderType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
		folder.setCreateUser(null);
		String userId = request.getParameter("userId");
		if(StringUtil.isEmpty(userId)) {
			userId = "200001";
		}
		String folderId = request.getParameter("folderId");
		if(StringUtil.isEmpty(folderId)) {
			folder.setParentFolderId("0");
		} else {
			folder.setId(folderId);
		}
		List<Folder4Rtx> newSubFolder = new ArrayList<Folder4Rtx>();
		List<Document4Rtx> newSubDocument = new ArrayList<Document4Rtx>();
		try {
			User paramUser = new User();
			paramUser.setId(userId);
			User user = userService.get(paramUser);
			folder.setCreateUserOrg(user.getOrgId());
			folder = folderService.get(folder);
			// 若无公共文档根目录，创建之！
			if (folder == null) {
				folder = new Folder();
				folder.setFolderType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
				folder.setParentFolderId("0");
				folder.setKmAppFlag("0");
				folder.setFolderName("根目录");
				folder.setFolderPath("/根目录");
				folder.setCreateUserOrg(user.getCreateUserOrg());
				folder.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
				folderService.save(folder);
			}
			Folder folder1 = new Folder();
			Folder4Rtx folder4Rtx = new Folder4Rtx();
			folder1.setId(folder.getId());
			folder1.setFolderType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
			folder1.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
			folder1.setDeleteFlag(0);
			folder1.setCreateUserOrg(user.getCreateUserOrg());
			folder1 = folderService.getDirDocs4Rtx(folder,user.getId());
			folder4Rtx.setPermView(folder1.isPermView());
			folder4Rtx.setPermNewUpDown(folder1.isPermNewUpDown());
			folder4Rtx.setPermEdit(folder1.isPermEdit());
			folder4Rtx.setPermDelete(folder1.isPermDelete());
			folder4Rtx.setPermCopyPaste(folder1.isPermCopyPaste());
			folder4Rtx.setPermRename(folder1.isPermRename());
			folder4Rtx.setPermCollect(folder1.isPermCollect());
			folder4Rtx.setPermVersion(folder1.isPermVersion());
			folder4Rtx.setPermHistory(folder1.isPermHistory());
			folder4Rtx.setPermRelate(folder1.isPermRelate());
			String root =  "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();// +"?login-at=true";
			if(folder1 != null) {

				folder4Rtx.setId(folder1.getId());
				List<Folder> subDirs = folder1.getSubdirs();
				if(subDirs != null) {
					for(Folder f : subDirs) {
						Folder4Rtx folder4Rtx1 = new Folder4Rtx();
						folder4Rtx1.setId(f.getId());
						folder4Rtx1.setFolderName(f.getFolderName());
						folder4Rtx1.setParentFolderId(f.getParentFolderId());
						folder4Rtx1.setOwner(f.getOwner());
						folder4Rtx1.setModifyDate(f.getModifyDate());
						if(UserUtils.getUser(f.getCreateUser()) != null){
							folder4Rtx1.setDeptName(UserUtils.getUser(f.getCreateUser()).getDeptName());
						}

						newSubFolder.add(folder4Rtx1);
					}
				}
				folder4Rtx.setSubdirs(newSubFolder);
				List<Document> documentList = folder1.getDocuments();
				// 查询当前目录权限
				Permission permission = new Permission();
				permission.setFolderId(folder.getId());
				permission.setUserId(user.getId());
				permission.setDeptId(user.getDeptId());
				permission.setOrgId(user.getOrgId());
				List<Permission> list = permissionService
						.queryPermission(permission);
				Folder permFolder = PermissionUtil.permissionValue(list);

				boolean isView = permFolder.isPermView();

				if(documentList != null) {
					for(Document d : documentList) {

						Document4Rtx document4Rtx = new Document4Rtx();
						document4Rtx.setId(d.getId());
						document4Rtx.setDmName(d.getDmName());
						document4Rtx.setCreateUserName(d.getCreateUserName());
						document4Rtx.setCreateDate(d.getCreateDate());
						document4Rtx.setFolderId(d.getFolderId());
						document4Rtx.setDmSuffix(d.getDmSuffix());
						document4Rtx.setOwner(d.getOwner());
						document4Rtx.setDmSize(d.getDmSize());
						document4Rtx.setModifyDate(d.getModifyDate());
						if(UserUtils.getUser(d.getCreateUser()) != null){
							document4Rtx.setCreateUserDeptName(UserUtils.getUser(d.getCreateUser()).getDeptName());
						}

						String phy_path = d.getPhysicalPath();
						if(d.getCreateUser() == user.getId() || isView) {
							document4Rtx.setPermissionValue("1");
						} else {
							document4Rtx.setPermissionValue("0");
						}
						document4Rtx.setPhysicalPath(root+"/"+phy_path.replaceAll("\\\\","/")+"?login-at=true");
						newSubDocument.add(document4Rtx);
						//document4Rtx.set
					}

				}
				folder4Rtx.setDocuments(newSubDocument);

			}

			map.put("code","000000");
			map.put("body", folder4Rtx);
			map.put("errormsg", "");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code","200000");
			map.put("errormsg", "请重新加载数据。");
		}
		//String token = super.getToken(request);
		//model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return map;
	}

	@RequestMapping(value = "myArchive.action")
	@ResponseBody
	public Map<String, Object> myArchive(HttpServletRequest request) throws CustomException {
		Map<String,Object> map = new HashMap<String,Object>();
		// 公共文档根目录为Root目录
		String userId = request.getParameter("userId");
		if(StringUtil.isEmpty(userId)) {
			userId = "200001";
		}
		String folderId = request.getParameter("folderId");
		Folder folder = new Folder();
		folder.setFolderType(Constants.ARC_FOLDER_TYPE_MY_DOC);
		folder.setCreateUser(userId);
		if(StringUtil.isEmpty(folderId)) {
			folder.setParentFolderId("0");
		} else {
			folder.setId(folderId);
		}

		List<Folder4Rtx> newSubFolder = new ArrayList<Folder4Rtx>();
		List<Document4Rtx> newSubDocument = new ArrayList<Document4Rtx>();
		try {
			User paramUser = new User();
			paramUser.setId(userId);
			User user = userService.get(paramUser);
			folder = folderService.get(folder);
			// 若无公共文档根目录，创建之！
			if (folder == null) {
				folder = new Folder();
				folder.setFolderType(Constants.ARC_FOLDER_TYPE_MY_DOC);
				folder.setParentFolderId("0");
				folder.setKmAppFlag("0");
				folder.setFolderName("根目录");
				folder.setFolderPath("/根目录");
				folder.setCreateUserOrg(user.getCreateUserOrg());
				folder.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
				folderService.save(folder);
			}
			Folder folder1 = new Folder();
			Folder4Rtx folder4Rtx = new Folder4Rtx();
			folder1.setId(folder.getId());
			folder1.setFolderType(Constants.ARC_FOLDER_TYPE_MY_DOC);
			folder1.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
			folder1.setDeleteFlag(0);
			folder1 = folderService.getDirDocs4Rtx(folder,user.getId());
			String root =  "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();// +"?login-at=true";
			if(folder1 != null) {
				folder4Rtx.setId(folder1.getId());
				List<Folder> subDirs = folder1.getSubdirs();
				if(subDirs != null) {
					for(Folder f : subDirs) {
						Folder4Rtx folder4Rtx1 = new Folder4Rtx();
						folder4Rtx1.setId(f.getId());
						folder4Rtx1.setFolderName(f.getFolderName());
						folder4Rtx1.setParentFolderId(f.getParentFolderId());
						folder4Rtx1.setOwner(f.getOwner());
						folder4Rtx1.setModifyDate(f.getModifyDate());
						newSubFolder.add(folder4Rtx1);
					}
				}
				folder4Rtx.setSubdirs(newSubFolder);
				List<Document> documentList = folder1.getDocuments();
				if(documentList != null) {
					for(Document d : documentList) {
						Document4Rtx document4Rtx = new Document4Rtx();
						document4Rtx.setId(d.getId());
						document4Rtx.setDmName(d.getDmName());
						document4Rtx.setCreateUserName(d.getCreateUserName());
						document4Rtx.setCreateDate(d.getCreateDate());
						document4Rtx.setFolderId(d.getFolderId());
						document4Rtx.setDmSuffix(d.getDmSuffix());
						document4Rtx.setDmSize(d.getDmSize());
						document4Rtx.setOwner(d.getOwner());
						document4Rtx.setModifyDate(d.getModifyDate());
						String phy_path = d.getPhysicalPath();

						document4Rtx.setPhysicalPath(root+"/"+phy_path.replaceAll("\\\\","/")+"?login-at=true");
						newSubDocument.add(document4Rtx);
						//document4Rtx.set
					}

				}
				folder4Rtx.setDocuments(newSubDocument);

			}


			map.put("code","000000");
			map.put("body", folder4Rtx);
			map.put("errormsg", "");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code","200000");
			map.put("errormsg", "请重新加载数据。");
		}
		//String token = super.getToken(request);
		//model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return map;
	}

	@RequestMapping(value = "myCollection.action")
	@ResponseBody
//	@ActionLog(operateModelNm = "文档管理", operateFuncNm = "intoMyCollectionFolder", operateDescribe = "收藏夹信息初始化")
	public Map<String, Object> myCollection( HttpServletRequest request) throws Exception {

		Map<String,Object> map = new HashMap<String,Object>();
		Document ment = new Document();
		List<Document4Rtx> document4RtxList = new ArrayList<Document4Rtx>();
		try{

			String userId = request.getParameter("userId");
			// 默认排序
			ment.addOrderByFieldDesc("t.CREATE_DATE");
			ment.setCollectId(userId);
			ment.setDmInRecycle(0);
			ment.setFileType(Constants.ARC_DOC_FILETYPE_FAVOR);
			List<Document> list = documentService.queryAll(ment);
			String root =  "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();// +"?login-at=true";
			for (int i = 0; i < list.size(); i++) {
				Document d = list.get(i);
				Document4Rtx document4Rtx = new Document4Rtx();
				document4Rtx.setId(d.getId());
				document4Rtx.setDmName(d.getDmName());
				document4Rtx.setCreateUserName(d.getCreateUserName());
				document4Rtx.setCreateDate(d.getCreateDate());
				document4Rtx.setFolderId(d.getFolderId());
				document4Rtx.setDmSuffix(d.getDmSuffix());
				document4Rtx.setOwner(d.getOwner());
				document4Rtx.setDmSize(d.getDmSize());
				document4Rtx.setModifyDate(d.getModifyDate());
				String phy_path = d.getPhysicalPath();
				document4Rtx.setPhysicalPath(root+"/"+phy_path.replaceAll("\\\\","/")+"?login-at=true");
				//document4Rtx.setCollectName(UserUtils.getUser(d.getCreateUser()).getDisplayName());
				document4RtxList.add(document4Rtx);
			}
			map.put("code","000000");
			map.put("body", document4RtxList);
			map.put("errormsg", "");
		}catch (Exception e){
			e.printStackTrace();
			map.put("code","200000");
			map.put("errormsg", "请重新加载数据。");
		}
		return map;
	}
}