package com.jc.system.api.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.domain.validator.ApiMetaValidator;
import com.jc.system.api.service.IApiMetaService;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/apiService")
public class ApiMetaController extends BaseController {

	@Autowired
	private IApiMetaService apiServiceService;

	@org.springframework.web.bind.annotation.InitBinder("apiMeta")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ApiMetaValidator());
	}

	public ApiMetaController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
	public Map<String,Object> save(@Valid ApiMeta apiMeta, BindingResult result, HttpServletRequest request) throws Exception{
		apiMeta.setUuid(UUID.randomUUID().toString());
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			apiServiceService.save(apiMeta);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(ApiMeta apiMeta, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = apiServiceService.update(apiMeta);
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
	public ApiMeta get(ApiMeta apiMeta, HttpServletRequest request) throws Exception{
		return apiServiceService.get(apiMeta);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2016-02-15
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/apiMeta/module/apiServiceForm";
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(ApiMeta apiMeta, PageManager page){
		if(StringUtil.isEmpty(apiMeta.getOrderBy())) {
			apiMeta.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = apiServiceService.query(apiMeta, page);
		return rePage;
	}

	@RequestMapping(value="getAllSubsystemAndApi.action",method=RequestMethod.GET)
	@ResponseBody
	public void getAllSubsystemAndApi(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String pluginId = request.getParameter("pluginId");
		String jsonArray = apiServiceService.getAllSubsystemAndApi(pluginId);
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/apiMeta/apiServiceList";
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(ApiMeta apiMeta, String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		apiMeta.setPrimaryKeys(ids.split(","));
		if(apiServiceService.deleteByIds(apiMeta) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}