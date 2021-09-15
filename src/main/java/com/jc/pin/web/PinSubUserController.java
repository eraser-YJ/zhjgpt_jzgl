package com.jc.pin.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.pin.domain.PinSubUser;
import com.jc.pin.domain.validator.PinSubUserValidator;
import com.jc.pin.service.IPinSubUserService;
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
@RequestMapping(value="/sys/pinSubUser")
public class PinSubUserController extends BaseController{

	
	@Autowired
	private IPinSubUserService pinSubUserService;
	
	@org.springframework.web.bind.annotation.InitBinder("pinSubUser")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new PinSubUserValidator()); 

    }
	
	public PinSubUserController() {
	}

	/**
	 * @description 保存方法
	 * @param  pinSubUser 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-05-04
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义用户拼音表",operateFuncNm="save",operateDescribe="对自定义用户拼音表进行新增操作")
	public Map<String,Object> save(@Valid PinSubUser pinSubUser,BindingResult result,
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
			pinSubUserService.save(pinSubUser);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	* workflowParamTemp修改方法
	* @param  pinSubUser 实体类
	* @param  result 校验结果
	* @return Integer 更新的数目
	* @author
	* @version  2018-05-04 
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义用户拼音表",operateFuncNm="update",operateDescribe="对自定义用户拼音表进行更新操作")
	public Map<String, Object> update(PinSubUser pinSubUser, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = pinSubUserService.update(pinSubUser);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  pinSubUser 实体类
	 * @return PinSubUser 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-05-04
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="自定义用户拼音表",operateFuncNm="get",operateDescribe="对自定义用户拼音表进行单条查询操作")
	public PinSubUser get(PinSubUser pinSubUser,HttpServletRequest request) throws Exception{
		return pinSubUserService.get(pinSubUser);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2018-05-04
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/pinSubUser/pinSubUserForm";
	}

	/**
	 * @description 分页查询方法
	 * @param  pinSubUser 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-05-04 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(PinSubUser pinSubUser,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(pinSubUser.getOrderBy())) {
			pinSubUser.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = pinSubUserService.query(pinSubUser, page);
		return rePage;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2018-05-04 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="自定义用户拼音表",operateFuncNm="manage",operateDescribe="对自定义用户拼音表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/pinSubUser/pinSubUserList"; 
	}

/**
	 * @description 删除方法
	 * @param  pinSubUser 实体类
	 * @param  pinSubUser 实体类
	 * @param  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-05-04
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义用户拼音表",operateFuncNm="deleteByIds",operateDescribe="对自定义用户拼音表进行删除")
	public  Map<String, Object> deleteByIds(PinSubUser pinSubUser,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		pinSubUser.setPrimaryKeys(ids.split(","));	
		if(pinSubUserService.deleteByIds(pinSubUser) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * @description 数据加载方法
	 * @param  pinUser 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2017-1-4
	 */
	@RequestMapping(value = "infoLoading.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="自定义用户树表",operateFuncNm="infoLoading",operateDescribe="对自定义用户树表进行数据导入操作")
	public Map<String,Object> infoLoading(@Valid PinSubUser pinUser,BindingResult result,
										  HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = pinSubUserService.infoLoading();
		return resultMap;
	}

}