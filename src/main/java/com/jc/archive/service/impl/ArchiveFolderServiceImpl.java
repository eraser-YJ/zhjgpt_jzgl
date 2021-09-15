package com.jc.archive.service.impl;

import com.jc.csmp.common.enums.ProjectStageForder;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.archive.ArchiveException;
import com.jc.archive.dao.IArchiveFolderDao;
import com.jc.archive.domain.*;
import com.jc.archive.service.*;
import com.jc.archive.util.PermissionUtil;
import com.jc.foundation.util.UploadUtils;
import com.jc.system.common.util.Constants;
import com.jc.system.common.util.FileUtil;
import com.jc.system.content.service.IAttachBusinessService;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.AdminSide;
import com.jc.system.security.service.IAdminSideService;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author
 * @version 2014-06-05
 * @title GOA2.0源代码
 * @description 业务服务类 Copyright (c) 2014 yixunnet.com Inc. All Rights
 * Reserved Company 长春嘉诚网络工程有限公司
 */
@Service
public class ArchiveFolderServiceImpl extends BaseServiceImpl<Folder> implements IArchiveFolderService {

    private IArchiveFolderDao folderDao;

    private IDocumentService docService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IAudithisService audithisService;

    @Autowired
    private IVersionService versionService;

    @Autowired
    private IRelateService relateService;

    @Autowired
    private IAdminSideService adminSideService;

    private IAttachBusinessService attachBusinessService;
    private IAttachService attachService;

    public ArchiveFolderServiceImpl() {
    }

    @Override
    public void saveFolder(Map<String, Object> infoMap) throws CustomException {
        String projectName = (String) infoMap.get("projectName");
        Object projectNumber = infoMap.get("projectNumber");
        Object buildDeptId = infoMap.get("buildDeptId");
        String folderType = (String) infoMap.get("folderType");//0-资料管理；5-文档管理

        if (StringUtil.isEmpty(folderType)) {
            folderType = "5";
        }
        Folder parent = new Folder();
        parent.setFolderName("根目录");
        parent.setFolderType(folderType);
        parent.setDeleteFlag(0);
        List<Folder> folderList1 = this.folderDao.queryAll(parent);
        if (folderList1 != null && folderList1.size() > 0) {
            parent = folderList1.get(0);
        }
        Integer save = 0;
        Folder folder = new Folder();
        folder.setExtStr1(projectNumber + "");
        folder.setExtStr2("-1");
        folder.setDeleteFlag(0);
        if (!this.checkFolderHave(folder)) {
            folder.setFolderPath("根目录/" + projectName);
            folder.setParentFolderId(parent.getId());
            folder.setDmInRecycle(0);
            folder.setModel(0);
            folder.setFolderName(projectName);
            folder.setFolderType(folderType);
            folder.setKmAppFlag("0");
            this.save(folder);
        }

        try {

            List<Folder> folderList = new ArrayList<>();
            Map<String, List<ProjectStageForder>> map = new HashMap<>();

            List<ProjectStageForder> projectStageForderList = ProjectStageForder.getFolderByType(folderType);
            for (ProjectStageForder em : projectStageForderList) {
                Folder newfolder = new Folder();
                newfolder.setExtStr1(projectNumber + "");
                newfolder.setExtStr2(em.getCode());
                newfolder.setDeleteFlag(0);
                if (!this.checkFolderHave(newfolder)) {
                    newfolder.setCurrentUserDeptId(buildDeptId + "");
                    newfolder.setFolderPath("根目录/" + projectName + "/" + em.getName());
                    newfolder.setFolderName(em.getName());
                    newfolder.setParentFolderId(folder.getId());
                    newfolder.setCurrentUserDeptId(buildDeptId + "");
                    newfolder.setFolderType(folderType);
                    newfolder.setKmAppFlag("0");
                    newfolder.setDmInRecycle(0);
                    newfolder.setModel(0);

                    if (em.getPareantCode().equals("-1")) {
                        this.save(newfolder);
                    } else {
                        List<ProjectStageForder> childrenForderList = map.get(em.getPareantCode());
                        if (childrenForderList == null) {
                            childrenForderList = new ArrayList<>();
                            map.put(em.getPareantCode(), childrenForderList);
                        }
                        childrenForderList.add(em);
                    }
                }else{
                    continue;
                }


            }
            if (map != null) {
                saveFolderByEm(map, infoMap, folderType);
            }


        } catch (DBException e) {
            e.printStackTrace();
            throw new CustomException("创建项目根目录异常" + e.getMessage());
        }
    }

    public boolean checkFolderHave(Folder folder) throws CustomException {
        List<Folder> folderList = this.queryAll(folder);
        if (folderList == null || folderList.size() <= 0) {
            return false;
        } else {
            folder = folderList.get(0);
        }
        return true;
    }
    @Override
    public void uploadAttach(Map<String, Object> infoMap, HttpServletRequest request) throws CustomException {
        // 这里是数据库入库操作.

        Attach attach = null;
        try {
            attach = new Attach();
            attach.setFileSize((Long) infoMap.get("fileSize"));

            attach.setFileName((String) infoMap.get("fileName"));
            attach.setUploadTime(new Date());
            attach.setUrl(UploadUtils.REQUEST_URI + "/download.action");
            attach.setResourcesName((String) infoMap.get("filePath"));
            attach.setContent_type("multipart/form-data");
            attach.setCategory("picList1");
            attach.setExt((String) infoMap.get("fileType"));
            attach.setBusinessId((String) infoMap.get("businessId"));
            attachService.save(attach);
            infoMap.put("fileids",attach.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("上传附件错误"+e.getMessage());
        }

    }

    private void saveFolderByEm(Map<String, List<ProjectStageForder>> folderMap, Map<String, Object> infoMap, String folderType) throws CustomException {
        List<Folder> folderList = new ArrayList<>();
        Folder queryFole = new Folder();
        queryFole.setExtStr1(infoMap.get("projectNumber") + "");
        for (String key : folderMap.keySet()) {
            queryFole.setExtStr2(key);
            queryFole.setDeleteFlag(0);
            folderList = folderDao.queryAll(queryFole);
            Folder parent = new Folder();
            if (folderList != null && folderList.size() > 0) {
                parent = folderList.get(0);
            } else {
                continue;
            }
            List<ProjectStageForder> projectStageForderList = folderMap.get(key);
            for (ProjectStageForder em : projectStageForderList) {
                Folder newfolder = new Folder();
                newfolder.setFolderPath("根目录/" + infoMap.get("projectName") + "/" + parent.getFolderName() + "/" + em.getName());
                newfolder.setFolderName(em.getName());
                newfolder.setParentFolderId(parent.getId());
                newfolder.setDeleteFlag(0);
                newfolder.setDmInRecycle(0);
                newfolder.setModel(0);
                newfolder.setFolderType(folderType);
                newfolder.setKmAppFlag("0");
                newfolder.setExtStr1(infoMap.get("projectNumber") + "");
                newfolder.setExtStr2(em.getCode());
                try {
                    this.save(newfolder);
                } catch (DBException e) {
                    e.printStackTrace();
                    throw new CustomException("生成子目录错误" + e.getMessage());
                }
            }


        }
    }

    @Autowired
    public ArchiveFolderServiceImpl(IArchiveFolderDao folderDao,
                                    IDocumentService docService,
                                    IAttachBusinessService attachBusinessService,
                                    IAttachService attachService, IPermissionService permissionService) {
        super(folderDao);
        this.folderDao = folderDao;
        this.docService = docService;
        this.attachBusinessService = attachBusinessService;
        this.attachService = attachService;
        this.permissionService = permissionService;
    }

    private PageManager getDataByPermission(Folder folder) {
        PageManager p = new PageManager();
        p.setPageRows(999999999);
        return queryForPermission(folder, p, "queryCount", "query");
        /*
         * try { } catch (Exception e) { // TODO Auto-generated catch block
         * e.printStackTrace(); return null; }
         */
    }

    /**
     * 方法描述：获取指定目录下列表信息
     *
     * @param folder
     * @return
     * @throws ArchiveException
     * @author zhanglg
     * @version 2014年6月6日下午2:34:02
     * @see
     */
    @Override
    public Folder getDirDocs(Folder folder) throws ArchiveException {
        folder.setCreateUser(null);
        // 如果参数中没有文件夹ID，既为查看根目录，应设置上级目录ID为0，做为查询条件
        if (folder.getId() == null || folder.getId().trim().length() == 0) {
            folder.setParentFolderId("0");
            folder.setId(null);
            folder.setFolderName("根目录");
            folder.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
        }

        try {
            folder = this.get(folder);
            if (folder == null) {
                return null;
            }

            // 查询直接子目录
            Folder subdir = new Folder();
            subdir.setParentFolderId(folder.getId());
            subdir.setFolderType(folder.getFolderType());
            subdir.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            subdir.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
            subdir.setDeleteFlag(0);
//			folder.addOrderByField("t.MODIFY_DATE");
            subdir.addOrderByField("t.MODIFY_DATE");
            /*
             * if(Constants.ARC_FOLDER_TYPE_PUB_DOC.equals(folder.getFolderType()
             * )) { PageManager p = getDataByPermission(subdir); if(p != null) {
             * folder.setSubdirs((List<Folder>)p.getData()); } } else
             * if(Constants
             * .ARC_FOLDER_TYPE_MY_DOC.equals(folder.getFolderType())) { }
             */
            folder.setSubdirs(folderDao.queryAll(subdir));

            // 查询当前目录下文件
            Document doc = new Document();
            doc.setFolderId(folder.getId());
            doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            doc.setDeleteFlag(0);
            if ("0".equals(folder.getFolderType())) {
                doc.setFileType(folder.getFolderType());
            }
            doc.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
            doc.addOrderByFieldDesc("t.MODIFY_DATE");
            /*
             * if(Constants.ARC_FOLDER_TYPE_PUB_DOC.equals(folder.getFolderType()
             * )) { doc.setFileType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
             * PageManager pm = docService.getDataByPermission(doc); if(pm !=
             * null) { folder.setDocuments((List<Document>)pm.getData()); } }
             * else
             * if(Constants.ARC_FOLDER_TYPE_MY_DOC.equals(folder.getFolderType
             * ())) { }
             */
            folder.setDocuments(docService.queryAll(doc));
            /**/
            // 查询当前目录权限
            Permission permission = new Permission();
            permission.setFolderId(folder.getId());
            permission.setUserId(SystemSecurityUtils.getUser().getId());
            permission.setDeptId(UserUtils.getUser(
                    SystemSecurityUtils.getUser().getId()).getDeptId());
            permission.setOrgId(UserUtils.getUser(
                    SystemSecurityUtils.getUser().getId()).getOrgId());
            List<Permission> list = permissionService
                    .queryPermission(permission);
            Folder permFolder = PermissionUtil.permissionValue(list);

            folder.setPermView(permFolder.isPermView());
            folder.setPermNewUpDown(permFolder.isPermNewUpDown());
            folder.setPermEdit(permFolder.isPermEdit());
            folder.setPermDelete(permFolder.isPermDelete());
            folder.setPermCopyPaste(permFolder.isPermCopyPaste());
            folder.setPermRename(permFolder.isPermRename());
            folder.setPermCollect(permFolder.isPermCollect());
            folder.setPermVersion(permFolder.isPermVersion());
            folder.setPermHistory(permFolder.isPermHistory());
            folder.setPermRelate(permFolder.isPermRelate());

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return folder;
    }


    @Override
    public Folder getDirDocs4Rtx(Folder folder, String userId) throws ArchiveException {
        // 如果参数中没有文件夹ID，既为查看根目录，应设置上级目录ID为0，做为查询条件


        try {
            folder = this.get(folder);
            if (folder == null) {
                return null;
            }

            // 查询直接子目录
            Folder subdir = new Folder();
            subdir.setParentFolderId(folder.getId());
            subdir.setFolderType(folder.getFolderType());
            subdir.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            subdir.setCreateUserOrg(folder.getCreateUserOrg());
            subdir.setDeleteFlag(0);
            subdir.addOrderByFieldDesc("t.MODIFY_DATE");
            /*
             * if(Constants.ARC_FOLDER_TYPE_PUB_DOC.equals(folder.getFolderType()
             * )) { PageManager p = getDataByPermission(subdir); if(p != null) {
             * folder.setSubdirs((List<Folder>)p.getData()); } } else
             * if(Constants
             * .ARC_FOLDER_TYPE_MY_DOC.equals(folder.getFolderType())) { }
             */
            folder.setSubdirs(folderDao.queryAll(subdir));

            // 查询当前目录下文件
            Document doc = new Document();
            doc.setFolderId(folder.getId());
            doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            doc.setDeleteFlag(0);
            if ("0".equals(folder.getFolderType())) {
                doc.setFileType(folder.getFolderType());
            }
            doc.setCreateUserOrg(folder.getCreateUserOrg());
            doc.addOrderByFieldDesc("t.MODIFY_DATE");
            /*
             * if(Constants.ARC_FOLDER_TYPE_PUB_DOC.equals(folder.getFolderType()
             * )) { doc.setFileType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
             * PageManager pm = docService.getDataByPermission(doc); if(pm !=
             * null) { folder.setDocuments((List<Document>)pm.getData()); } }
             * else
             * if(Constants.ARC_FOLDER_TYPE_MY_DOC.equals(folder.getFolderType
             * ())) { }
             */
            folder.setDocuments(docService.queryAll(doc));
            /**/
            // 查询当前目录权限
            Permission permission = new Permission();
            permission.setFolderId(folder.getId());
            permission.setUserId(userId.toString());
            permission.setDeptId(UserUtils.getUser(
                    userId.toString()).getDeptId());
            permission.setOrgId(UserUtils.getUser(
                    userId.toString()).getOrgId());
            List<Permission> list = permissionService
                    .queryPermission(permission);
            Folder permFolder = PermissionUtil.permissionValue(list);

            folder.setPermView(permFolder.isPermView());
            folder.setPermNewUpDown(permFolder.isPermNewUpDown());
            folder.setPermEdit(permFolder.isPermEdit());
            folder.setPermDelete(permFolder.isPermDelete());
            folder.setPermCopyPaste(permFolder.isPermCopyPaste());
            folder.setPermRename(permFolder.isPermRename());
            folder.setPermCollect(permFolder.isPermCollect());
            folder.setPermVersion(permFolder.isPermVersion());
            folder.setPermHistory(permFolder.isPermHistory());
            folder.setPermRelate(permFolder.isPermRelate());

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return folder;
    }

    /**
     * 方法描述：归档目录下列表信息
     *
     * @param folder
     * @return
     * @throws ArchiveException
     * @author weny
     * @version 2014-07-09
     * @see
     */
    @Override
    public Folder getFolderPermissionQuery(Folder folder)
            throws ArchiveException {
        folder.setCreateUser(null);
        // 如果参数中没有文件夹ID，既为查看根目录，应设置上级目录ID为0，做为查询条件
        if (folder.getId() == null) {
            folder.setParentFolderId("0");
            folder.setFolderName("根目录");
            if (Constants.ARC_FOLDER_TYPE_FILE_DOC.equals(folder.getFolderType())) {
                folder.setCreateUserOrg(UserUtils.getUser(
                        SystemSecurityUtils.getUser().getId()).getOrgId());
            }
        }

        try {
            folder = this.get(folder);
            if (folder == null) {
                return null;
            }

            // 查询直接子目录
            Folder subdir = new Folder();
            subdir.setParentFolderId(folder.getId());
            // subdir.setFolderType(Constants.ARC_FOLDER_TYPE_PUB_DOC);
            subdir.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            subdir.setDeleteFlag(0);
            subdir.setOrderBy("a.MODIFY_DATE DESC");
            subdir.setCreateUser(SystemSecurityUtils.getUser().getId());
            subdir.setCreateUserDept(UserUtils.getUser(
                    SystemSecurityUtils.getUser().getId()).getDeptId());
            subdir.setCreateUserOrg(UserUtils.getUser(
                    SystemSecurityUtils.getUser().getId()).getOrgId());
            List<Folder> list = folderDao.getFolderPermission(subdir);
            // if(list != null && list.size()>0){
            // for(int i=0;i<list.size();i++){
            // Folder f = list.get(i);
            // f.setOwner(UserUtils.getUser(SystemSecurityUtils.getUser().getId()).getDisplayName());
            // }
            // }
            folder.setSubdirs(list);

            // 查询当前目录下文件
            Document doc = new Document();
            doc.setFolderId(folder.getId());
            doc.setFileType(Constants.ARC_FOLDER_TYPE_FILE_DOC);
            doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
            doc.setDeleteFlag(0);
            doc.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
            doc.setOrderBy("t.MODIFY_DATE DESC");
            folder.setDocuments(docService.queryAll(doc));

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return folder;
    }

    /**
     * 验证附件名称长度是否超过64，超过返回false，不超过返回true
     *
     * @param folder
     * @return
     */
    public boolean checkAttachName(Folder folder) {
        if (folder.getFileids() != null) {
            String fileIds[] = folder.getFileids().split(",");
            if (fileIds != null && fileIds.length > 0) {
                for (String fileid : fileIds) {
                    // 读取上传的附件记录
                    try {
                        Attach attach = new Attach();
                        attach.setId(fileid);
                        attach = attachService.get(attach);
                        String fileName = attach.getFileName();
                        int index = fileName.lastIndexOf(".");
                        String name = fileName.substring(0, index);
                        if (name != null && name.length() > 64) {
                            return false;
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        return true;
    }

    /**
     * 方法描述：上传文档保存方法。通过上传组件统一上传，出于统一管理的目的，并且与点聚WebOffice组件工作模式兼容，
     * 所有文件要统一移动到Constants.DJ_UPLOAD_DIR目录下。Attach表的记录不再需要，予以删除。
     *
     * @param folder
     * @throws ArchiveException
     * @author 张立刚
     * @version 2014年6月17日下午7:11:53
     * @see
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<Document> uploadDocs(Folder folder, HttpServletRequest request)
            throws ArchiveException {
        List<Document> documents = new ArrayList<Document>();
        if (folder.getFileids() != null) {
            String fileIds[] = folder.getFileids().split(",");
            Folder queryFolder = new Folder();
            queryFolder.setId(folder.getId());
            Folder parentfolder;
            try {
                parentfolder = this.get(queryFolder);
            } catch (CustomException e1) {
                e1.printStackTrace();
                ArchiveException ae = new ArchiveException();
                ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
                throw ae;
            }
            // 保存文档
            if (fileIds != null && fileIds.length > 0) {
                for (String fileid : fileIds) {
                    if (StringUtil.trimIsEmpty(fileid)) {
                        continue;
                    }

                    try {
                        // 读取上传的附件记录
                        Attach attach = new Attach();
                        attach.setId(fileid);
                        attach = attachService.get(attach);

                        // 创建文档记录
                        Document doc = new Document();
                        doc.setFolderId(folder.getId());
                        doc.setModel(folder.getModel());
                        doc.setFileType(Constants.ARC_DOC_FILETYPE_DOC);
					/*	String fileName = docService.getNewFileName(
								folder.getId(),
								attach.getFileName().substring(0,
										attach.getFileName().lastIndexOf(".")),
								attach.getExt());*/
                        String fileName = docService.getNewFileNameByModel(
                                folder.getId(),
                                attach.getFileName().substring(0,
                                        attach.getFileName().lastIndexOf(".")),
                                attach.getExt(), folder.getModel()==null?0:folder.getModel());
                        doc.setDmName(fileName);
                        doc.setDmAlias(attach.getResourcesName().substring(
                                attach.getResourcesName().lastIndexOf("/") + 1));
                        doc.setDmLockStatus(Constants.ARC_DOC_LOCKSTATUS_UNLOCK);
                        doc.setPhysicalPath(Constants.DJ_UPLOAD_DIR
                                + File.separator + doc.getDmAlias());
                        BigDecimal size = new BigDecimal(attach.getFileSize());
                        if (attach.getFileSize() / 1024 < 1024) {

                            doc.setDmSize(size.divide(new BigDecimal(1024))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .toString()
                                    + "K");
                        } else {
                            doc.setDmSize(size
                                    .divide(new BigDecimal(1024 * 1024))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .toString()
                                    + "M");
                        }

                        doc.setDmSuffix(attach.getExt());
                        // 根据文件扩展名决定ContentType
                        if ("doc".equalsIgnoreCase(doc.getDmSuffix())
                                || "dot".equalsIgnoreCase(doc.getDmSuffix())
                                || "docx".equalsIgnoreCase(doc.getDmSuffix())) {
                            doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_WORD);
                        } else if ("xls".equalsIgnoreCase(doc.getDmSuffix())
                                || "xla".equalsIgnoreCase(doc.getDmSuffix())
                                || "xlsx".equalsIgnoreCase(doc.getDmSuffix())) {
                            doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_EXCEL);
                        } else if ("ppt".equalsIgnoreCase(doc.getDmSuffix())
                                || "pot".equalsIgnoreCase(doc.getDmSuffix())
                                || "pps".equalsIgnoreCase(doc.getDmSuffix())
                                || "pptx".equalsIgnoreCase(doc.getDmSuffix())) {
                            doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_POWERPOINT);
                        } else {
                            doc.setContentType(Constants.ARC_DOC_CONTENTTYPE_UNKNOWN);
                        }
                        doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
                        doc.setDmDir(parentfolder.getFolderPath());
                        Document dt = new Document();
                        dt.setModel(folder.getModel()==null?0:folder.getModel());
                        doc.setSeq(docService.getSeq(dt));
                        // 增加版本号
                        Version v = versionService.createVersion(doc);
                        doc.setCurrentVersion(v.getCurrentVersion());

                        docService.save(doc);
                        v.setBackUpId(doc.getId());
                        v.setVersionDesc("上传文档(" + doc.getDmName() + ")版本号："
                                + v.getCurrentVersion());
                        v.setIsCurrentUsed(1);
                        versionService.save(v);

                        documents.add(doc);
                        File parentFile = new File(
                                this.getAbsoluteParentPath(request));
                        if (parentFile.exists() == false) {
                            if (parentFile.mkdirs() == false) {
                                // 创建目录失败
                                ArchiveException ae = new ArchiveException();
                                ae.setLogMsg(MessageUtils
                                        .getMessage("JC_SYS_002"));
                                throw ae;
                            }
                        }
                        // 读取上传的附件文件
                       /* File sourceFile = new File(this.getAbsoluteContextPath(request)//+ (String) SettingUtils.getSetting(SettingUtils.FILE_PATH)
                                + GlobalContext.getProperty("FILE_PATH")
                                + File.separator
                                + attach.getResourcesName());*/
                        File sourceFile = new File(attach.getResourcesName());
                        // 移动文件到DJ_UPLOAD_DIR
                        File targetFile = new File(this.getAbsoluteParentPath(request) + doc.getDmAlias());
                        if (sourceFile.exists()) {
                            // 用rename方式移动
                            if (sourceFile.renameTo(targetFile) == false) {
                                // 如果rename方式移动不成功，调用JC封装方法通过Copy/Delete方式实现移动
                                FileUtil.moveFile(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
                            }
                        }
						/*//不要删这段注释，这是处理felxpaper的代码
						 * if( "pdf".equalsIgnoreCase(doc.getDmSuffix())){//如果是pdf文件，则转为swf文件
							String destPathFile = targetFile.getPath().substring(0,(targetFile.getPath().length()-3))+"swf";
							String command = "D:\\Program Files (x86)\\SWFTools\\pdf2swf.exe" + " -t " + targetFile+ " -o "   
					                + destPathFile + " -s flashversion=9 -f ";
							
							Process process = Runtime.getRuntime().exec(command); // 调用外部程序   
							final InputStream is1 = process.getInputStream();   
							new Thread(new Runnable() {   
							    public void run() {   
							        BufferedReader br = new BufferedReader(new InputStreamReader(is1));    
							        try {
										while(br.readLine()!= null) ;
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}   
							    }   
							}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区   
							InputStream is2 = process.getErrorStream();   
							BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));    
							StringBuilder buf = new StringBuilder(); // 保存输出结果流   
							String line = null;   
							while((line = br2.readLine()) != null) buf.append(line); // 循环等待ffmpeg进程结束   
							
//							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
							while (br2.readLine() != null); 
							process.waitFor();
							Process pro = Runtime.getRuntime().exec(command);
							dealWith(pro);//处理输入流和出错流  
							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
							while (bufferedReader.readLine() != null);
							pro.waitFor();
						}*/
                        // 删除Attach记录
                        attach.setPrimaryKeys(new String[]{String.valueOf(attach.getId())});
                        //attachService.delete(attach, false);
                    } catch (CustomException e) {
                        e.printStackTrace();
                        ArchiveException ae = new ArchiveException();
                        ae.setLogMsg(MessageUtils.getMessage("JC_SYS_002"));
                        throw ae;
                    }/*//不要删这段注释，这是处理felxpaper的代码
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
                }
            }
        }

        return documents;
    }

    /**
     * 方法描述：删除选中的文件夹和文档
     *
     * @param ids Id数组字符串格式，如：#dir_1,#dir_2,#doc_1
     * @return 返回删除的对象
     * @throws ArchiveException
     * @author zhanglg
     * @version 2014年6月19日上午10:13:28
     * @see
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Folder deleteDirDocs(String ids, HttpServletRequest request)
            throws ArchiveException {
        String[] idsArray = ids.split(",");
        ArrayList<String> folderIds = new ArrayList<String>();
        ArrayList<String> docIds = new ArrayList<String>();
        Folder returnFolder = new Folder();
        returnFolder.setSubdirs(new ArrayList<Folder>());
        returnFolder.setDocuments(new ArrayList<Document>());
        // 分类要删除的对象Id
        for (String id : idsArray) {
            String[] item = id.split("_");
            if (item.length == 2 && "#dir".equals(item[0])) {
                folderIds.add(item[1]);

            } else if (item.length == 2 && "#doc".equals(item[0])) {
                docIds.add(item[1]);
            }
        }
        // 删除文件夹
        if (folderIds.size() > 0) {
            Folder folder = new Folder();
            String[] pks = new String[folderIds.size()];
            for (int i = 0; i < folderIds.size(); i++) {
                String pk = folderIds.get(i);
                pks[i] = pk;
            }
            folder.setPrimaryKeys(pks);
            folder.setModifyUser(SystemSecurityUtils.getUser().getId());
            folder.setModifyDate(new Date());
            folderDao.deleteDirToRecycle(folder);
            returnFolder.getSubdirs().add(folder);
        }
        // 删除文档
        if (docIds.size() > 0) {
            Document doc = new Document();
            String[] pks = new String[docIds.size()];
            for (int i = 0; i < docIds.size(); i++) {
                String pk = docIds.get(i);
                pks[i] = pk;

            }
            doc.setPrimaryKeys(pks);
            doc.setModifyDate(new Date());
            docService.deleteDocToRecycle(doc);
            try {
                Relate relate = new Relate();
                relate.setPrimaryKeys(pks);
                relateService.deleteRelateDM(relate);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error(e.getMessage());
                e.printStackTrace();
            }
            returnFolder.getDocuments().add(doc);
        }
        return returnFolder;
    }

    /**
     * 方法描述：回收站数据查询
     *
     * @param folder
     * @throws ArchiveException
     * @author weny
     * @version 2014年6月18日
     * @see
     */
    @Override
    public List<Recycle> selectRecycle() throws ArchiveException {
        List<Recycle> recycle = new ArrayList<Recycle>();
        Document document = new Document();
        document.setDmInRecycle(1);
        document.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
        document.setOrderBy("t.MODIFY_DATE DESC");
        Folder folder = new Folder();
        folder.setDmInRecycle(1);
        folder.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
        folder.setOrderBy("t.MODIFY_DATE DESC");
        List<Folder> folerRecycle = null;
        List<Document> documentRecycle = null;
        try {
            folerRecycle = this.queryAll(folder);
            documentRecycle = docService.queryAll(document);
            if (folerRecycle != null) {
                for (int i = 0; i < folerRecycle.size(); i++) {
                    Recycle recycleFolder = new Recycle();
                    Folder der = folerRecycle.get(i);
                    recycleFolder.setId(der.getId());
                    recycleFolder.setDmName(der.getFolderName());
                    recycleFolder.setContentType("0");
                    recycleFolder.setType("0");
                    recycleFolder.setDmSize("0");
                    recycleFolder.setDmSuffix("文件夹");
                    recycleFolder.setModifyDate(der.getModifyDate());
                    recycle.add(recycleFolder);
                }
            }
            if (documentRecycle != null) {
                for (int i = 0; i < documentRecycle.size(); i++) {
                    Recycle recycleFolder = new Recycle();
                    Document der = documentRecycle.get(i);
                    recycleFolder.setId(der.getId());
                    recycleFolder.setDmName(der.getDmName());
                    recycleFolder.setContentType(der.getContentType());
                    recycleFolder.setType("1");
                    recycleFolder.setDmSize(der.getDmSize());
                    recycleFolder.setModifyDate(der.getModifyDate());
                    recycleFolder.setDmSuffix(der.getDmSuffix());
                    recycle.add(recycleFolder);
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return recycle;
    }

    /**
     * 方法描述：回收站数据查询
     *
     * @param folder
     * @throws ArchiveException
     * @author weny
     * @version 2014年6月18日
     * @see
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer batchDelete(String id, String type) throws ArchiveException {
        Integer count = 0;
        try {
            if ("0".equals(type)) {
                Folder folder = new Folder();
                folder.setPrimaryKeys(id.split(","));
                count = this.delete(folder);
                String[] permissionId = id.split(",");
                for (int i = 0; i < permissionId.length; i++) {
                    Permission permission = new Permission();
                    permission.setFolderId(permissionId[i]);
                    List<Permission> list = permissionService
                            .queryAll(permission);
                    for (int j = 0; j < list.size(); j++) {
                        permissionService.delectPermission(list.get(j));
                    }
                }
            } else {
                Document document = new Document();
                document.setPrimaryKeys(id.split(","));
                count = docService.delete(document);
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return count;
    }

    /**
     * 方法描述：回收站数据还原
     *
     * @param folder
     * @throws ArchiveException
     * @author weny
     * @version 2014年6月26日
     * @see
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map<String, Object> batchRecycle(String id, String type)
            throws ArchiveException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // 文件夹还原
            if ("0".equals(type)) {
                Folder folder = new Folder();
                folder.setId(id);
                folder.setDeleteFlag(0);
                folder.setDmInRecycle(1);
                folder = this.get(folder);
                if (folder == null) {
                    resultMap.put("success", "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_023"));
                    return resultMap;
                }
                // check文件所在文件夹是否被删除
                if (checkDelectFolder(folder.getParentFolderId()) == 0) {
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_010"));
                    return resultMap;
                }
                // 文件夹还原重命名check
                // getNewFolderName();
                String dmName = getNewFolderName(folder.getParentFolderId(),
                        folder.getFolderName());
                if (dmName.length() > 64) {
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_026"));
                    return resultMap;
                }
                folder.setFolderName(dmName);
                folder.setDmInRecycle(0);
                folder.setModifyDateNew(new Date());
                folderDao.update(folder);
                resultMap.put("success", "true");
                resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils
                        .getMessage("JC_OA_ARCHIVE_012",
                                new String[]{dmName}));
                // 文档还原
            } else {
                Document document = new Document();
                document.setId(id);
                document.setDeleteFlag(0);
                document.setDmInRecycle(1);
                document.setFileType(Constants.ARC_DOC_FILETYPE_DOC);
                document = docService.get(document);
                if (document == null) {
                    resultMap.put("success", "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_023"));
                    return resultMap;
                }
                // check文件所在文件夹是否被删除
                if (checkDelectFolder(document.getFolderId()) == 0) {
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_010"));
                    return resultMap;
                }
                Permission permission = new Permission();
                permission.setFolderId(document.getFolderId());
                permission.setUserId(SystemSecurityUtils.getUser().getId());
                permission.setDeptId(SystemSecurityUtils.getUser().getDeptId());
                permission.setOrgId(SystemSecurityUtils.getUser().getOrgId());
                List<Permission> list = permissionService
                        .queryPermission(permission);
                Folder folderPermission = PermissionUtil.permissionValue(list);

                // 文档还原重命名check
                String dmName = docService.getNewFileName(
                        document.getFolderId(), document.getDmName(),
                        document.getDmSuffix(), Constants.ARC_DOC_FILETYPE_DOC);
                if (dmName.length() > 64) {
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_026"));
                    return resultMap;
                }
                if (document.getModel().intValue() == 1
                        || folderPermission.isPermNewUpDown()
                        || document.getCreateUser().equals(SystemSecurityUtils
                        .getUser().getId())) {
                    document.setDmName(dmName);
                    document.setDmInRecycle(0);
                    document.setModifyDateNew(new Date());
                    docService.update(document);
                    resultMap.put("success", "true");
                    resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_012",
                                    new String[]{dmName}));
                } else {
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,
                            MessageUtils.getMessage("JC_OA_ARCHIVE_011"));
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return resultMap;
    }

    // 查询文件夹是否被删除
    private Integer checkDelectFolder(String folderId) throws CustomException {
        Integer num = 0;
        Folder folderParent = new Folder();
        folderParent.setId(folderId);
        folderParent.setDeleteFlag(0);
        folderParent.setDmInRecycle(0);
        folderParent = this.get(folderParent);
        if (folderParent == null) {
            num = 0;
        } else if (folderParent.getParentFolderId().equals("0")) {
            num = 1;
        } else {
            return checkDelectFolder(folderParent.getParentFolderId());
        }
        return num;
    }

    // 文件夹还原重命名判断
    private String folderPermission(String folderName, String parentFolderId,
                                    String fileType) throws CustomException {
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setParentFolderId(parentFolderId);
        folder.setDmInRecycle(0);
        folder.setDeleteFlag(0);
        folder.setFolderType(fileType);
        List<Folder> permission = this.queryAll(folder);
        if (permission != null && permission.size() > 0) {
            return folderPermission(folderName + "(1)", parentFolderId,
                    fileType);
        }
        return folderName;
    }

    // 文档还原重命名判断
    private String documentPermission(String dmName, String folderId,
                                      String fileType, String dmSuffix) throws CustomException {
        Document document = new Document();
        document.setDmName(dmName);
        document.setFolderId(folderId);
        document.setDmInRecycle(0);
        document.setDeleteFlag(0);
        document.setFileType(fileType);
        document.setDmSuffix(dmSuffix);
        List<Document> permission = docService.queryAll(document);
        if (permission != null && permission.size() > 0) {
            return documentPermission(dmName + "(1)", folderId, fileType,
                    dmSuffix);
        }
        return dmName;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public synchronized Document copyDocTo(Document document, Folder folder,
                                           HttpServletRequest request) throws ArchiveException {
        Document newDoc = new Document();
        try {
            // 查询原文档
            // Document oldDoc = new Document();
            // oldDoc.setId(document.getId());
            // oldDoc = docService.get(oldDoc);
            // 生成文件名
            Calendar calendar = Calendar.getInstance();
            String fileName = String.valueOf(calendar.getTimeInMillis()) + "-"
                    + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + "-"
                    + String.valueOf(calendar.get(Calendar.MINUTE)) + "-"
                    + String.valueOf(calendar.get(Calendar.SECOND)) + "."
                    + document.getDmSuffix();
            // 设置新文档
            newDoc.setFileType(document.getFileType());
            newDoc.setContentType(document.getContentType());
            newDoc.setDmName(document.getDmName());
            newDoc.setDmAlias(fileName);
            newDoc.setDmLockStatus(document.getDmLockStatus());
            newDoc.setDmSize(document.getDmSize());
            newDoc.setDmSuffix(document.getDmSuffix());
            newDoc.setDmInRecycle(document.getDmInRecycle());
            newDoc.setModel(document.getModel());
            // 查询目标目录
            // Folder folder = new Folder();
            // folder.setId(document.getFolderId());
            // folder = this.get(folder);

            // 生成新文件名
            newDoc.setDmName(docService.getNewFileName(folder.getId(),
                    document.getDmName(), document.getDmSuffix()));
            newDoc.setId(null);
            Document dt = new Document();
            dt.setModel(document.getModel());
            newDoc.setSeq(docService.getSeq(dt));
            // 增加版本号
            Version v = versionService.createVersion(newDoc);
            newDoc.setCurrentVersion(v.getCurrentVersion());
            v.setVersionDesc("复制文档(" + newDoc.getDmName() + ")版本号："
                    + newDoc.getCurrentVersion());

            newDoc.setFolderId(folder.getId());
            newDoc.setDmDir(folder.getFolderPath());
            newDoc.setPhysicalPath(Constants.DJ_UPLOAD_DIR + File.separator
                    + fileName);
            // 复制File
            FileUtil.copyFile(
                    this.getAbsoluteContextPath(request)
                            + document.getPhysicalPath(),
                    this.getAbsoluteParentPath(request) + fileName);

            // 保存文件记录

            docService.save(newDoc);
            v.setBackUpId(newDoc.getId());
            v.setIsCurrentUsed(1);
            versionService.save(v);

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
        }

        return newDoc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Document cutDocTo(Document document) throws ArchiveException {

        // 查询原文档
        Document oldDoc = new Document();
        try {
            oldDoc.setId(document.getId());
            oldDoc = docService.get(oldDoc);

            // 查询目标目录
            Folder folder = new Folder();
            folder.setId(document.getFolderId());
            folder = this.get(folder);
            // 生成新文件名
            Document d = new Document();
            d.setFolderId(document.getFolderId());
            d.setDmName(oldDoc.getDmName());
            d.setDmSuffix(oldDoc.getDmSuffix());
            d.setModel(oldDoc.getModel());
            d.setFileType(oldDoc.getFileType());

            oldDoc.setDmName(docService.getNewFileName(d, oldDoc.getDmName()));

            oldDoc.setFolderId(document.getFolderId());
            oldDoc.setPhysicalPath(Constants.DJ_UPLOAD_DIR + File.separator
                    + oldDoc.getDmAlias());
            // 保存文件记录
            docService.update(oldDoc);
            // 更新附件表记录中的文件名信息
            Attach attach = new Attach();
            attach.setBusinessIdArray(new String[]{oldDoc.getId().toString()});
            List<Attach> attachs = attachService
                    .queryAttachByBusinessIds(attach);
            if (attachs != null && attachs.size() > 0) {
                attach = attachs.get(0);
                attach.setFileName(oldDoc.getDmName());
                attachService.update(attach);
            }

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
        }
        return oldDoc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public synchronized Folder copyDirTo(Folder sourceFolder,
                                         Folder targetFolder, HttpServletRequest request)
            throws ArchiveException {

        Folder newFolder = new Folder();
        try {
            // // 查询源目录信息
            // Folder sourceFolder = new Folder();
            // sourceFolder.setId(folder.getId());
            // sourceFolder = this.get(sourceFolder);
            // // 查询目标目录
            // Folder targetFolder = new Folder();
            // targetFolder.setId(folder.getParentFolderId());
            // targetFolder = this.get(targetFolder);

            newFolder = sourceFolder.clone();
            newFolder.setId(null);
            newFolder.setParentFolderId(targetFolder.getId());
            // 生成新目录名
            newFolder.setFolderName(this.getNewFolderName(targetFolder.getId(),
                    sourceFolder.getFolderName()));
            // 保存新目录
            newFolder.setFolderPath(targetFolder.getFolderPath() + "/"
                    + newFolder.getFolderName());
            this.save(newFolder);

            // 递归生成新目录树结构、并逐级保存
            this.recursionCopyFolder(sourceFolder, newFolder, request);
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
        }
        return newFolder;
    }

    /**
     * 方法描述：递归复制目录 用于复制下级目录到一个新目录，不做重名的校验
     *
     * @throws CustomException
     * @throws CloneNotSupportedException
     * @author zhangligang
     * @version 2014年6月26日上午10:30:46
     * @see
     */
    private void recursionCopyFolder(Folder source, Folder target,
                                     HttpServletRequest request) throws CustomException,
            CloneNotSupportedException {
        // 复制目录下文档
        Document doc = new Document();
        doc.setFolderId(source.getId());
        List<Document> documents = docService.queryAll(doc);
        for (Document document : documents) {
            document.setFolderId(target.getId());
            this.copyDocTo(document, target, request);
        }
        // 复制目录下目录
        Folder folder = new Folder();
        folder.setParentFolderId(source.getId());
        List<Folder> folders = this.queryAll(folder);
        for (Folder subFolder : folders) {
            Folder newFolder = subFolder.clone();
            newFolder.setParentFolderId(target.getId());
            newFolder.setFolderPath(target.getFolderPath() + "/"
                    + newFolder.getFolderName());
            this.save(newFolder);
            this.recursionCopyFolder(subFolder, newFolder, request);
        }
        return;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public synchronized Folder cutDirTo(Folder sourcefolder, Folder targetfolder)
            throws ArchiveException {
        // 查询源目录信息
        Folder oldFolder = sourcefolder;
        try {
            // oldFolder.setId(folder.getId());
            // oldFolder = this.get(oldFolder);

            // Folder parentFolder = new Folder();
            // parentFolder.setId(folder.getParentFolderId());
            // parentFolder = this.get(parentFolder);
            oldFolder.setParentFolderId(targetfolder.getId());
            // 生成新目录名
            oldFolder.setFolderName(this.getNewFolderName(targetfolder.getId(),
                    oldFolder.getFolderName()));
            oldFolder.setOldFolderPath(oldFolder.getFolderPath() + "/");
            oldFolder.setFolderPath(targetfolder.getFolderPath() + "/"
                    + oldFolder.getFolderName());
            // 更新目录
            this.update(oldFolder);

        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
        }
        return oldFolder;
    }

    @Override
    public Document getDocument(Document doc) throws ArchiveException {
        Document document = null;
        try {
            document = docService.get(doc);
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return document;
    }

    @Override
    public String getNewFolderName(String parentFolderId, String name)
            throws ArchiveException {
        String newName = name;
        Folder targetFolder = new Folder();
        targetFolder.setParentFolderId(parentFolderId);
        targetFolder.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
        targetFolder.setDeleteFlag(0);
        List<Folder> folders;
        try {
            // 查询父文件夹下所有文件
            folders = this.queryAll(targetFolder);

            if (folders != null) {
                Map<String, Folder> map = new HashMap<String, Folder>();
                for (Folder f : folders) {
                    map.put(f.getFolderName(), f);
                }
                // 文件夹名称如果重复，生成新名称
                for (int i = 0; map.get(newName) != null; i++) {
                    newName = name + "(" + (i + 1) + ")";
                    // newName += "(1)";
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return newName;
    }

    /**
     * 方法描述：取得项目下文档模块（兼容点聚）绝对路径（不包含文件名）
     *
     * @param request
     * @return
     * @author zhangligang
     * @version 2014年7月2日下午2:48:41
     * @see
     */
    @Override
    public String getAbsoluteParentPath(HttpServletRequest request) {
        return GlobalContext.getProperty("gxwj") + File.separator
                + Constants.DJ_UPLOAD_DIR + File.separator;
    }

    /**
     * 方法描述：取得项目上下文绝对路径
     *
     * @param request
     * @return
     * @author zhangligang
     * @version 2014年7月2日下午3:02:50
     * @see
     */
    @Override
    public String getAbsoluteContextPath(HttpServletRequest request) {

        return GlobalContext.getProperty("gxwj");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer update(Folder folder) throws CustomException {

        String path = folder.getFolderPath().substring(0,
                folder.getFolderPath().lastIndexOf("/") + 1);
        folder.setFolderPath(path + folder.getFolderName());
        Integer i = super.update(folder);
        if (i > 0) {
            folder.setFolderPath(folder.getFolderPath() + "/");
            folderDao.updateAllChildPath(folder);
        }
        return i;
    }

    /**
     * @param Folder folder 实体类
     * @return List<Folder>
     * @throws Exception
     * @description 权限过滤文件夹
     * @author weny
     * @version 2014-07-09
     */
    @Override
    public List<Folder> getFolderPermission(Folder folder) {
        return folderDao.getFolderPermission(folder);
    }

    @Override
    public String getNewFileName(String folderId, String dmName, String ext)
            throws ArchiveException {

        return docService.getNewFileName(folderId, dmName, ext);
    }

    /**
     * 方法描述：回收站清空
     *
     * @param folder
     * @throws CustomException
     * @author weny
     * @version 2014年6月18日
     * @see
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int clearRecycl() throws CustomException {
        Document document = new Document();
        document.setDmInRecycle(1);
        document.setDeleteFlag(0);
        List<Document> doc = docService.queryAll(document);
        int count = 0;
        for (int i = 0; i < doc.size(); i++) {
            Document ment = doc.get(i);
            ment.setDeleteFlag(1);
            count = docService.update(ment);
        }
        Folder folder = new Folder();
        folder.setDmInRecycle(1);
        folder.setDeleteFlag(0);
        List<Folder> fol = this.queryAll(folder);
        for (int i = 0; i < fol.size(); i++) {
            Folder der = fol.get(i);
            der.setDeleteFlag(1);
            count = folderDao.update(der);
        }
        return count;
    }

    /**
     * 方法描述：批量删除我的文档文件夹和文档
     *
     * @param folder
     * @throws ArchiveException
     * @author
     * @version 2014年6月18日
     * @see
     */
    @Override
    public Integer bulkDelete(String idsdir, String idsdoc) throws ArchiveException {
        // TODO Auto-generated method stub
        Integer count = 0;
        try {
            if (idsdir != null && !idsdir.equals("")) {
                Folder folder = new Folder();
                folder.setPrimaryKeys(idsdir.split(","));
                count = count + this.delete(folder);
                String[] permissionId = idsdir.split(",");
                for (int i = 0; i < permissionId.length; i++) {
                    Permission permission = new Permission();
                    permission.setFolderId(permissionId[i]);
                    List<Permission> list = permissionService
                            .queryAll(permission);
                    for (int j = 0; j < list.size(); j++) {
                        permissionService.delectPermission(list.get(j));
                    }
                }
            }
            if (idsdoc != null && !idsdoc.equals("")) {
                Document document = new Document();
                document.setPrimaryKeys(idsdoc.split(","));
                //Relate relate = new Relate();
                //relate.setPrimaryKeys(idsdoc.split(","));
                //relateService.deleteRelateDM(relate);
                count = count + docService.delete(document);
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return count;
    }

    /**
     * 方法描述：获取归档目录下是否是机构管理员列表信息
     *
     * @param folder
     * @return
     * @throws ArchiveException
     * @author
     * @version
     * @see
     */
    @Override
    public Folder getFileDirDocsQuery(Folder folder) throws ArchiveException {
        folder.setCreateUser(null);
        // 如果参数中没有文件夹ID，既为查看根目录，应设置上级目录ID为0，做为查询条件
        if (folder.getId() == null) {
            folder.setParentFolderId("0");
            folder.setFolderName("根目录");
            folder.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
        }
        try {
            folder = this.get(folder);
            if (folder == null) {
                return null;
            }
            AdminSide adminside = new AdminSide();
            adminside.setUserId(SystemSecurityUtils.getUser().getId());
            adminside.setIsChecked("1");
            adminside.setDeptId(folder.getCreateUserOrg());
            List<AdminSide> as = adminSideService.queryAll(adminside);
            if (as != null && as.size() > 0) {
                // 查询直接子目录
                Folder subdir = new Folder();
                subdir.setParentFolderId(folder.getId());
                subdir.setFolderType(folder.getFolderType());
                subdir.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
                subdir.setCreateUserOrg(SystemSecurityUtils.getUser()
                        .getOrgId());
                subdir.setDeleteFlag(0);
                subdir.addOrderByFieldDesc("t.MODIFY_DATE");
                folder.setSubdirs(folderDao.queryAll(subdir));

                // 查询当前目录下文件
                Document doc = new Document();
                doc.setFolderId(folder.getId());
                doc.setDmInRecycle(Constants.ARC_DM_IN_RECYCLE_NO);
                doc.setDeleteFlag(0);
                if ("0".equals(folder.getFolderType())) {
                    doc.setFileType(folder.getFolderType());
                }
                doc.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
                doc.addOrderByFieldDesc("t.MODIFY_DATE");
                folder.setDocuments(docService.queryAll(doc));
            } else {
                return null;
            }
        } catch (CustomException e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            ArchiveException ae = new ArchiveException();
            ae.setLogMsg(MessageUtils.getMessage("JC_SYS_055"));
            throw ae;
        }
        return folder;
    }
	
	/*//不要删这段注释，这是处理felxpaper的代码
	 * private void dealWith(final Process pro){  
	        // 下面是处理堵塞的情况  
	        try {  
	            new Thread(){  
	                public void run(){  
	                    BufferedReader br1 = new BufferedReader(new InputStreamReader(pro.getInputStream()));  
	                    String text;  
	                    try {  
	                        while ( (text = br1.readLine()) != null) {  
	                            System.out.println(text);  
	                        }  
	                    } catch (IOException e) {  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }.start();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	          
	        try {  
	            new Thread(){  
	                public void run(){  
	                    BufferedReader br2 = new BufferedReader(new InputStreamReader(pro.getErrorStream()));//这定不要忘记处理出理时产生的信息，不然会堵塞不前的  
	                    String text;  
	                    try {  
	                        while( (text = br2.readLine()) != null){  
	                            System.err.println(text);  
	                        }  
	                    } catch (IOException e) {  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }.start();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  */


}