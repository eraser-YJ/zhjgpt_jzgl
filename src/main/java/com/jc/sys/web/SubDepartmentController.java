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
	 * @description ????????????
	 * @param  subDepartment ?????????
	 * @param  result ????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="save",operateDescribe="??????????????????????????????????????????")
	public Map<String,Object> save(@Valid SubDepartment subDepartment,BindingResult result,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// ??????token
		resultMap = validateToken(request,subDepartment.getDeptToken());
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get(SUCCESS))){

			User userInfo = SystemSecurityUtils.getUser();// ????????????????????????

			Department deptcenter = DeptCacheUtil.getDeptById(subDepartment.getId());
			if (deptcenter != null){
				subDepartment.setDeptType(deptcenter.getDeptType());
				subDepartment.setName(deptcenter.getName());
			} else {
				resultMap.put("errorMessage", "?????????????????????");
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				return resultMap;
			}

			// ????????????????????????????????????????????????
			SubDepartment dept = new SubDepartment();
			dept.setOrganizationId(subDepartment.getId());
			if (subDepartmentService.get(dept) != null) {
				resultMap.put("errorMessage", "???????????????");
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
	* workflowParamTemp????????????
	* @param  subDepartment ?????????
	* @param  result ????????????
	* @return Integer ???????????????
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="update",operateDescribe="??????????????????????????????????????????")
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
	 * @description ????????????????????????
	 * @param  subDepartment ?????????
	 * @return SubDepartment ????????????
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="get",operateDescribe="????????????????????????????????????????????????")
	public SubDepartment get(SubDepartment subDepartment,HttpServletRequest request) throws Exception{
		return subDepartmentService.get(subDepartment);
	}

	/**
	 * ???????????????
	 * @return String ???????????????
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
	 * ???????????????
	 * @return String ???????????????
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
	 * ??????????????????????????????(??????????????????)
	 * @param department
	 * @return
	 * @throws Exception
	 * @author ?????????
	 * @version 1.0 2014???4???16??? ??????4:08:33
	 */
	@RequestMapping(value = "queryOne.action",method=RequestMethod.POST)
	@ResponseBody
	public SubDepartment queryOne(SubDepartment department) throws Exception {
		SubDepartment reDepartment = subDepartmentService.queryOne(department);
		return reDepartment;
	}

	/**
	 * @description ??????????????????
	 * @param  subDepartment ?????????
	 * @param  page ???????????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version  2018-04-04 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubDepartment subDepartment,PageManager page){
		//????????????
		if(StringUtil.isEmpty(subDepartment.getOrderBy())) {
			subDepartment.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = subDepartmentService.query(subDepartment, page);
		return rePage;
	}

	/**
	* @description ????????????
	* @return String ???????????????
	* @throws Exception
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="manage",operateDescribe="??????????????????????????????????????????")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/subDepartment/subDepartmentList"; 
	}

/**
	 * @description ????????????
	 * @param  subDepartment ?????????
	 * @param  subDepartment ?????????
	 * @param  ids ?????????????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="deleteByIds",operateDescribe="????????????????????????????????????")
	public  Map<String, Object> deleteByIds(SubDepartment subDepartment,String ids, HttpServletRequest request) throws Exception{
		subDepartment.setPrimaryKeys(ids.split(","));
		Map<String, Object> resultMap = subDepartmentService.deleteByIds(subDepartment);
		return resultMap;
	}

	/**
	 * @description ????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="copyDeptTree.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="?????????????????????",operateFuncNm="copyDeptTree",operateDescribe="??????????????????????????????")
	public  Map<String, Object> copyDeptTree(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = null;
		User userInfo = SystemSecurityUtils.getUser();// ????????????????????????
		if(userInfo.getIsSystemAdmin() || userInfo.getIsAdmin() == 1){
			resultMap = subDelService.copyDeptTree();
		}
		return resultMap;
	}

}