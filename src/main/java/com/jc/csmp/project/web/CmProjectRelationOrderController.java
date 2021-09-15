package com.jc.csmp.project.web;

import com.jc.csmp.project.domain.CmProjectRelationOrder;
import com.jc.csmp.project.domain.validator.CmProjectRelationOrderValidator;
import com.jc.csmp.project.service.ICmProjectRelationOrderService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.number.service.Number;
import com.jc.system.number.service.interfaces.INumber;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 建设管理-项目工程联系单控制器
 * 1、签收部门的负责人进行签收，不允许给为配置部门负责人的部门发送项目工程联系单
 * 2、已经签收的联系单，不允许修改
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/relation")
public class CmProjectRelationOrderController extends BaseController{

	@Autowired
	private ICmProjectRelationOrderService projectRelationOrderService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectRelationOrder")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectRelationOrderValidator());
	}

	public CmProjectRelationOrderController() {
	}

	/**
	 * 保存方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
	public Map<String,Object> save(@Valid CmProjectRelationOrder entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(projectRelationOrderService.saveEntity(entity), resultMap, getToken(request));
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(CmProjectRelationOrder entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(projectRelationOrderService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmProjectRelationOrder get(CmProjectRelationOrder entity) throws Exception{
		return projectRelationOrderService.get(entity);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		INumber number = new Number();
		String ifUpdate = request.getParameter("o");
		if (ifUpdate == null) {
			String no = number.getNumber("PROREL", 1, 2, null);
			map.put("code", no.substring(1));
		}
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "csmp/project/relation/projectRelationOrderForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectRelationOrder entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setDeptCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		PageManager page_ = projectRelationOrderService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage() throws Exception{
		return "csmp/project/relation/projectRelationOrderList";
	}

	@RequestMapping(value="my.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String my() throws Exception{
		return "csmp/project/relation/projectRelationOrderMyList";
	}

	/**
	 * 查询我接收的联系单
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="myList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager myList(CmProjectRelationOrder entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setSignerDeptLeaderId(SystemSecurityUtils.getUser().getId());
		PageManager page_ = projectRelationOrderService.myQuery(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	 * 删除方法
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(CmProjectRelationOrder entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		Integer result = projectRelationOrderService.deleteByIds(entity);
		if (result.intValue() == GlobalContext.CUSTOM_SIGN_ERROR) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "存在已签收的工程单，无法删除，请重新选择");
		} else{
			if (result != 0) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
			}
		}
		return resultMap;
	}

	@RequestMapping(value="read.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="read",operateDescribe="设置工程联系单已读")
	public Result read(String id) throws Exception{
		if (StringUtil.isEmpty(id)) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		CmProjectRelationOrder entity = this.projectRelationOrderService.getById(id);
		if (entity != null && entity.getSignerDept().equals(SystemSecurityUtils.getUser().getDeptId())) {
			entity.setSignStatus("1");
			entity.setSignDate(new Date(System.currentTimeMillis()));
			this.projectRelationOrderService.update(entity);
		}
		return Result.success();
	}
}

