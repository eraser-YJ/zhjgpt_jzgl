package com.jc.sys.web;

import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.util.*;
import com.jc.sys.service.ISubDelService;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.WebDataBinder;


import com.jc.foundation.exception.CustomException;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.applog.ActionLog;

import com.jc.sys.domain.SubDepartment;
import com.jc.sys.domain.validator.SubDepartmentValidator;
import com.jc.sys.service.ISubDepartmentService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value="/sys/subDepartment")
public class SubDepartmentController extends BaseController{

	private static final String SUCCESS = "success";
	
	@Autowired
	private ISubDepartmentService subDepartmentService;

	@Autowired
	private ISubDelService subDelService;
	
	@org.springframework.web.bind.annotation.InitBinder("subDepartment")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SubDepartmentValidator()); 

    }
	
	public SubDepartmentController() {
	}

	/**
	 * @description 保存方法
	 * @param  subDepartment 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="save",operateDescribe="对自定义部门管理进行新增操作")
	public Map<String,Object> save(@Valid SubDepartment subDepartment,BindingResult result,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request,subDepartment.getDeptToken());
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get(SUCCESS))){

			User userInfo = SystemSecurityUtils.getUser();// 获取登录用户信息

			Department deptcenter = DeptCacheUtil.getDeptById(subDepartment.getId());
			if (deptcenter != null){
				subDepartment.setDeptType(deptcenter.getDeptType());
				subDepartment.setName(deptcenter.getName());
			} else {
				resultMap.put("errorMessage", "组织类型不明确");
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				return resultMap;
			}

			// 校验同一级部门或机构名称是否存在
			SubDepartment dept = new SubDepartment();
			dept.setOrganizationId(subDepartment.getId());
			if (subDepartmentService.get(dept) != null) {
				resultMap.put("errorMessage", "组织已存在");
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				return resultMap;
			}

			subDepartment.setDeleteFlag(0);
			subDepartment.setCreateDate(DateUtils.getSysDate());
			subDepartment.setCreateUser(userInfo.getCreateUser());
			subDepartment.setModifyUser(userInfo.getCreateUser());
			subDepartment.setOrganizationId(subDepartment.getId());
			subDepartment.setModifyDate(DateUtils.getSysDate());
			if(subDepartment.getQueue() == null){
				subDepartment.setQueue(50);
			}
			if (subDepartmentService.save(subDepartment) >= 1) {
				resultMap.put(SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				SubDepartment subDepartment2 = subDepartmentService.queryOne(subDepartment);
				resultMap.put("dept", subDepartment2);
			} else{
				resultMap.put(SUCCESS, "false");
			}
		}
		return resultMap;
	}

	/**
	* workflowParamTemp修改方法
	* @param  subDepartment 实体类
	* @param  result 校验结果
	* @return Integer 更新的数目
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="update",operateDescribe="对自定义部门管理进行更新操作")
	public Map<String, Object> update(SubDepartment subDepartment, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = subDepartmentService.update(subDepartment);
		if (flag == 1) {
			resultMap.put("dept", subDepartment);
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  subDepartment 实体类
	 * @return SubDepartment 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="get",operateDescribe="对自定义部门管理进行单条查询操作")
	public SubDepartment get(SubDepartment subDepartment,HttpServletRequest request) throws Exception{
		return subDepartmentService.get(subDepartment);
	}

	/**
	 * 显示添加层
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version 2018-04-04
	 */
	@RequestMapping(value = "showDeptInsertHtml.action",method=RequestMethod.GET)
	public String showDeptInsertHtml(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("token", getToken(request));
		return "sys/subDepartment/subDepartmentInsert";
	}

	/**
	 * 显示编辑层
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version 2018-04-04
	 */
	@RequestMapping(value = "showDeptEditHtml.action",method=RequestMethod.GET)
	public String showDeptEditHtml(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("token", getToken(request));
		return "sys/subDepartment/subDepartmentEdit";
	}

	/**
	 * 获取部门单条记录方法(带父节点部门)
	 * @param department
	 * @return
	 * @throws Exception
	 * @author 张继伟
	 * @version 1.0 2014年4月16日 下午4:08:33
	 */
	@RequestMapping(value = "queryOne.action",method=RequestMethod.POST)
	@ResponseBody
	public SubDepartment queryOne(SubDepartment department) throws Exception {
		SubDepartment reDepartment = subDepartmentService.queryOne(department);
		return reDepartment;
	}

	/**
	 * @description 分页查询方法
	 * @param  subDepartment 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-04-04 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubDepartment subDepartment,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(subDepartment.getOrderBy())) {
			subDepartment.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = subDepartmentService.query(subDepartment, page);
		return rePage;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="manage",operateDescribe="对自定义部门管理进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/subDepartment/subDepartmentList"; 
	}

/**
	 * @description 删除方法
	 * @param  subDepartment 实体类
	 * @param  subDepartment 实体类
	 * @param  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="deleteByIds",operateDescribe="对自定义部门管理进行删除")
	public  Map<String, Object> deleteByIds(SubDepartment subDepartment,String ids, HttpServletRequest request) throws Exception{
		subDepartment.setPrimaryKeys(ids.split(","));
		Map<String, Object> resultMap = subDepartmentService.deleteByIds(subDepartment);
		return resultMap;
	}

	/**
	 * @description 删除方法
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="copyDeptTree.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门管理",operateFuncNm="copyDeptTree",operateDescribe="中心部门数据到子系统")
	public  Map<String, Object> copyDeptTree(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = null;
		User userInfo = SystemSecurityUtils.getUser();// 获取登录用户信息
		if(userInfo.getIsSystemAdmin() || userInfo.getIsAdmin() == 1){
			resultMap = subDelService.copyDeptTree();
		}
		return resultMap;
	}

}