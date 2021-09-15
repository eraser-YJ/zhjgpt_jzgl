package com.jc.archive.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import com.jc.archive.domain.SubPermission;
import com.jc.archive.domain.validator.SubPermissionValidator;
import com.jc.archive.service.ISubPermissionService;
import com.jc.system.applog.ActionLog;


/**
 * @title  GOA2.5源代码
 * @description  controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version  2014-06-19
 */
 
@Controller
@RequestMapping(value="/archive/subPermission")
public class SubPermissionController extends BaseController{
	
	@Autowired
	private ISubPermissionService subPermissionService;
	
	@org.springframework.web.bind.annotation.InitBinder("subPermission")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SubPermissionValidator()); 
    }
	
	public SubPermissionController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param SubPermission subPermission 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-19 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="manageList",operateDescribe="查询权限关联信息")
	public PageManager manageList(SubPermission subPermission,PageManager page,HttpServletRequest request){
		PageManager page_ = subPermissionService.query(subPermission, page);
		return page_; 
	}

	/**
	* @deprecated 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author 盖旭
	* @version  2014-06-19 
	*/
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="文档管理",operateFuncNm="manage",operateDescribe="权限关联跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/subPermission/subPermission1"; 
	}

/**
	 * @deprecated 删除方法
	 * @param SubPermission subPermission 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-19
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="deleteByIds",operateDescribe="删除权限关联信息")
	public Integer deleteByIds(SubPermission subPermission,String ids,HttpServletRequest request) throws Exception{
		subPermission.setPrimaryKeys(ids.split(","));
		return subPermissionService.delete(subPermission);
	}

	/**
	 * @deprecated 保存方法
	 * @param SubPermission subPermission 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-19
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="save",operateDescribe="新增权限关联信息")
	public Map<String,Object> save(@Valid SubPermission subPermission,BindingResult result,
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
			subPermissionService.save(subPermission);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param SubPermission subPermission 实体类
	* @return Map 更新结果
	* @author 盖旭
	* @version  2014-06-19 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="update",operateDescribe="更新权限关联信息")
	public Map<String, Object> update(SubPermission subPermission, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = subPermissionService.update(subPermission);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param SubPermission subPermission 实体类
	 * @return SubPermission 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-19
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="get",operateDescribe="查询权限关联单条信息")
	public SubPermission get(SubPermission subPermission,HttpServletRequest request) throws Exception{
		return subPermissionService.get(subPermission);
	}

}