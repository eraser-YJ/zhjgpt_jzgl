package com.jc.archive.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Folder;
import com.jc.archive.domain.Recycle;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @title  GOA2.0源代码
 * @description  业务接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public interface IArchiveFolderService extends IBaseService<Folder>{

    void saveFolder(Map<String, Object> infoMap) throws CustomException;

    void uploadAttach(Map<String, Object> infoMap, HttpServletRequest request) throws CustomException;

    /**
	 * 方法描述：获取指定目录下列表信息
	 * @param folder
	 * @return
	 * @author zhanglg
	 * @version  2014年6月6日下午2:34:02
	 * @throws ArchiveException 
	 * @see
	 */
	public Folder getDirDocs(Folder folder) throws ArchiveException;
	//List<Folder> queryAllByPermission(Folder folder) throws ArchiveException;
	/**
	 * 方法描述：上传文档保存方法
	 * @param folder
	 * @throws ArchiveException
	 * @author 张立刚
	 * @version  2014年6月17日下午7:11:53
	 * @return 
	 * @see
	 */
	public List<Document> uploadDocs(Folder folder, HttpServletRequest request) throws ArchiveException;

	/**
	 * 方法描述：删除选中的文件夹和文档
	 * @param ids Id数组字符串格式，如：#dir_1,#dir_2,#doc_1
	 * @author zhanglg
	 * @version  2014年6月19日上午10:13:28
	 * @return 
	 * @throws  
	 * @see
	 */
	public Folder deleteDirDocs(String ids, HttpServletRequest request) throws ArchiveException;

	/**
	 * 方法描述：回收站数据查询
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	public List<Recycle> selectRecycle() throws ArchiveException;
	

	/**
	 * 方法描述：复制文档到指定目录中
	 * @param document
	 * @author zhangligang
	 * @version  2014年6月25日下午1:30:44
	 * @return 新文档
	 * @throws ArchiveException 
	 * @see
	 */
	public Document copyDocTo(Document document, Folder folder, HttpServletRequest request) throws ArchiveException;

	/**
	 * 方法描述：剪切文档到指定目录
	 * @param document
	 * @author zhangligang
	 * @version  2014年6月26日上午8:29:53
	 * @return 新文档名称
	 * @throws ArchiveException 
	 * @see
	 */
	public Document cutDocTo(Document document) throws ArchiveException;

	/**
	 * 方法描述：复制文件夹到指定目录中
	 * @param sourcefolder 复制的源文件夹
	 * @param targetFolder 目标文件夹
	 * @author zhangligang
	 * @version  2014年6月26日上午9:02:09
	 * @return 新文件夹
	 * @throws ArchiveException 
	 * @see
	 */
	public Folder copyDirTo(Folder sourceFolder, Folder targetFolder, HttpServletRequest request) throws ArchiveException;

	/**
	 * 方法描述：剪切文件夹到指定目录中
	 * @param folder
	 * @author zhangligang
	 * @version  2014年6月26日上午9:51:47
	 * @return 新文件夹
	 * @throws ArchiveException 
	 * @see
	 */
	public Folder cutDirTo(Folder sourcefolder, Folder targetfolder) throws ArchiveException;
	/**
	 * 方法描述：回收站数据查询
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月18日
	 * @see
	 */
	public Integer batchDelete(String id, String type) throws ArchiveException;
	
	/**
	 * 方法描述：回收站数据还原
	 * 
	 * @param folder
	 * @throws ArchiveException
	 * @author weny
	 * @version 2014年6月26日
	 * @see
	 */
	public Map<String, Object> batchRecycle(String id, String type) throws ArchiveException;
	
	/**
	 * 方法描述：查询文档
	 * @param doc
	 * @return
	 * @author zhangligang
	 * @version  2014年6月27日下午4:11:06
	 * @throws ArchiveException 
	 * @see
	 */
	public Document getDocument(Document doc) throws ArchiveException;

	/**
	 * 方法描述：获取新文件名，已处理重名问题
	 * @param parentFolderId
	 * @param name
	 * @return
	 * @throws ArchiveException
	 * @author zhangligang
	 * @version  2014年7月2日上午10:35:03
	 * @see
	 */
	public String getNewFolderName(String parentFolderId, String name)
			throws ArchiveException;
	/**
	 * 方法描述：取得项目下文档绝对路径（不包含文件名）
	 * 
	 * @param request
	 * @return
	 * @author zhangligang
	 * @version 2014年7月2日下午2:48:41
	 * @see
	 */
	public String getAbsoluteParentPath(HttpServletRequest request);

	/**
	 * 方法描述：取得项目上下文绝对路径
	 * 
	 * @param request
	 * @return
	 * @author zhangligang
	 * @version 2014年7月2日下午3:02:50
	 * @see
	 */
	public String getAbsoluteContextPath(HttpServletRequest request);
	
	/**
	 * @description 权限过滤文件夹
	 * @param Folder folder 实体类
	 * @return List<Folder>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-09
	 */
	public List<Folder> getFolderPermission(Folder folder);
	
	/**
	 * 方法描述：归档目录下列表信息
	 * 
	 * @param folder
	 * @return
	 * @author weny
	 * @version 2014-07-09
	 * @throws ArchiveException
	 * @see
	 */
	public Folder getFolderPermissionQuery(Folder folder) throws ArchiveException;

	/**
	 * 方法描述：取得新文件名
	 * @param folderId
	 * @param dmName
	 * @return
	 * @author zhangligang
	 * @version  2014年7月11日上午9:38:41
	 * @see
	 */
	public String getNewFileName(String folderId, String dmName, String ext)throws ArchiveException;
	
	
	/**
	 * 方法描述：回收站清空
	 * 
	 * @param folder
	 * @author weny
	 * @version 2014年6月18日
	 * @throws CustomException 
	 * @see
	 */
	public int clearRecycl() throws CustomException;
	/**
	 * 方法描述：批量删除文件夹和文件
	 * 
	 * @param folder
	 * @author 
	 * @version 
	 * @throws CustomException 
	 * @see
	 */
	public Integer bulkDelete(String idsdir, String idsdoc) throws ArchiveException;
	/**
	 * 方法描述：查询是否是机构管理员管理下的归档目录下的文件夹和文件
	 * 
	 * @param folder
	 * @author 
	 * @version 2014年10月24日
	 * @throws CustomException 
	 * @see
	 */
	public Folder getFileDirDocsQuery(Folder folder) throws ArchiveException;


	boolean checkAttachName(Folder folder);

	public Folder getDirDocs4Rtx(Folder folder, String userId) throws ArchiveException;
}