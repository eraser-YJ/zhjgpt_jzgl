package com.jc.system.tab.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.tab.domain.TabTree;
import com.jc.system.tab.domain.validator.TabTreeValidator;
import com.jc.system.tab.service.ITabTreeService;
import com.jc.system.util.PluginServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/tabTree")
public class TabTreeController extends BaseController {
	
	@Autowired
	private ITabTreeService tabTreeService;
	
	@org.springframework.web.bind.annotation.InitBinder("tabTree")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new TabTreeValidator());
    }

	@RequestMapping(value = "save.action",method= RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统页签树表",operateFuncNm="save",operateDescribe="对系统页签树表进行新增操作")
	public Map<String,Object> save(@Valid TabTree tabTree,BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			tabTreeService.save(tabTree);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method= RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统页签树表",operateFuncNm="update",operateDescribe="对系统页签树表进行更新操作")
	public Map<String, Object> update(TabTree tabTree, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = tabTreeService.update(tabTree);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="get.action",method= RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="系统页签树表",operateFuncNm="get",operateDescribe="对系统页签树表进行单条查询操作")
	public TabTree get(TabTree tabTree,HttpServletRequest request) throws Exception{
		return tabTreeService.get(tabTree);
	}

	@RequestMapping(value="loadForm.action",method= RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/tabTree/tabTreeForm";
	}

	@RequestMapping(value="manageList.action",method= RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(TabTree tabTree,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(tabTree.getOrderBy())) {
			tabTree.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = tabTreeService.query(tabTree, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method= RequestMethod.GET)
	@ActionLog(operateModelNm="系统页签树表",operateFuncNm="manage",operateDescribe="对系统页签树表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/tabTree/tabTreeList"; 
	}

	@RequestMapping(value="deleteByIds.action",method= RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统页签树表",operateFuncNm="deleteByIds",operateDescribe="对系统页签树表进行删除")
	public  Map<String, Object> deleteByIds(TabTree tabTree,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		tabTree.setPrimaryKeys(ids.split(","));	
		if(tabTreeService.deleteByIds(tabTree) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}