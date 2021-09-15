package com.jc.archive.facade.impl;

import com.jc.archive.ArchiveException;
import com.jc.archive.domain.*;
import com.jc.archive.facade.IArchiveFacadeService;
import com.jc.archive.service.*;
import com.jc.archive.util.PermissionUtil;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.PropertiesUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.common.util.Constants;
import com.jc.system.common.util.FileUtil;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.util.SettingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.*;

@Service
public class ArchiveFacadeService implements IArchiveFacadeService {
	@Autowired
	private IDocumentService docService;
	@Autowired
	private IArchiveFolderService folderService;
	@Autowired
	private IAudithisService audithisService;
	@Autowired
	private IFilingService filingService;

	@Autowired
	private IAttachService attachService;

	@Autowired
	private IPermissionService permissionService;


//	@Autowired
//	private ISendService sendService;
//
//
//	@Autowired
//	private IReceiveService receiveService;

	@Autowired
	private IDepartmentService departmentService; // 部门服务

	protected transient final Logger log = Logger.getLogger(this.getClass());

	/**
	 * 检测父节点是否存在
	 * @param id
	 * @return
	 */
	private boolean checkFolder(String id) {
		log.info("选择的归档文件夹id："+id);
		Folder folder = new Folder();
		folder.setFolderType(Constants.ARC_FOLDER_TYPE_FILE_DOC);
		boolean ok = true;
		try {
			folder.setDeleteFlag(null);
			List<Folder> list = folderService.queryAll(folder);
			String parentFolderId = id;
			if(list == null) {
				ok = false;
			} else {
				while(true) {
					if("0".equals(parentFolderId) || !ok) {
						//ok = false;
						break;
					}
					for(Folder f : list) {
						if(f.getId().equals(parentFolderId)) {
							if(f.getDmInRecycle().intValue() == 1 || f.getDeleteFlag().intValue() == 1) {
								ok = false;
								break;
							}
							parentFolderId = f.getParentFolderId();
						}
					}
				}
			}
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return ok;

	}
	@Override
	public Map<String, Object> archiveFile(Archive archive,
										   HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 检查参数
		if (archive.getFolderId() == null
				|| StringUtil.isEmpty(archive.getArchiveName())
				|| StringUtil.isEmpty(archive.getPiId())) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_022"));
			return resultMap;
		}
		// 查询归档文件夹
		Folder folder = new Folder();
		folder.setId(archive.getFolderId());
		folder.setFolderType(Constants.ARC_FOLDER_TYPE_FILE_DOC);
		if(!checkFolder(archive.getFolderId())) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
			return resultMap;
		}
		// 验证当前人有没有权限访问选中的文件夹
		Permission permission = new Permission();
		permission.setFolderId(archive.getFolderId());
		permission.setUserId(SystemSecurityUtils.getUser().getId());
		permission.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		permission.setOrgId(SystemSecurityUtils.getUser().getOrgId());
		List<Permission> listPermission = null;
		try {
			listPermission = permissionService
					.queryPermission(permission);

			if(listPermission != null && listPermission.size()>0) {
				Folder folderPermission = PermissionUtil
						.permissionValue(listPermission);
				if(folderPermission == null || !folderPermission.isPermNewUpDown()) {
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
							MessageUtils.getMessage("JC_OA_ARCHIVE_027"));
					return resultMap;
				}
			}
			else{
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
						MessageUtils.getMessage("JC_OA_ARCHIVE_027"));
				return resultMap;
			}
		} catch (ArchiveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			folder.setDmInRecycle(0);
			folder = folderService.get(folder);
			if(folder == null) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
						MessageUtils.getMessage("JC_OA_ARCHIVE_023"));
				return resultMap;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_019"));
			return resultMap;
		}
		try {
			Attach attach = new Attach();
			attach.setBusinessId(archive.getPiId());
			attach.setBusinessTable(archive.getTableName());
			attach.setIsPaged("1");
			//	attach.setCategory(category);
			List<Attach> attachList = null;//(List<Attach>) page_.getData();
			if(archive.getAttachList() != null && archive.getAttachList().size() > 0) {
				attachList = archive.getAttachList();
			} else {
				PageManager page = new PageManager();
				page.setPageRows(999999);
				PageManager page_ = attachService.query(attach, page);
				attachList = (List<Attach>) page_.getData();
			}
			if(attachList != null) {
				Map<String,String> filingMap = new HashMap<String, String>();
				for(Attach a : attachList) {
					filingMap.put(a.getFileName(), SettingUtils.getSetting("filePath").toString() + File.separatorChar + a.getResourcesName());
				}
				archive.setFilingMap(filingMap);
			}

			// 检查同一归档对象在同一文件夹下只允许一个实例
			Document checkDoc = new Document();
			checkDoc.setPiId(archive.getPiId());
			checkDoc.setFolderId(folder.getId());
			checkDoc.setFileType(Constants.ARC_DOC_FILETYPE_FILING);
			checkDoc.setTableName(archive.getTableName());//归档表名
			checkDoc = docService.get(checkDoc);
			Document doc=null;
			if (checkDoc == null) {
				// 如果归档对象为空，是新建归档
				// 创建归档对象
				doc = new Document();
				doc.setFolderId(folder.getId());
				doc.setPiId(archive.getPiId());
				doc.setFileType(Constants.ARC_DOC_FILETYPE_FILING);
				doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_UNKNOWN);
				// doc.setDmLink(paramMap.get(Constants.ARC_FACADE_PARAM_DMLINK));
				doc.setDmName(archive.getArchiveName());
				doc.setDmLockStatus(Constants.ARC_DOC_LOCKSTATUS_UNLOCK);
				doc.setDmDir(folder.getFolderPath());
				doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
				doc.setDeleteFlag(0);
				// doc.setSeq(docService.getSeq());
				doc.setTableName(archive.getTableName());//归档表名
				Integer count = docService.save(doc);


				if (count > 0) {
					audithisService.audithis(request, doc.getId(),
							doc.getDmName(),
							Constants.ARC_AUDITHIS_DATATYPE_DOC,
							Constants.ARC_AUDITHIS_NEWUPDOWN, "新建归档 ");
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
							MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
				}

			} else {
				// 如果归档对象不为空，更新归档
				doc=checkDoc;

				doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_UNKNOWN);
				doc.setDmName(archive.getArchiveName());
				doc.setDmLockStatus(Constants.ARC_DOC_LOCKSTATUS_UNLOCK);
				doc.setDmDir(folder.getFolderPath());
				doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
				doc.setDeleteFlag(0);
				doc.setModifyDateNew(new Date());
				doc.setTableName(archive.getTableName());//归档表名
				Integer count=docService.update(doc);
				if (count > 0) {
					//先清空归档附件表
					Filing filing=new Filing();
					filing.setDocumentId(doc.getId());
					List<Filing> filings=filingService.queryAll(filing);
					if(filings.size() > 0) {
						String[] ids=new String[filings.size()];
						for (int i = 0; i < filings.size(); i++) {
							ids[i] = filings.get(i).getId().toString();
						}

						filing.setPrimaryKeys(ids);
						filingService.delete(filing, false);
					}

					audithisService.audithis(request, doc.getId(),
							doc.getDmName(),
							Constants.ARC_AUDITHIS_DATATYPE_DOC,
							Constants.ARC_AUDITHIS_NEWUPDOWN, "新建归档 ");
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
							MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
				}
			}

			// 处理归档表单
			// 创建附件记录
			if (StringUtils.isEmpty(archive.getFormContent()) == false) {
				Filing filing = new Filing();
				filing.setFileName(archive.getArchiveName());

				filing.setDocumentId(doc.getId());

				filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_FORM);
				filing.setFormContent(archive.getFormContent());
				filingService.save(filing);
			}
			// 处理归档正文
			Map<String, String> bodyMap = archive.getBodyMap();
			if (bodyMap != null) {
				Calendar calendar = Calendar.getInstance();
				String newFileName = null;

				Iterator<String> keyIte = bodyMap.keySet().iterator();
				String fileName = null;
				String filePath = null;
				while (keyIte.hasNext()) {
					fileName = keyIte.next();
					filePath = bodyMap.get(fileName);
					calendar.setTimeInMillis(System.currentTimeMillis());
					// 复制附件文件
					newFileName = String
							.valueOf(calendar.getTimeInMillis())
							+ "-"
							+ String.valueOf(calendar
							.get(Calendar.HOUR_OF_DAY))
							+ "-"
							+ String.valueOf(calendar.get(Calendar.MINUTE))
							+ "-"
							+ String.valueOf(calendar.get(Calendar.SECOND))
							+ "." + FileUtil.getFileExt(filePath);
					FileUtil.copyFile(
							folderService.getAbsoluteContextPath(request)
									+ filePath,
							folderService.getAbsoluteParentPath(request)
									+ newFileName);
					File file = new File(
							folderService.getAbsoluteParentPath(request)
									+ newFileName);
					if (file.exists()) {
						// 创建附件记录
						Filing filing = new Filing();
						filing.setFileName(fileName);
						filing.setFilePath(Constants.DJ_UPLOAD_DIR
								+ File.separator + newFileName);
						filing.setDocumentId(doc.getId());
						//	BigDecimal size = new BigDecimal(file.length());
						filing.setFizeSize(getFileSize(file.length()));
						filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_BODY);
						filingService.save(filing);
					}
				}
			}
			// 处理归档附件
			Map<String, String> filingMap = archive.getFilingMap();
			if (filingMap != null) {
				Calendar calendar = Calendar.getInstance();
				String newFileName = null;

				Iterator<String> keyIte = filingMap.keySet().iterator();
				String fileName = null;
				String filePath = null;
				while (keyIte.hasNext()) {
					fileName = keyIte.next();
					filePath = filingMap.get(fileName);
					calendar.setTimeInMillis(System.currentTimeMillis());
					// 复制附件文件
					newFileName = String
							.valueOf(calendar.getTimeInMillis())
							+ "-"
							+ String.valueOf(calendar
							.get(Calendar.HOUR_OF_DAY))
							+ "-"
							+ String.valueOf(calendar.get(Calendar.MINUTE))
							+ "-"
							+ String.valueOf(calendar.get(Calendar.SECOND))
							+ "." + FileUtil.getFileExt(filePath);
					//判断文件是否存在 不存在进行文件夹创建
					File fileCreate = new File(folderService.getAbsoluteParentPath(request));
					if (!fileCreate.exists()||!fileCreate.isDirectory()) {
						fileCreate.mkdirs();
					}
					FileUtil.copyFile(
							folderService.getAbsoluteContextPath(request)
									+ filePath,
							folderService.getAbsoluteParentPath(request)
									+ newFileName);
					File file = new File(
							folderService.getAbsoluteParentPath(request)
									+ newFileName);
					if (file.exists()) {
						// 创建附件记录
						Filing filing = new Filing();
						filing.setFileName(fileName);
						filing.setFilePath(Constants.DJ_UPLOAD_DIR
								+ File.separator + newFileName);
						filing.setDocumentId(doc.getId());
						//BigDecimal size = new BigDecimal(file.length());
						filing.setFizeSize(getFileSize(file.length()));
						filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_ATTACH);
						filingService.save(filing);
					}
				}
			}
			//saveDataToFtp(archive, documentId, archive.getRootPath());
		} catch (CustomException e) {
			e.printStackTrace();
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_021"));
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> archiveFiling(Archive archive,HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 检查参数
		if (archive.getFolderId() == null || StringUtil.isEmpty(archive.getArchiveName()) || archive.getDocId() ==null) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_022"));
			return resultMap;
		}
		if(!checkFolder(archive.getFolderId())) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
			return resultMap;
		}
		// 验证当前人有没有权限访问选中的文件夹
		Permission permission = new Permission();
		permission.setFolderId(archive.getFolderId());
		permission.setUserId(archive.getCreateUser());
		permission.setDeptId(archive.getCreateUserDept());
		permission.setOrgId(archive.getCreateUserOrg());
		List<Permission> listPermission = null;
		try {
			listPermission = permissionService.queryPermission(permission);

			if(listPermission != null && listPermission.size()>0) {
				Folder folderPermission = PermissionUtil.permissionValue(listPermission);
				if(folderPermission == null || !folderPermission.isPermNewUpDown()) {
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_027"));
					return resultMap;
				}
			}
			else{
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_027"));
				return resultMap;
			}
		} catch (ArchiveException e1) {
			e1.printStackTrace();
		}
		//检查归档目录
		Folder folder = new Folder();
		folder.setId(archive.getFolderId());
		folder.setFolderType(Constants.ARC_FOLDER_TYPE_FILE_DOC);
		try {
			folder = folderService.get(folder);
			if(folder == null) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_023"));
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_019"));
			return resultMap;
		}
		// 检查同一归档对象在同一文件夹下只允许一个实例
		Document checkDoc = new Document();
		checkDoc.setDocId(archive.getDocId());
		checkDoc.setFolderId(archive.getFolderId());
		checkDoc.setFileType(Constants.ARC_DOC_FILETYPE_FILING);
		checkDoc.setTableName(archive.getTableName());//归档表名
		checkDoc = docService.get(checkDoc);
		Document doc=null;
		if (checkDoc == null) {
			// 如果归档对象为空，是新建归档
			doc = new Document();
			doc.setFolderId(archive.getFolderId());
			doc.setDocId(archive.getDocId());
			doc.setFileType(Constants.ARC_DOC_FILETYPE_FILING);
			doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_UNKNOWN);
			doc.setDmName(archive.getArchiveName());
			doc.setDmLockStatus(Constants.ARC_DOC_LOCKSTATUS_UNLOCK);
			doc.setDmDir(folder.getFolderPath());
			doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
			doc.setDeleteFlag(0);
			doc.setTableName(archive.getTableName());//归档表名
			doc.setCreateUser(archive.getCreateUser());//归档人id
			doc.setCreateUserName(archive.getCreateUserName());//归档人名称
			doc.setCreateUserDept(archive.getCreateUserDept());//归档人部门id
			doc.setCreateUserDeptName(archive.getCreateUserDeptName());//归档人部门名称
			doc.setCreateUserOrg(archive.getCreateUserOrg());//归档人机构
			Integer count = docService.save(doc);
			if (count > 0) {
				/*audithisService.audithis(request, doc.getId(),
						doc.getDmName(),
						Constants.ARC_AUDITHIS_DATATYPE_DOC,
						Constants.ARC_AUDITHIS_NEWUPDOWN, "新建归档 ");*/
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
						MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
			}else{
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,"新建归档失败");
				return resultMap;
			}

		} else {
			// 如果归档对象不为空，更新归档
			doc=checkDoc;

			doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_UNKNOWN);
			doc.setDmName(archive.getArchiveName());
			doc.setDmLockStatus(Constants.ARC_DOC_LOCKSTATUS_UNLOCK);
			doc.setDmDir(folder.getFolderPath());
			doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
			doc.setDeleteFlag(0);
			doc.setModifyDateNew(new Date());
			doc.setTableName(archive.getTableName());//归档表名
			Integer count=docService.update(doc);
			if (count > 0) {
				//先清空归档附件表
				Filing filing=new Filing();
				filing.setDocumentId(doc.getId());
				List<Filing> filings=filingService.queryAll(filing);
				if(filings.size() > 0) {
					String[] ids=new String[filings.size()];
					for (int i = 0; i < filings.size(); i++) {
						ids[i] = filings.get(i).getId().toString();
					}

					filing.setPrimaryKeys(ids);
					filingService.delete(filing, false);
				}

				/*audithisService.audithis(request, doc.getId(),
						doc.getDmName(),
						Constants.ARC_AUDITHIS_DATATYPE_DOC,
						Constants.ARC_AUDITHIS_NEWUPDOWN, "新建归档 ");*/
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
						MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
			}else{
					resultMap.put("success", "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,"更新归档失败");
				return resultMap;
			}
		}

		try {
			// 处理归档表单
			if (StringUtils.isEmpty(archive.getFormContent()) == false) {
				Filing filing = new Filing();
				filing.setFileName(archive.getArchiveName());

				filing.setDocumentId(doc.getId());

				filing.setFileType(Constants.ARC_ARCHIVE_FILETYPE_FORM);
				filing.setFormContent(archive.getFormContent());
				Integer count = filingService.save(filing);
				if(count >0){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
							MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
				}else{
					resultMap.put("success", "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,"文档表单处理失败");
					return resultMap;
				}
			}
			//处理公文正文和附件
			if(archive.getFileList() != null && archive.getFileList().size() >0){
				Integer count = 0;
				for(int i=0;i<archive.getFileList().size();i++){
					Filing filing = archive.getFileList().get(i);
					filing.setDocumentId(doc.getId());
					count = filingService.save(filing);
				}
				if(count >0){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
							MessageUtils.getMessage("JC_OA_ARCHIVE_020"));
				}else{
					resultMap.put("success", "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,"文档附件处理失败");
					return resultMap;
				}
			}
		} catch (CustomException e) {
			e.printStackTrace();
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_021"));
			return resultMap;
		}
		return resultMap;
	}

	private String getFileSize(Long sizeValue) {
		BigDecimal size = new BigDecimal(sizeValue);
		if(sizeValue / 1024 < 1024) {

			return (size.divide(new BigDecimal(1024))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toString()
					+ "KB");
		} else {
			return (size.divide(new BigDecimal(1024 * 1024))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toString()
					+ "MB");
		}
	}

	/**
	 * 将归档数据存放到 档案中间表
	 * @param archive
	 * @param documentId
	 */
	@SuppressWarnings("resource")
	private void saveDataToFtp(Archive archive, String documentId, String rootPath) {
		Map<String, String> wsftp = PropertiesUtil.getProperties("/ws_ftp.properties");
		String oracleIp = wsftp.get("oracleIP");
		String oracleUserName = wsftp.get("oracleUserName");
		String oraclePassword = wsftp.get("oraclePassword");
		String pdfForder = wsftp.get("pdfForder");
		String ftpHost = wsftp.get("ftpHost");
		String formName ="";
		log.info("开始归档数据同步");
		String relativeId = "";
		try {
			Filing filing = new Filing();
			filing.setDocumentId(documentId);
			List<Filing> list = filingService.queryAll(filing);
			if(list != null && list.size() > 0) {
//				Connection conn = JDBC_Oracle.getConnection(oracleIp, oracleUserName, oraclePassword);
				PreparedStatement ps = null;
				String id = archive.getTableName() + "-" +archive.getPiId();
				/*if(conn != null) {
					log.info("连接oracle数据库成功");
					String tableName = archive.getTableName();//"";
					 ps = conn.prepareStatement("delete from T_INFORMATION where OA_ID=?"); 
					 ps.setString(1, id);
					 ps.execute();
					 ps = conn.prepareStatement("delete from T_BAS_ATTACHMENT where C_MIDRECID=?"); 
					 ps.setString(1, id);
					 ps.execute();
					 if("TOA_DOC_SEND".equals(tableName)) {
						 //发文
						 String uuid = Identities.uuid2();
						 relativeId = uuid;
						 Send send = new Send();
						 send.setId(Long.valueOf(archive.getPiId()));
						 send = sendService.get(send);
						 if(send != null) {
							 formName = send.getTitle();
							 String sendSql = "insert into T_INFORMATION"+
									 "(id,FNAME,FDATE,FNUMBER,FSENDUNIT,TITLE,MAIN_SEND_USER_NAME,COPY_SEND_USER_NAME,SECRET,PRIORITY,NO_VALUE,SAVE_DATE,DEPT_NAME,SUBSTITUS_USER,TEL,OPEN_PROPERTY,RELATE_NO,PROOF_USER,REVIEW_USER,CHECK_USER,PRINT_NUM,SEND_TIME,CREATE_USER_DEPT,CREATE_USER_ORG,CREATE_USER_ORG_ID,DOC_TYPE,OA_ID) values"+
									 "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							 ps = conn.prepareStatement(sendSql); 
							 ps.setString(1, uuid); 
							 ps.setString(2, "");
							 ps.setDate(3, new java.sql.Date(new Date().getTime()));
							 ps.setString(4, "");
							 ps.setString(5, "");
							 ps.setString(6, send.getTitle());
							 ps.setString(7, send.getMainSendUserName() == null ? "":send.getMainSendUserName());
							 ps.setString(8, send.getCopySendUserName() == null ? "":send.getCopySendUserName());
							 ps.setString(9, send.getSecretValue());
							 ps.setString(10, send.getPriorityValue());
							 ps.setString(11, send.getNoValue() == null ? "" : send.getNoValue());
							 ps.setString(12, send.getSaveDate() == null ? "":send.getSaveDate());
							 
							 ps.setString(13, send.getDeptName() == null ? "": send.getDeptName());
							 ps.setString(14, send.getSubstitusUser() == null ? "":send.getSubstitusUser());
							 ps.setString(15, send.getTel() == null ? "":send.getTel());
							 String open = send.getOpenProperty();
							 if(open == null) {
								 ps.setString(16, "");
							 } else if("0".equals(open)) {
								 ps.setString(16, "主动公开");
							 } else if("1".equals(open)) {
								 ps.setString(16, "依申请公开");
							 } else if("2".equals(open)) {
								 ps.setString(16, "不予公开");
							 }
							 ps.setString(17, send.getRelateNo() == null ? "":send.getRelateNo());
							 ps.setString(18, send.getProofUser() == null ? "":send.getProofUser());
							 ps.setString(19, send.getReviewUser() == null ? "":send.getReviewUser());
							 ps.setString(20, send.getContractors() == null ? "":send.getContractors());
							 ps.setString(21, send.getPrintNum() == null ? "":send.getPrintNum());
							  ps.setString(22, send.getCreateDate().toLocaleString());
							  Department dept = new Department();
							  dept.setId(send.getCreateUserDept());
							  dept = departmentService.get(dept);
							  
							 ps.setString(23, dept.getName());
							 Department org = new Department();
							 org.setId(send.getCreateUserOrg());
							 org = departmentService.get(org);
							 ps.setString(24, org.getName());
							 ps.setString(25, String.valueOf(org.getId()));
							 ps.setString(26, "1");
							 ps.setString(27, id);
							 ps.executeUpdate(); 
						 }
					 } else if("TOA_DOC_RECEIVE".equals(tableName)) {
						 //收文
						 Receive receive = new Receive();
						 String uuid = Identities.uuid2();
						 relativeId = uuid;
						 receive.setId(Long.valueOf(archive.getPiId()));
						 receive = receiveService.get(receive);
						 if(receive != null) {
							 formName = receive.getTitle();
							 String receiveSql = "insert into T_INFORMATION"+
									 "(id,FNAME,FDATE,FNUMBER,FSENDUNIT,TITLE,IS_URGENT,SEND_DEPT,SEND_NO,SEQ_VALUE,CONTRACTORS,RECEIVE_DATE,TEL,CREATE_USER_DEPT,CREATE_USER_ORG,CREATE_USER_ORG_ID,DOC_TYPE,DEPT_NAME,OA_ID) values"+
									 "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							 ps = conn.prepareStatement(receiveSql); 
							 ps.setString(1, uuid); 
							 ps.setString(2, "");
							 ps.setDate(3, new java.sql.Date(new Date().getTime()));
							 ps.setString(4, "");
							 ps.setString(5, "");
							 ps.setString(6, receive.getTitle());
							 String ungent = "非急件";
							 if(receive.getIsUrgent() != null && "1".equals(receive.getIsUrgent())) {
								 ungent = "急件";
							 }
							 ps.setString(7, ungent);
							 ps.setString(8, receive.getSendDept() == null ? "" : receive.getSendDept());
							 ps.setString(9, receive.getSendNoId() == null ? "" : receive.getSendNoId());
							 ps.setString(10, receive.getSeqValue() == null ? "": receive.getSeqValue());
							 ps.setString(11, receive.getContractors() == null ? "":receive.getContractors());
							 ps.setString(12, receive.getReceiveDateStr());
							 ps.setString(13, receive.getPhoneNum() == null ? "" :receive.getPhoneNum());
							 Department dept = new Department();
							  dept.setId(receive.getCreateUserDept());
							  dept = departmentService.get(dept);
							  
							 ps.setString(14, dept.getName());
							 Department org = new Department();
							 org.setId(receive.getCreateUserOrg());
							 org = departmentService.get(org);
							 ps.setString(15, org.getName());
							 ps.setString(16, String.valueOf(org.getId()));
							 ps.setString(17, "2");
							 ps.setString(18, "");
							 ps.setString(19, id);
							 ps.executeUpdate(); 
						 }
					 }
					 

					 //获取表单html转pdf后的文件
					 String sql = "";
					 
					 // ps.setInt(3, per.getSex()); 
					 File formFile = new File(rootPath+ File.separator + Constants.DJ_UPLOAD_PDF_DIR+File.separator + archive.getHtmlFileName()); 
					 if(formFile != null && formFile.exists()) {
						// Long mill =  Calendar.getInstance().getTimeInMillis();
						// String newName = String.valueOf(mill) + ".pdf";
						 FileInputStream  input = new FileInputStream (formFile);
						 FtpWSUploader.uploadFile(tableName, formFile.getName(), input);
						 String uuid = Identities.uuid2();
						 sql = "insert into T_BAS_ATTACHMENT(id,C_MIDRECID,C_TYPE,TITLE,I_SIZE,C_FTPFILEPATH) values(?,?,?,?,?,?)";
						 ps = conn.prepareStatement(sql); 
						 ps.setString(1, uuid); 
						 ps.setString(2, relativeId);
						 ps.setString(3, "3");
						 ps.setString(4, formName + ".pdf");
						 ps.setString(5, "");
						 ps.setString(6, "/"+tableName+"/"+formFile.getName());
						// ps.setString(6, "ftp://"+ftpHost + "/"+tableName+"/"+formFile.getName());
						 // ps.setDate(2, per.getBirthday()); 
						 
						 // ps.setInt(3, per.getSex()); 
						 int i = ps.executeUpdate();
						 input.close();
						 formFile.delete();
					 } else {
						 log.info(archive.getHtmlFileName() + "----html转换后的pdf文件不存在");
					 }
					for(Filing f : list) {
						log.info("归档数据id:"+f.getId());
						if(1 == f.getFileType()) {
							//正文
							log.info("正文归档开始");
							File contentFile = new File("c:\\send.pdf");
							File file = new File(rootPath + File.separator+f.getFilePath());
							String fileName = file.getName();
							String newFileName = fileName.replaceAll(".aip", ".pdf");
							File newFile = new File(rootPath+"\\upload\\"+newFileName); 
							contentFile.renameTo(newFile);
							FileInputStream  input = new FileInputStream (newFile);
							String uuid = Identities.uuid2();
							 FtpWSUploader.uploadFile(tableName, newFileName, input);
							 sql = "insert into T_BAS_ATTACHMENT(id,C_MIDRECID,C_TYPE,TITLE,I_SIZE,C_FTPFILEPATH) values(?,?,?,?,?,?)";
							 ps = conn.prepareStatement(sql); 
							 ps.setString(1, uuid); 
							 ps.setString(2, relativeId);
							 ps.setString(3, "1");
							 ps.setString(4, f.getFileName().replaceAll(".aip", ".pdf"));
							 ps.setString(5, f.getFizeSize());
							 //ps.setString(6, "ftp://"+ftpHost + "/"+tableName+"/"+newFileName);
							 ps.setString(6, "/"+tableName+"/"+newFileName);
							// ps.setDate(2, per.getBirthday()); 

							// ps.setInt(3, per.getSex()); 
							 int i = ps.executeUpdate(); 
							 log.info("正文归档成功  filename:"+newFileName);
							 try {
								 input.close();
								 contentFile.delete();
								 newFile.delete();
								 file.delete();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if(2 == f.getFileType()) {
							//附件
							log.info("附件归档开始");
							File file = new File(rootPath + File.separator+f.getFilePath());
							FileInputStream  input = new FileInputStream (file);
							String uuid = Identities.uuid2();
							String fileName = file.getName();
							FtpWSUploader.uploadFile(tableName, fileName, input);
							 sql = "insert into T_BAS_ATTACHMENT(id,C_MIDRECID,C_TYPE,TITLE,I_SIZE,C_FTPFILEPATH) values(?,?,?,?,?,?)";
							 ps = conn.prepareStatement(sql); 
							 ps.setString(1, uuid); 
							 ps.setString(2, relativeId);
							 ps.setString(3, "2");
							 ps.setString(4, f.getFileName());
							 ps.setString(5, f.getFizeSize());
							 ps.setString(6, "/"+tableName+"/"+fileName);
							 //ps.setString(6, "ftp://"+ftpHost + "/"+tableName+"/"+fileName);
							 int i = ps.executeUpdate(); 
							 log.info("附件归档成功: fileName"+fileName);
							// input.close();
						}
					}
					
					ps.close();*/
				//发文归档
				/*	byte[] data = Base64.decodeBase64(aaa);
					FileOutputStream out;
					try {
						out = new FileOutputStream("c:\\aa.aip");
						out.write(data);
						out.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			import org.apache.commons.codec.binary.Base64;*/


//				} else {
//					log.info("连接oracle数据库失败：oracleIp"+oracleIp+"  username:"+oracleUserName+"  password:"+oraclePassword);
//				}
//			} else{
				log.info("未找到归档数据documentId:" + documentId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("归档存储错误："+e.getMessage());
		}

	}
}
