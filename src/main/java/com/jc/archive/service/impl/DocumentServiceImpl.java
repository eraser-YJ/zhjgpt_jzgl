package com.jc.archive.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.archive.ArchiveException;
import com.jc.archive.dao.IDocumentDao;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Filing;
import com.jc.archive.service.IDocumentService;
import com.jc.archive.service.IFilingService;
import com.jc.system.common.util.Constants;
import com.jc.system.content.service.IAttachBusinessService;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title GOA2.0源代码
 * @description 业务服务类 Copyright (c) 2014 yixunnet.com Inc. All Rights
 *              Reserved Company 长春嘉诚网络工程有限公司
 * @author
 * @version 2014-06-05
 */
@Service
public class DocumentServiceImpl extends BaseServiceImpl<Document> implements
		IDocumentService {

	private IDocumentDao documentDao;

	@Autowired
	private IAttachService attachService;

	@Autowired
	private IAttachBusinessService attachBusinessService;

	@Autowired
	private IFilingService filingService;

	public DocumentServiceImpl() {
	}

	@Autowired
	public DocumentServiceImpl(IDocumentDao documentDao) {
		super(documentDao);
		this.documentDao = documentDao;
	}

	/**
	 * 方法描述：文件复制保存关联表数据
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int insertCollection(Document ment, String id)
			throws ArchiveException {
		int count = 0;
		try {
			ment.setId(id);
			Document document = get(ment);

			document.setCollectId(SystemSecurityUtils.getUser().getId());
			document.setFileType(Constants.ARC_DOC_FILETYPE_FAVOR);
			document.setDmInRecycle(0);
			document.setBackUpId(Long.valueOf(id));
			document.setId(null);
			count = save(document);
		} catch (CustomException e) {
			e.printStackTrace();
			ArchiveException ae = new ArchiveException();
			ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
			throw ae;
		}
		return count;
	}

	/**
	 * 方法描述：更新收藏数据
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int updateCollection(Document ment, String id)
			throws ArchiveException {
		int count = 0;
		try {
			ment.setBackUpId(Long.valueOf(id));
			ment.setFileType(Constants.ARC_DOC_FILETYPE_FAVOR);
			ment.setId(null);
			ment.setCollectId(SystemSecurityUtils.getUser().getId());
			ment.setDmInRecycle(0);
			Document document = get(ment);
			Document doc = new Document();
			doc.setId(id);
			doc = get(doc);
			doc.setId(document.getId());
			doc.setFileType(Constants.ARC_DOC_FILETYPE_FAVOR);
			doc.setDmInRecycle(0);

			// 收藏人
			doc.setCollectId(SystemSecurityUtils.getUser().getId());
			count = update(doc);
		} catch (CustomException e) {
			e.printStackTrace();
			ArchiveException ae = new ArchiveException();
			ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
			throw ae;
		}
		return count;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int deleteDocToRecycle(Document doc) {
		int count = 0;
		count = documentDao.deleteDocToRecycle(doc);
		return count;
	}

	@Override
	public String getSeq(Document d) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(Calendar.YEAR);
		d.setCurrentYear(year);
		String seq = documentDao.getSeq(d);
		return seq;
	}

	private String getName(List<Document> documents, String name) throws ArchiveException {
		String newName = name;
		try {
			// 判断文件名是否重复
			if (documents != null) {
				Map<String, Document> map = new HashMap<String, Document>();
				for (Document doc : documents) {
					map.put(doc.getDmName(), doc);
				}

				for (int i = 0; map.get(newName) != null; i++) {
					newName = name + "(" + (i+1) + ")";
					// newName+="(1)";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ArchiveException ae = new ArchiveException();
			ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
			throw ae;
		}
		return newName;
	}
	@Override
	public String getNewFileName(String folderId, String name,String ext, String type) throws ArchiveException {
		Document target = new Document();
		target.setFolderId(folderId);
		target.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		target.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
		target.setDeleteFlag(0);
		target.setFileType(type);
		target.setDmSuffix(ext);
		try {
			return getName(this.queryAll(target), name);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return name;
		}
	}
	@Override
	public String getNewFileName(Document d, String name) throws ArchiveException {
		Document target = new Document();
		target.setFolderId(d.getFolderId());
		target.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		target.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
		target.setDeleteFlag(0);
		target.setFileType(d.getFileType());
		target.setDmSuffix(d.getDmSuffix());
		target.setModel(d.getModel());
		try {
			return getName(this.queryAll(target), name);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return name;
		}
	}
	
	@Override
	public String getNewFileName(String folderId, String name,String ext) throws ArchiveException {
		// 查询目标目录下所有文件，用于判断文件名是否重复
		Document target = new Document();
		target.setFolderId(folderId);
		target.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		target.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
		target.setDeleteFlag(0);
		target.setDmSuffix(ext);
		try {
			return getName(this.queryAll(target), name);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return name;
		}
	}
	
	
	@Override
	public String getNewFileNameByModel(String folderId, String name,String ext, int model) throws ArchiveException {
		// 查询目标目录下所有文件，用于判断文件名是否重复
		Document target = new Document();
		target.setFolderId(folderId);
		target.setModel(model);
	//	target.setDmName(name);
	//	target.setDmSuffix(ext);
		target.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		target.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
		target.setDeleteFlag(0);
		target.setFileType("0");
		target.setDmSuffix(ext);
		try {
			return getName(this.queryAll(target), name);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return name;
		}
	}
	
	
	/**
	 * @description 根据文档id获取未关联文档的集合
	 * @param Document 实体类
	 * @param page 分页信息
	 * @return map：list 查询的记录 ，page分页信息
	 * @author 
	 * @version 2014-07-02
	 */
	public PageManager queryByRelate(Document document,final PageManager page){
		return documentDao.queryByRelate(document, page);
	}

	/**
	 * @获得附件信息
	 * @param filing 实体类
	 * @author 
	 * @version 2014-07-09
	 * @throws CustomException 
	 */
	@Override
	public List<Filing> queryFiling(Filing filing) throws CustomException{
		return filingService.queryAll(filing);
	}

	/**
	 * @附件下载
	 * @version 2014-07-10
	 * @throws CustomException 
	 */
	@Override
	public void downLoad(String id, HttpServletRequest request,
			HttpServletResponse response) throws CustomException{
		Document document = new Document();
		document.setId(id);
		document = this.get(document);
		filingService.downLoad(id, document.getDmName(), request, response);
	}

	/**
	 * @附件下载
	 * @version 2014-07-10
	 * @throws CustomException 
	 */
	@Override
	public int downLoadCheck(String id, HttpServletRequest request) throws CustomException{
		return filingService.downLoadCheck(id, request);
	}
	
	/**
	 * @销毁归档数据
	 * @author weny
	 * @version 2014-07-15
	 * @throws CustomException 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteFiling(String id, HttpServletRequest request) throws CustomException{
		String path = GlobalContext.getProperty("gxwj");
		Filing filing = new Filing();
		filing.setDocumentId(id);
		List<Filing> file = filingService.queryAll(filing);
		for(int i=0;i<file.size();i++){
			Filing fil = file.get(i);
			if(fil.getFilePath() !=null && fil.getFilePath().length()>0){
				com.jc.system.common.util.FileUtil.delFile(path + fil.getFilePath());
			}
		}
		filingService.getDeleteFiling(file);
		Document doc = new Document();
		doc.setId(id);
		documentDao.deleteList(doc);
	}

	public PageManager  getDataByPermission(Document doc) {
		PageManager p = new PageManager();
		p.setPageRows(999999999);
		return queryForPermission(doc, p, "queryCount", "query");
	}
}