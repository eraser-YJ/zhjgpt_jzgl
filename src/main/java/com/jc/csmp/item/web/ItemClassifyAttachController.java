package com.jc.csmp.item.web;

import com.jc.csmp.item.domain.ItemClassify;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.domain.validator.ItemClassifyAttachValidator;
import com.jc.csmp.item.service.IItemClassifyAttachService;
import com.jc.csmp.item.service.IItemClassifyService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
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
import java.util.List;
import java.util.Map;

/**
 * 项目分类附件处理
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/csmp/item/attach")
public class ItemClassifyAttachController extends BaseController{

	@Autowired
	private IItemClassifyAttachService itemClassifyAttachService;
	@Autowired
	private IItemClassifyService itemClassifyService;

	@org.springframework.web.bind.annotation.InitBinder("itemClassifyAttach")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ItemClassifyAttachValidator());
	}

	public ItemClassifyAttachController() {
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
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid ItemClassifyAttach entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(itemClassifyAttachService.saveEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(ItemClassifyAttach entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(itemClassifyAttachService.updateEntity(entity), resultMap, getToken(request));
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
	public ItemClassifyAttach get(ItemClassifyAttach entity) throws Exception{
		return itemClassifyAttachService.get(entity);
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
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
        /**获取项目分类*/
		List<ItemClassify> itemClassifyList =itemClassifyService.queryAll(new ItemClassify());
		model.addAttribute("itemClassifyList",itemClassifyList);
		return "csmp/item/itemClassifyAttachForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(ItemClassifyAttach entity, PageManager page){
		entity.addOrderByField("t.ext_num1");
		PageManager page_ = itemClassifyAttachService.query(entity, page);
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
	public String manage(Model model,HttpServletRequest request) throws Exception{
		/**获取项目分类*/
		List<ItemClassify> itemClassifyList =itemClassifyService.queryAll(new ItemClassify());
		model.addAttribute("itemClassifyList",itemClassifyList);
		return "csmp/item/itemClassifyAttachList";
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
	@ActionLog(operateModelNm="项目分类附件",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(ItemClassifyAttach entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(itemClassifyAttachService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}
}

