package com.jc.supervise.point.web;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.supervise.point.domain.CmSupervisionPoint;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import com.jc.supervise.point.domain.validator.CmSupervisionPointValidator;
import com.jc.supervise.point.service.ICmSupervisionPointColumnService;
import com.jc.supervise.point.service.ICmSupervisionPointService;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.security.SystemSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 建设管理-监察点管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervise/point")
public class CmSupervisionPointController extends BaseController{

	@Autowired
	private ICmSupervisionPointService cmSupervisionPointService;
	@Autowired
	private ICmSupervisionPointColumnService cmSupervisionPointColumnService;
	@Autowired
	private ICmProjectInfoService cmProjectInfoService;

	@org.springframework.web.bind.annotation.InitBinder("cmSupervisionPoint")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmSupervisionPointValidator());
    }

	public CmSupervisionPointController() {
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
	public Map<String,Object> save(@Valid CmSupervisionPoint entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
			GlobalUtil.resultToMap(cmSupervisionPointService.saveEntity(entity), resultMap, getToken(request));
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
	public Map<String, Object> update(CmSupervisionPoint entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmSupervisionPointService.updateEntity(entity), resultMap, getToken(request));
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
	public CmSupervisionPoint get(CmSupervisionPoint entity) throws Exception{
		return cmSupervisionPointService.get(entity);
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
		map.put("tempId", UUID.randomUUID().toString());
		model.addAttribute("data", map);
		return "supervise/point/supervisionPointForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmSupervisionPoint entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		PageManager page_ = cmSupervisionPointService.query(entity, page);
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
	public String manage() throws Exception{
		return "supervise/point/supervisionPointList";
	}

	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String search() throws Exception{
		return "supervise/point/supervisionPointSearch";
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
	public  Map<String, Object> deleteByIds(CmSupervisionPoint entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmSupervisionPointService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value="checkPage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String checkPage() throws Exception{
		return "supervise/point/supervisionPointCheckPage";
	}

	@RequestMapping(value="checkJs.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="checkJs",operateDescribe="对进行单条查询操作")
	public Result checkJs(CmSupervisionPoint entity) throws Exception{
		if (StringUtil.isEmpty(entity.getJsContent())) {
			return Result.failure(1, "脚本不存在");
		}
		if (StringUtil.isEmpty(entity.getFunctionName())) {
			return Result.failure(1, "脚本方法名不存在");
		}
		//1、根据项目编号查询项目
		CmProjectInfo project = this.cmProjectInfoService.getbyProjectNumber(entity.getProjectNumber());
		if (project == null) {
			return Result.failure(1, "项目不存在");
		}
		//2、查询出全部的参数来源
		CmSupervisionPointColumn columnParam = new CmSupervisionPointColumn();
		columnParam.setSupervisionId(entity.getSupervisionId());
		columnParam.addOrderByField(" t.queue ");
		List<CmSupervisionPointColumn> columnList = this.cmSupervisionPointColumnService.queryAll(columnParam);
		if (columnList == null || columnList.size() == 0) {
			return Result.failure(1, "数据来源未配置");
		}
		return this.cmSupervisionPointService.checkJs(entity, project, columnList);
	}
}
