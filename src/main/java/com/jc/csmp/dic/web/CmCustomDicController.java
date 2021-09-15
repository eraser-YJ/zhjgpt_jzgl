package com.jc.csmp.dic.web;

import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.domain.validator.CmCustomDicValidator;
import com.jc.csmp.dic.service.ICmCustomDicService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Department;
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
 * 建设管理-自定义字典项Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/custom/dic")
public class CmCustomDicController extends BaseController{

	@Autowired
	private ICmCustomDicService cmDicLandNatureService;

	@org.springframework.web.bind.annotation.InitBinder("cmDicLandNature")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmCustomDicValidator());
    }

	public CmCustomDicController() {
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
	public Map<String,Object> save(@Valid CmCustomDic entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(cmDicLandNatureService.saveEntity(entity), resultMap, getToken(request));
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
	public Map<String, Object> update(CmCustomDic entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmDicLandNatureService.updateEntity(entity), resultMap, getToken(request));
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
	public CmCustomDic get(CmCustomDic entity) throws Exception{
		return cmDicLandNatureService.get(entity);
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
		return "csmp/dic/cmCustomDicForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmCustomDic entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByField(" t.queue ");
		}
		PageManager page_ = cmDicLandNatureService.query(entity, page);
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
	public String manage(HttpServletRequest request) throws Exception{
		request.setAttribute("dataType", request.getParameter("dataType"));
		return "csmp/dic/cmCustomDicList";
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
	public  Map<String, Object> deleteByIds(CmCustomDic entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if (cmDicLandNatureService.queryCountByParentIds(entity) > 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "该类别下存在子类别，无法删除");
			return resultMap;
		}
		if(cmDicLandNatureService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 根据条件获取数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="queryTree.action")
	@ResponseBody
	public List<Department> queryTree(HttpServletRequest request) {
		try {
			String dataType = request.getParameter("dataType");
			CmCustomDic param = new CmCustomDic();
			param.addOrderByField(" t.queue ");
			param.setDataType(dataType);
			List<Department> resultList = new ArrayList<>();
			CmCustomDic entity = new CmCustomDic();
			entity.setDataType(dataType);
			Department root = new Department();
			root.setId("0");

			root.setParentId("-1");
			root.setName(entity.getDataTypeValue());
			resultList.add(root);
			List<CmCustomDic> dataList = this.cmDicLandNatureService.queryAll(param);
			if (dataList != null && dataList.size() > 0) {
				for (CmCustomDic dic : dataList) {
					Department dept = new Department();
					dept.setId(dic.getId());
					dept.setParentId(dic.getParentId());
					dept.setName(dic.getName());
					resultList.add(dept);
				}
			}
			return resultList;
		} catch (Exception ex) {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value = "checkCode.action")
	@ResponseBody
	public boolean checkCode(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String code = request.getParameter("code");
		String dataType = request.getParameter("dataType");
		Result result = cmDicLandNatureService.checkCode(id, code, dataType);
		return result.getCode().intValue() == ResultCode.SUCCESS.code().intValue() ? true : false;
	}
}
