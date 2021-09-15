package com.jc.dlh.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jc.foundation.domain.BaseBean;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jc.dlh.domain.DlhDatamodelItem;
import com.jc.dlh.service.IDlhDatamodelItemService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类
 * @author lc 
 * @version 2020-03-10
 */

@Controller
@RequestMapping(value = "/dlh/dlhDatamodelItem")
public class DlhDatamodelItemController extends BaseController {

	@Autowired
	private IDlhDatamodelItemService dlhDatamodelItemService;

	@Autowired
	private IDicService dicService;

	public DlhDatamodelItemController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @param BindingResult    result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "save.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@Valid DlhDatamodelItem dlhDatamodelItem, BindingResult result, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if (!"false".equals(resultMap.get("success"))) {
			dlhDatamodelItemService.save(dlhDatamodelItem);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @param BindingResult    result 校验结果
	 * @return Integer 更新的数目
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhDatamodelItem dlhDatamodelItem, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = dlhDatamodelItemService.update(dlhDatamodelItem);
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
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @return DlhDatamodelItem 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "get.action", method = RequestMethod.GET)
	@ResponseBody
	public DlhDatamodelItem get(DlhDatamodelItem dlhDatamodelItem, HttpServletRequest request) throws Exception {
		return dlhDatamodelItemService.get(dlhDatamodelItem);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "loadForm.action", method = RequestMethod.GET)
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		String dbType = request.getParameter("dbType");
		return "dlh/dlhDatamodelItem/" + dbType + "/dlhDatamodelItemForm";
	}

	@RequestMapping(value = "select.action", method = RequestMethod.GET)
	public String select(HttpServletRequest request) throws Exception {
		String modelId = request.getParameter("modelId");
		if (modelId != null && modelId.length() > 0) {
			DlhDatamodelItem cond = new DlhDatamodelItem();
			cond.setModelId(modelId);
			List<DlhDatamodelItem> itemList = dlhDatamodelItemService.queryAll(cond);
			request.setAttribute("itemList", itemList);
		}
		return "dlh/dlhDatamodelItem/include/dlhDatamodelItemSel";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @param PageManager      page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDatamodelItem dlhDatamodelItem, PageManager page) {
		try {
			// 默认排序
			if (StringUtil.isEmpty(dlhDatamodelItem.getOrderBy())) {
				dlhDatamodelItem.addOrderByField("t.item_seq");
				dlhDatamodelItem.addOrderByFieldDesc("t.CREATE_DATE");
			}
			PageManager page_ = dlhDatamodelItemService.query(dlhDatamodelItem, page);
			List<? extends BaseBean> page_data = page_.getData();
			return page_;
		} catch (Exception e) {
			e.printStackTrace();
			return page;
		}
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @param PageManager      page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageFormList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageFormList(DlhDatamodelItem dlhDatamodelItem, PageManager page) {
		try {
			// 默认排序
			if (StringUtil.isEmpty(dlhDatamodelItem.getOrderBy())) {
				dlhDatamodelItem.addOrderByField("t.item_seq");
				dlhDatamodelItem.addOrderByFieldDesc("t.CREATE_DATE");
			}
			PageManager page_ = dlhDatamodelItemService.query(dlhDatamodelItem, page);
			List<DlhDatamodelItem> page_data = (List<DlhDatamodelItem>)page_.getData();
//			List<DlhDatamodelItem> page_ = dlhDatamodelItemService.queryAll(dlhDatamodelItem);
			for(DlhDatamodelItem item:page_data){
				if(!StringUtil.isEmpty(item.getDicCode())){
					Dic dic = new Dic();
					dic.setParentId(item.getDicCode());
					item.setDicList(dicService.query(dic));
				}
			}
			return page_;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manage.action", method = RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception {
		String modelId = request.getParameter("modelId");
		String dbType = request.getParameter("dbType");
		if (modelId != null) {
			request.setAttribute("modelId", modelId);
			request.setAttribute("dbType", dbType);
			return "dlh/dlhDatamodelItem/" + dbType + "/dlhDatamodelItemList";
		}
		return "";

	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageForm.action", method = RequestMethod.GET)
	public String manageForm(HttpServletRequest request) throws Exception {
		String modelId = request.getParameter("modelId");
		String dbType = request.getParameter("dbType");
		if (modelId != null) {
			request.setAttribute("modelId", modelId);
			request.setAttribute("dbType", dbType);
			return "dlh/dlhDatamodelItem/" + dbType + "/dlhDatamodelFormList";
		}
		return "";

	}

	/**
	 * @description 删除方法
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @param String           dlhDatamodelItem 实体类
	 * @param String           ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhDatamodelItem dlhDatamodelItem, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhDatamodelItem.setPrimaryKeys(ids.split(","));
		try {
			if (dlhDatamodelItemService.deleteByIds(dlhDatamodelItem) != 0) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
			}
		} catch (Exception ex) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ex.getMessage());
		}
		return resultMap;
	}

	/**
	 * @description 导入excel方法
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-19
	 */
	@RequestMapping(value = "importExcel.action")
	@ResponseBody
	public void importExcel(@RequestParam(value = "excelFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		if (file.getSize() == 0) {
			response.getWriter().print("{\"success\":\"false\",\"errorMsg\":\"请选择文件后再上传\"}");
		} else {
			if (!file.getOriginalFilename().endsWith("xlsx") && !file.getOriginalFilename().endsWith("xls")) {
				response.getWriter().print("{\"success\":\"false\",\"errorMsg\":\"文件类型错误\"}");
			} else {
				String success = "";
				String errorMsg = "";
				try {
					Map<String, Object> resultMap = dlhDatamodelItemService.importExcel(file, request);
					if (resultMap.get("success") != null) {
						success = (String) resultMap.get("success");
					}
					if (resultMap.get("errorMsg") != null) {
						errorMsg = (String) resultMap.get("errorMsg");
					}
				} catch (Exception e) {
					success = "false";
					errorMsg = "导入失败！";
				} finally {
					response.getWriter().print("{\"success\":\"" + success + "\",\"errorMsg\":\"" + errorMsg + "\"}");
				}
			}
		}
	}

}