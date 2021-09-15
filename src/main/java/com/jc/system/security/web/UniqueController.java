package com.jc.system.security.web;

import javax.validation.Valid;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.WebDataBinder;


import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.domain.PageManager;
import com.jc.system.applog.ActionLog;

import com.jc.system.security.domain.Unique;
import com.jc.system.security.domain.validator.UniqueValidator;
import com.jc.system.security.service.IUniqueService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/unique")
public class UniqueController extends BaseController {
	
	@Autowired
	private IUniqueService uniqueService;
	
	@org.springframework.web.bind.annotation.InitBinder("unique")
    public void initBinder(WebDataBinder binder) {  
		binder.setValidator(new UniqueValidator());
    }
	
	public UniqueController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="生成标识",operateFuncNm="save",operateDescribe="对标识进行新增操作")
	public Map<String,Object> save(@Valid Unique unique,BindingResult result,int createCount, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			List<Unique> list = new ArrayList<>();
			for(int i=0; i<createCount; i++){
				Unique unique1 = new Unique();
				unique1.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
				unique1.setState("0");
				list.add(unique1);
			}
			uniqueService.saveList(list);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(Unique unique, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = uniqueService.update(unique);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public Unique get(Unique unique,HttpServletRequest request) throws Exception{
		return uniqueService.get(unique);
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/unique/module/uniqueForm"; 
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Unique unique,PageManager page){
		if(StringUtil.isEmpty(unique.getOrderBy())) {
			unique.addOrderByFieldDesc("t.id");
		}
		PageManager rePage = uniqueService.query(unique, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/unique/uniqueList"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="生成标识",operateFuncNm="deleteByIds",operateDescribe="对标识进行删除")
	public  Map<String, Object> deleteByIds(Unique unique,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		unique.setPrimaryKeys(ids.split(","));	
		if(uniqueService.deleteByIds(unique) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}