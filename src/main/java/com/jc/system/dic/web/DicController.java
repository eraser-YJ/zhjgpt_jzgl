package com.jc.system.dic.web;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicTree;
import com.jc.system.dic.domain.DicType;
import com.jc.system.dic.service.IDicService;
import com.jc.system.dic.service.impl.DicManagerImpl;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 字典控制器
 * @author Administrator
 * @date 2020-06-29
 */
@Controller
@RequestMapping(value = "/dic")
public class DicController extends BaseController {

	private IDicManager dicManager = new DicManagerImpl();

	@Autowired
	private IDicService dicService;
	
	public DicController() {
	}

	/**
	 * 跳转方法
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	public String manage(Model model, HttpServletRequest request) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()) {
				dicManager.getDicsByTypeCode("test");
				Map<String, Object> map = new HashMap<>(1);
				String token = getToken(request);
				map.put(GlobalContext.SESSION_TOKEN, token);
				model.addAttribute("data", map);
				model.addAttribute("parentId","-1");
				model.addAttribute("parentType","-1=-2");
				menuUtil.saveMenuID("/dic/manage.action",request);
				return "dic/manageList";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 跳转方法
	 * @param model
	 * @param code
	 * @param parentId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "manageNext.action",method=RequestMethod.GET)
	public String manageNext(Model model,String code,String parentId, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		model.addAttribute("parentId",code);
		model.addAttribute("parentType",code+"="+parentId);
		return "dic/manageList";
	}

	/**
	 * 分页查询方法
	 * @param dic
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="字典设置",operateFuncNm="manageList",operateDescribe="对字典设置进行查询")
	public PageManager manageList(Dic dic, PageManager page, HttpServletRequest request){
		if(StringUtils.isEmpty(dic.getOrderBy())) {
			dic.addOrderByFieldDesc("t.create_date");
		}
		PageManager rePage = dicService.query(dic,page);
		return rePage;
	}

	@RequestMapping(value = "dicEdit.action",method=RequestMethod.GET)
	public String dicEdit(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "dic/manageEdit";
	}

	/**
	 * 获取单条记录方法
	 * @param dic
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="字典设置",operateFuncNm="get",operateDescribe="对字典设置进行对象查询")
	public Dic get(Dic dic,HttpServletRequest request) throws Exception{
		return dicService.get(dic);
	}

	/**
	 * 保存方法
	 * @param dic
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="字典设置",operateFuncNm="save",operateDescribe="对字典设置进行保存")
	public Map<String,Object> save(@Valid Dic dic, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String,Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			dicService.save(dic);
			resultMap.put("success", "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 删除方法
	 * @param dic
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="字典设置",operateFuncNm="deleteByIds",operateDescribe="对字典设置进行删除")
	public Integer deleteByIds(Dic dic,String ids,HttpServletRequest request) throws Exception{
		dic.setId(ids.split(",")[0]);
		return dicService.delete(dic);
	}

	/**
	 * 修改方法
	 * @param dic
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="字典设置",operateFuncNm="update",operateDescribe="对字典设置进行修改")
	public Map<String, Object> update(Dic dic,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = dicService.update(dic);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 验证用户名是否存在
	 * @param dic
	 * @param codeOld
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "checkDicCode.action",method=RequestMethod.GET)
	@ResponseBody
	public String checkDicCode(Dic dic,String codeOld) throws Exception {
		if(StringUtils.isEmpty(codeOld)){
			Dic dics = new Dic();
			dics.setDeleteFlag(null);
			dics.setParentType(dic.getParentType());
			dics.setCode(dic.getCode());
			List<Dic> dicList = dicService.query(dics);
			if(dicList == null || dicList.size() == 0){
				return "true";
			} else if(dicList.size() == 1 && dicList.get(0).getId().equals(dic.getId())){
				return "true";
			} else {
				return "false";
			}
		} else {
			return "true";
		}
	}

	/**
	 * 显示管理员机构树
	 * @return
	 */
	@RequestMapping(value = "managerDicTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<DicTree> managerDicTree() {
		List<DicTree> dicTreeList = new ArrayList<>();
		List<Dic> dicList = dicService.query(new Dic());
		for (Dic dicVo : dicList){
			DicTree dicTree = new DicTree();
			dicTree.setId(dicVo.getCode());
			dicTree.setName(dicVo.getValue());
			dicTree.setParentId(dicVo.getParentId());
			dicTreeList.add(dicTree);
		}
		return dicTreeList;
	}

	@RequestMapping(value = "getDics.action",method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject getDics(String typeCode, String parentCode) {
		return new JSONPObject("fillDicsJsonpCallback", dicManager.getDicsByTypeCode(typeCode,parentCode));
	}

	@RequestMapping(value = "getDicsAll.action",method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject getDicsAll(String typeCode, String parentCode) {
		return new JSONPObject("fillAllDicsJsonpCallback", dicManager.getAllDicsByTypeCode(typeCode, parentCode, false));
	}

	@RequestMapping(value = "getDicValue.action",method=RequestMethod.GET)
	@ResponseBody
	public Dic getDicValue(String typeCode, String parentCode, String dicCode) {
		return dicManager.getDic(typeCode, parentCode, dicCode);
	}

	@RequestMapping(value = "getDicTypes.action",method=RequestMethod.GET)
	@ResponseBody
	public List<DicType> getDicTypes() {
		return dicManager.getDicTypes();
	}

	/**
	 * 跳转方法（数据字典左侧树）
	 * @param model
	 * @param typeCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicTreeTypes.action",method=RequestMethod.POST)
	public String getDicTreeTypes(Model model, String typeCode) throws Exception {
		model.addAttribute("dicTreeTypeList", dicManager.getDicTypes());
		return "dic/dicTypeList";
	}

	/**
	 * 跳转方法（数据字典右侧节点列表）
	 * @param model
	 * @param typeCode
	 * @param parentCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicTreeInfo.action",method=RequestMethod.POST)
	public String getDicTreeInfo(Model model, String typeCode, String parentCode) throws Exception {
		List<Dic> diclist = dicManager.getAllDicsByTypeCode(typeCode, parentCode, false);
		model.addAttribute("dicNodeInfo", diclist);
		model.addAttribute("dicNodeInfoSize", diclist.size());
		return "dic/dicNodeList";
	}

	/**
	 * 数据字典多级联动查询方法
	 * @param typeCode
	 * @param parentCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicJSONInfo.action",method=RequestMethod.POST)
	public void getDicJSONInfo(String typeCode, String parentCode, HttpServletResponse response) throws Exception {
		List<Dic> diclist = dicManager.getAllDicsByTypeCode(typeCode, parentCode, true);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(JsonUtil.java2Json(diclist));
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping(value = "addNewDic.action",method=RequestMethod.POST)
	@ResponseBody
	public Dic addNewType(@RequestBody Dic dic) throws CustomException {
		return dicManager.addNewDic(dic);
	}

	@RequestMapping(value = "addNewDicList.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "字典设置", operateFuncNm = "addNewDicList", operateDescribe = "对字典设置进行批量保存")
	public Map<String, Object> addNewDicList(String parentid, HttpServletRequest request) throws CustomException {
		Map<String, Object> resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if (!"false".equals(resultMap.get("success"))) {
			String jsonStr = request.getParameter("mydata");
			List<Dic> dicList = JsonUtil.json2Array(jsonStr, Dic.class);
			dicManager.addNewDicList(dicList);
			resultMap.put("success", "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value = "addNewType.action",method=RequestMethod.POST)
	@ResponseBody
	public DicType addNewType(@RequestBody DicType type) throws CustomException {
		return dicManager.addNewDicType(type);
	}

	@RequestMapping(value = "deleteDic.action",method=RequestMethod.POST)
	@ResponseBody
	public Dic deleteDic(@RequestBody Dic dic) throws CustomException {
		return dicManager.removeDic(dic);
	}

	@RequestMapping(value = "deleteType.action",method=RequestMethod.POST)
	@ResponseBody
	public DicType deleteType(@RequestBody DicType type) throws CustomException {
		return dicManager.removeDicType(type);
	}

	@RequestMapping(value = "updateDic.action",method=RequestMethod.POST)
	@ResponseBody
	public Dic updateDic(@RequestBody Dic dic) throws CustomException {
		return dicManager.updateDic(dic);
	}

	@RequestMapping(value = "updateDicType.action",method=RequestMethod.POST)
	@ResponseBody
	public DicType updateDicType(@RequestBody DicType type) throws CustomException {
		return dicManager.updateType(type);
	}

	@RequestMapping(value = "changeDicToType.action",method=RequestMethod.POST)
	@ResponseBody
	public Dic changeDicToType(Dic dic) throws CustomException {
		return dicManager.changeDicToType(dic);
	}

	@RequestMapping(value = "updateOrderFlag.action",method=RequestMethod.POST)
	@ResponseBody
	public Dic updateOrderFlag(Dic dic) throws CustomException {
		return dicManager.updateDic(dic);
	}

	/**
	 * 跳转方法
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "manageInfo.action",method=RequestMethod.GET)
	public String manageInfo(Model model, HttpServletRequest request) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()) {
				dicManager.getDicsByTypeCode("test");
				Map<String, Object> map = new HashMap<>();
				String token = getToken(request);
				map.put(GlobalContext.SESSION_TOKEN, token);
				model.addAttribute("data", map);
				return "dic/dicTypeManage";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 跳转方法（数据字典类型左侧树）
	 * @param model
	 * @param typeCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicInfoTreeTypes.action",method=RequestMethod.GET)
	public String getDicInfoTreeTypes(Model model, String typeCode) throws Exception {
		model.addAttribute("dicTreeTypeList", dicManager.getDicTypes());
		return "dic/dicInfoTypeList";
	}

	/**
	 * 跳转方法（数据字典右侧表单）
	 * @param model
	 * @param dicId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicInfo.action",method=RequestMethod.GET)
	public String getDicInfo(Model model, String dicId, HttpServletRequest request) throws Exception {
		Dic dic = dicManager.getDic(dicId);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("dicInfo", dic);
		return "dic/dicInfo";
	}

	/**
	 * 返回指定类型下所有的字典数据(客户端调用)
	 * @param model
	 * @param typeCode 类型码
	 * @param parentCode 父类型码
	 * @param useFlag 使用标志
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAllDicsByTypeCode.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Dic> getAllDicsByTypeCode(Model model, String typeCode, String parentCode, Boolean useFlag, HttpServletRequest request) throws Exception {
		return dicManager.getAllDicsByTypeCode(typeCode, parentCode, useFlag);
	}

	/**
	 * 返回指定字典数据(客户端调用)
	 * @param model
	 * @param typeCode
	 * @param parentCode
	 * @param dicCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDic.action",method=RequestMethod.GET)
	@ResponseBody
	public Dic getDic(Model model, String typeCode, String parentCode, String dicCode, HttpServletRequest request) throws Exception {
		return dicManager.getDic(typeCode, parentCode, dicCode);
	}

	/**
	 * 返回指定字典类型数据(客户端调用)
	 * @param model
	 * @param code
	 * @param parentCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicType.action",method=RequestMethod.GET)
	@ResponseBody
	public DicType getDicType(Model model, String code, String parentCode, HttpServletRequest request) throws Exception {
		return dicManager.getDicType(code, parentCode);
	}

	/**
	 * 根据id查询字典项(客户端调用)
	 * @param model
	 * @param dicId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDicById.action",method=RequestMethod.GET)
	@ResponseBody
	public Dic getDicById(Model model, String dicId, HttpServletRequest request) throws Exception {
		return dicManager.getDic(dicId);
	}
}