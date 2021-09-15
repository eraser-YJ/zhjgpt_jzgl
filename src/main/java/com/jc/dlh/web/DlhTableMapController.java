package com.jc.dlh.web;

import com.jc.dlh.domain.*;
import com.jc.dlh.domain.validator.DlhTableMapValidator;
import com.jc.dlh.service.IDlhDatamodelItemService;
import com.jc.dlh.service.IDlhDatamodelService;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhTableMapService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
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
import java.util.*;

/**
 * 表格关联处理
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/dlh/tablemap/")
public class DlhTableMapController extends BaseController{

	@Autowired
	private IDlhTableMapService dlhTableMapService;

	@Autowired
	private IDlhDatamodelService dlhDatamodelService;

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;

	@Autowired
	private IDlhDatamodelItemService dlhDatamodelItemService;

	@org.springframework.web.bind.annotation.InitBinder("dlhTableMap")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new DlhTableMapValidator());
	}

	public DlhTableMapController() {
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
	@ActionLog(operateModelNm="表格关联",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(DlhTableMap entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String tableNameK = entity.getTableNameK();
		String tableNameV = entity.getTableNameV();
		String columnNameK = entity.getColumnNameK();
		String columnNameV = entity.getColumnNameV();
		if(StringUtil.isEmpty(tableNameK)||StringUtil.isEmpty(tableNameV)){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "表名不能为空");
		}
		if(StringUtil.isEmpty(columnNameK)||StringUtil.isEmpty(columnNameV)){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "表名不能为空");
		}
		DlhDataobject dlhDataobject = new DlhDataobject();
		dlhDataobject.setTableCode(tableNameK);
		List<DlhDataobject> dlhDataobjects = dlhDataobjectService.queryAll(dlhDataobject);
		String objUrlK = entity.getObjUrlK();
		String objUrlV = entity.getObjUrlV();
		if(dlhDataobjects!=null&&dlhDataobjects.size()>0){
			objUrlK=dlhDataobjects.get(0).getObjUrl();
			entity.setObjUrlK(objUrlK);
		}

		dlhDataobject = new DlhDataobject();
		dlhDataobject.setTableCode(tableNameV);
		dlhDataobjects = dlhDataobjectService.queryAll(dlhDataobject);
		if(dlhDataobjects!=null&&dlhDataobjects.size()>0){
			objUrlV=dlhDataobjects.get(0).getObjUrl();
			entity.setObjUrlV(objUrlV);
		}


		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(dlhTableMapService.saveEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="表格关联",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(DlhTableMap entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(dlhTableMapService.updateEntity(entity), resultMap, getToken(request));
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
	public DlhTableMap get(DlhTableMap entity) throws Exception{
		return dlhTableMapService.get(entity);
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
		DlhDatamodel dlhDatamodel = new DlhDatamodel();
		dlhDatamodel.setDeleteFlag(0);
		List<DlhDatamodel> dlhDatamodels = dlhDatamodelService.queryAll(dlhDatamodel);
		model.addAttribute("modelList",dlhDatamodels);
		return "dlh/tableMap/dlhTableMapForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhTableMap entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = dlhTableMapService.query(entity, page);
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
	public String manage(HttpServletRequest request) throws Exception{

		return "dlh/tableMap/dlhTableMapList";
	}

	@RequestMapping(value="itemList.action",method=RequestMethod.GET)
	@ResponseBody
	public List<DlhDatamodelItem> itemList(String modelId){
		DlhDatamodel dlhDatamodel = new DlhDatamodel();
		dlhDatamodel.setDeleteFlag(0);
		dlhDatamodel.setTableCode(modelId);
		String id="";
		try {
			List<DlhDatamodel> dlhDatamodels = dlhDatamodelService.queryAll(dlhDatamodel);
			id = dlhDatamodels.get(0).getId();
		} catch (CustomException e) {
			e.printStackTrace();
		}

		DlhDatamodelItem item = new DlhDatamodelItem();
		item.setModelId(id);
		List<DlhDatamodelItem> dlhDatamodelItems = null;
		try {
			dlhDatamodelItems = dlhDatamodelItemService.queryAll(item);
			Collections.sort(dlhDatamodelItems, new Comparator<DlhDatamodelItem>() {
				@Override
				public int compare(DlhDatamodelItem o1, DlhDatamodelItem o2) {
					return o1.getItemSeq().compareTo(o2.getItemSeq());
				}

			});
		} catch (CustomException e) {
			e.printStackTrace();
		}

		return dlhDatamodelItems;
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
	@ActionLog(operateModelNm="表格关联",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(DlhTableMap entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(dlhTableMapService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}
}

