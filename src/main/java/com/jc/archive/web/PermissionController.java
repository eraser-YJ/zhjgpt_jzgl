package com.jc.archive.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
import com.jc.archive.domain.Folder;
import com.jc.archive.domain.Permission;
import com.jc.archive.domain.SubPermission;
import com.jc.archive.domain.validator.PermissionValidator;
import com.jc.archive.service.IArchiveFolderService;
import com.jc.archive.service.IAudithisService;
import com.jc.archive.service.IPermissionService;
import com.jc.archive.util.PermissionUtil;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.Constants;
import com.jc.system.security.SystemSecurityUtils;


/**
 * @title  GOA2.5源代码
 * @description  controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 闻瑜
 * @version  2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/permission")
public class PermissionController extends BaseController{
	
	@Autowired
	private IPermissionService permissionService;

	@Autowired
	private IArchiveFolderService archiveFolderService;

	@Autowired
	private IAudithisService audithisService;
	
	@org.springframework.web.bind.annotation.InitBinder("permission")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new PermissionValidator()); 
    }
	
	public PermissionController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param Permission permission 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05 
	 * @throws Exception 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="manageList",operateDescribe="查询文档权限信息")
	public PageManager manageList(Permission permission,PageManager page,String folderId,HttpServletRequest request) throws Exception{
		permission.setOrderBy("t.CREATE_DATE DESC");
		permission.setFolderId(folderId);
		permission.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		PageManager page_ = permissionService.manageListPermission(permission, page);
		return page_; 
	}

	/**
	* @deprecated 跳转方法
	* @param model
	* @param folderId 文件夹id
	* @param request
	* @return String 跳转的路径
	* @throws Exception
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="文档权限",operateFuncNm="manage",operateDescribe="文档权限跳转页面")
	public String manage(Model model,String folderId,HttpServletRequest request) throws Exception{
		model.addAttribute("folderId",folderId);
		return "archive/permission/permission"; 
	}

/**
	 * @deprecated 删除方法
	 * @param Permission permission 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="deleteByIds",operateDescribe="删除文档权限")
	public Integer deleteByIds(Permission permission,String ids,HttpServletRequest request) throws Exception{
		permission.setPrimaryKeys(ids.split(","));
		return permissionService.delete(permission);
	}

	/**
	 * @deprecated 保存方法
	 * @param Permission permission 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="save",operateDescribe="新增文档权限")
	public Map<String,Object> save(@Valid Permission permission,BindingResult result,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get("success"))){
			permissionService.save(permission);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param Permission permission 实体类
	* @param result 验证bean
	* @param request
	* @return Map 更新结构
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="update",operateDescribe="更新文档权限")
	public Map<String, Object> update(Permission permission, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = permissionService.update(permission);
		if (flag == 1) {
			resultMap.put("success", "true");
		} else {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
					MessageUtils.getMessage("JC_SYS_002"));
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param Permission permission 实体类
	 * @return Permission 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="get",operateDescribe="文档权限单条信息查询")
	public Permission get(Permission permission,HttpServletRequest request) throws Exception{
		return permissionService.get(permission);
	}

	/**
	 * @deprecated 删除方法
	 * @param Permission permission 实体类
	 * @param String ids 删除的多个主键
	 * @return Map 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="batchDelete.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="batchDelete",operateDescribe="删除文档权限")
	public Map<String, Object> batchDelete(Permission permission, String id,
										   String forderId, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Folder oldFolder = new Folder();
		oldFolder.setId(forderId);
		oldFolder.setDeleteFlag(0);
		oldFolder.setDmInRecycle(0);
		Folder folders = archiveFolderService.get(oldFolder);
		if (folders == null) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
			return resultMap;
		} else {
			permission.setId(id);
			Integer count = permissionService.delectPermission(permission);
			if (count == 1) {
				resultMap.put("success", "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
						MessageUtils.getMessage("JC_SYS_005"));
			}
			/*
			 * Folder folder = new Folder();
			 * folder.setId(Long.valueOf(forderId)); Folder fol =
			 * archiveFolderService.get(folder);
			 */
			audithisService.audithis(request, forderId,
					folders.getFolderName(),
					Constants.ARC_AUDITHIS_DATATYPE_DIR,
					Constants.ARC_AUDITHIS_AUTH, "文件夹权限的删除");
			return resultMap;
		}
	}

	/**
	 * @deprecated 更新权限
	 * @param permissionValue 权限值
	 * @param id  权限id
	 * @param num 操作标记
	 * @param forderId 文件夹id
	 * @param request
	 * @return Map 成功删除
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="batchUpdate.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="batchUpdate",operateDescribe="修改文档权限")
	public Map<String, Object> batchUpdate(String permissionValue,String id,String num,String forderId,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Folder oldFolder = new Folder();
		oldFolder.setId(forderId);
		oldFolder.setDeleteFlag(0);
		oldFolder.setDmInRecycle(0);
		Folder folders = archiveFolderService.get(oldFolder);
		if (folders != null) {
			Permission permission = new Permission();
			permission.setId(id);
			permission = permissionService.get(permission);
			if (permission == null) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_036"));
				return resultMap;
			}
			permission.setPermissionValue(PermissionUtil.permission(permission.getPermissionValue(), permissionValue, num));
			if (num.equals("1")) {
				if (permission.getPermissionValue().subSequence(0, 1).equals("0")) {
					permission.setPermissionValue("0000000000");
				} else if (permission.getPermissionValue().subSequence(0, 1).equals("1")
						&& !permission.getPermissionValue().subSequence(0, 1).equals(permissionValue.substring(0, 1))) {
					permission.setPermissionValue("1111000000");
				}
			}
			Integer count = permissionService.update(permission);
			if (count == 1) {
				resultMap.put("success", "true");
			}
			/*
			 * Folder folder = new Folder();
			 * folder.setId(Long.valueOf(folderId)); Folder fol =
			 * archiveFolderService.get(folder);
			 */
			audithisService.audithis(request, forderId,
					folders.getFolderName(),
					Constants.ARC_AUDITHIS_DATATYPE_DIR,
					Constants.ARC_AUDITHIS_AUTH,
					"文件夹" + PermissionUtil.permissionType(num) + "权限的修改");
			return resultMap;
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
					MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
			return resultMap;
		}
	}
	
	/**
	 * @deprecated 添加组织人员权限方法
	 * @param folderId 文件夹id
	 * @param id 使用人员id
	 * @param text 使用人员姓名
	 * @param type 数据类型
	 * @param request
	 * @throws Exception
	 * @author 盖旭 
	 * @version  2014-06-20
	 */
	@RequestMapping(value="batchInsert.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="batchInsert",operateDescribe="新增组织人员权限信息")
	public Map<String, Object> batchInsert(String folderId, String id, String text, String type,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Folder oldFolder = new Folder();
		oldFolder.setId(folderId);
		oldFolder.setDeleteFlag(0);
		oldFolder.setDmInRecycle(0);
		Folder folders = archiveFolderService.get(oldFolder);
        if(folders != null){
		Integer count = permissionService.batchSave(folderId, id, text, type);
		if (count == 1) {
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		audithisService.audithis(request, folderId,folders.getFolderName(), Constants.ARC_AUDITHIS_DATATYPE_DIR, Constants.ARC_AUDITHIS_AUTH, "文件夹权限的新增");
		return resultMap;
	 }else{
		 resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
 		 resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
		return resultMap;
	 }
	}

	/**
	 * @deprecated 修改组织人员权限方法
	 * @param id 权限id
	 * @param request
	 * @throws Exception
	 * @author 闻瑜
	 * @version  2014-06-20
	 */
	@RequestMapping(value="updatePermission.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="batchInsert",operateDescribe="修改组织人员权限信息")
	public Map<String, Object> updatePermission(String id,
			HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Permission pm = new Permission();
		pm.setId(id);
		pm = permissionService.get(pm);
		if (pm == null) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_036"));
			return resultMap;
		} else {
			Folder oldFolder = new Folder();
			oldFolder.setId(pm.getFolderId());
			oldFolder.setDeleteFlag(0);
			oldFolder.setDmInRecycle(0);
			Folder folders = archiveFolderService.get(oldFolder);
			if (folders == null) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_034"));
				return resultMap;
			} else {
				List<SubPermission> subPermission = permissionService.updatePermission(id);
				resultMap.put("success", "true");
				resultMap.put("subPermission", subPermission);
				return resultMap;
			}
		}
	}
	/**
	 * @deprecated 添加组织人员权限方法
	 * @param permissionId 权限id
	 * @param id 人员id
	 * @param text 人员姓名
	 * @param type 数据类型
	 * @param forderId 文件夹id
	 * @param request
	 * @throws Exception
	 * @author 闻瑜
	 * @version  2014-06-20
	 */
	@RequestMapping(value="batchPermission.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="batchPermission",operateDescribe="新增组织人员权限信息")
	public Map<String, Object> batchPermission(String permissionId, String id, String text, String type,String forderId,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Permission pm = new Permission();
		try{
			pm.setId(permissionId);
			pm = permissionService.get(pm);
			if (pm == null) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "error");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_ARCHIVE_036"));
				return resultMap;
			} else {
				Integer count = permissionService.updateBatchPermission(permissionId, id, text, type, forderId);
				if (count > 0) {
					resultMap.put("success", "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
				}
				Folder folder = new Folder();
				folder.setId(forderId);
				Folder fol = archiveFolderService.get(folder);
				audithisService.audithis(request, forderId, fol.getFolderName(),
						Constants.ARC_AUDITHIS_DATATYPE_DIR,
						Constants.ARC_AUDITHIS_AUTH, "文件夹权限的修改");
				return resultMap;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * @deprecated 权限查询
	 * @param folderId 文件夹id
	 * @param request
	 * @throws Exception
	 * @author 闻瑜
	 * @version  2014-06-23
	 */
	@RequestMapping(value="queryPermission.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="queryPermission",operateDescribe="查询文档权限信息")
	public Map<String, Object> queryPermission(String folderId,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Permission permission = new Permission();
		permission.setFolderId(folderId);
		permission.setUserId(SystemSecurityUtils.getUser().getId());
		permission.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		List<Permission> list = permissionService.queryPermission(permission);
		Folder folder = PermissionUtil.permissionValue(list);
		resultMap.put("folder", folder);
		return resultMap;
	}
	/**
	 * @deprecated 权限查询
	 * @param folderId 文件夹id
	 * @param request
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-23
	 */
	@RequestMapping(value="queryPermissionByFolder.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档权限",operateFuncNm="queryPermission",operateDescribe="查询文档权限信息")
	public boolean queryPermissionByFolder(String folderId,HttpServletRequest request) throws Exception{
		Permission permission = new Permission();
		permission.setFolderId(folderId);
		permission.setUserId(SystemSecurityUtils.getUser().getId());
		permission.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		permission.setOrgId(SystemSecurityUtils.getUser().getOrgId());
		List<Permission> list = permissionService.queryPermission(permission);
		return list == null || list.size() < 1;
	}
}