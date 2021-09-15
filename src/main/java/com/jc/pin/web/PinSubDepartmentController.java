package com.jc.pin.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.pin.domain.PinSubDepartment;
import com.jc.pin.domain.validator.PinSubDepartmentValidator;
import com.jc.pin.service.IPinSubDepartmentService;
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
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value="/sys/pinSubDepartment")
public class PinSubDepartmentController extends BaseController{

	@Autowired
	private IPinSubDepartmentService pinSubDepartmentService;
	
	@org.springframework.web.bind.annotation.InitBinder("pinSubDepartment")
    public void initBinder(WebDataBinder binder) {  
       	binder.setValidator(new PinSubDepartmentValidator());
    }
	
	public PinSubDepartmentController() {
	}

	/**
	 * 保存方法
	 * @param pinSubDepartment
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门拼音表",operateFuncNm="save",operateDescribe="对自定义部门拼音表进行新增操作")
	public Map<String,Object> save(@Valid PinSubDepartment pinSubDepartment,BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if (!GlobalContext.FALSE.equals(resultMap.get(GlobalContext.SUCCESS))) {
			pinSubDepartmentService.save(pinSubDepartment);
			resultMap.put(GlobalContext.SUCCESS, GlobalContext.TRUE);
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param pinSubDepartment
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门拼音表",operateFuncNm="update",operateDescribe="对自定义部门拼音表进行更新操作")
	public Map<String, Object> update(PinSubDepartment pinSubDepartment, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = pinSubDepartmentService.update(pinSubDepartment);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param pinSubDepartment
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门拼音表",operateFuncNm="get",operateDescribe="对自定义部门拼音表进行单条查询操作")
	public PinSubDepartment get(PinSubDepartment pinSubDepartment,HttpServletRequest request) throws Exception{
		return pinSubDepartmentService.get(pinSubDepartment);
	}

	/**
	 * 弹出对话框方法
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/pinSubDepartment/pinSubDepartmentForm";
	}

	/**
	 * 分页查询方法
	 * @param pinSubDepartment
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(PinSubDepartment pinSubDepartment,PageManager page){
		if(StringUtil.isEmpty(pinSubDepartment.getOrderBy())) {
			pinSubDepartment.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = pinSubDepartmentService.query(pinSubDepartment, page);
		return rePage;
	}

	/**
	 * 跳转方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="自定义部门拼音表",operateFuncNm="manage",operateDescribe="对自定义部门拼音表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/pinSubDepartment/pinSubDepartmentList"; 
	}

	/**
	 * 删除方法
	 * @param pinSubDepartment
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门拼音表",operateFuncNm="deleteByIds",operateDescribe="对自定义部门拼音表进行删除")
	public  Map<String, Object> deleteByIds(PinSubDepartment pinSubDepartment,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		pinSubDepartment.setPrimaryKeys(ids.split(","));	
		if(pinSubDepartmentService.deleteByIds(pinSubDepartment) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 数据加载方法
	 * @param pinDepartment
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "infoLoading.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义部门树表",operateFuncNm="infoLoading",operateDescribe="对自定义部门树表进行数据导入操作")
	public Map<String,Object> infoLoading(@Valid PinSubDepartment pinDepartment,BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = pinSubDepartmentService.infoLoading();
		return resultMap;
	}

}