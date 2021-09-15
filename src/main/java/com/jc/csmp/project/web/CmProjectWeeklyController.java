package com.jc.csmp.project.web;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.domain.CmProjectWeekly;
import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.csmp.project.domain.validator.CmProjectWeeklyValidator;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.csmp.project.service.ICmProjectWeeklyItemService;
import com.jc.csmp.project.service.ICmProjectWeeklyService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
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
 * 周报管理控制器
 * @author Administrator
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/weekly")
public class CmProjectWeeklyController extends BaseController{

	@Autowired
	private ICmProjectWeeklyService projectWeeklyService;
	@Autowired
	private ICmProjectInfoService cmProjectInfoService;
	@Autowired
	private ICmProjectWeeklyItemService cmProjectWeeklyItemService;
	@Autowired
	private IMobileReportLogService mobileReportLogService;
	
	@org.springframework.web.bind.annotation.InitBinder("cmProjectWeekly")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectWeeklyValidator());
	}

	public CmProjectWeeklyController() {
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
	@ActionLog(operateModelNm="周报管理",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid CmProjectWeekly entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		entity.setStatus(0);
		entity.setIfRelease(0);
		entity.setUserId(SystemSecurityUtils.getUser().getId());
		entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(projectWeeklyService.saveEntity(entity), resultMap, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value = "saveReportName.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="周报管理",operateFuncNm="save",operateDescribe="新增操作")
	public Result saveReportName(@Valid CmProjectWeekly entity) throws Exception {
		entity.setStatus(0);
		entity.setIfRelease(0);
		entity.setUserId(SystemSecurityUtils.getUser().getId());
		entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		return projectWeeklyService.saveEntity(entity);
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
	@ActionLog(operateModelNm="周报管理",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(CmProjectWeekly entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Result res = projectWeeklyService.updateEntity(entity);
		if (res.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
			if (entity.getClickRelease() != null && entity.getClickRelease().intValue() == 1) {
				//记录周报发布日志
				this.mobileReportLogService.saveLog(MobileReportLogEnum.weekly, res.getData(), SystemSecurityUtils.getUser());
			}
		}
		GlobalUtil.resultToMap(res, resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 反馈
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="feedback.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="周报管理",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> feedback(CmProjectWeekly entity) throws Exception {
		Map<String, Object> resultMap = new HashMap<>(2);
		if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getFeedback())) {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ResultCode.PARAM_IS_BLANK.message());
			return resultMap;
		}
		CmProjectWeekly week = this.projectWeeklyService.getById(entity.getId());
		if (week == null) {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ResultCode.RESULE_DATA_NONE.message());
			return resultMap;
		}
		week.setFeedback(entity.getFeedback());
		week.setStatus(1);
		GlobalUtil.resultToMap(projectWeeklyService.updateEntity(week), resultMap, null);
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
	public CmProjectWeekly get(CmProjectWeekly entity) throws Exception{
		return projectWeeklyService.get(entity);
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
		map.put("projectId", request.getParameter("projectId"));
		map.put("id", request.getParameter("id"));
		model.addAttribute("data", map);
		return "csmp/project/weekly/cmProjectWeeklyForm";
	}

	/**
	 * @description 弹出进度修改对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadPlanForm.action",method=RequestMethod.GET)
	public String loadPlanForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		request.setAttribute("projectId", request.getParameter("projectId"));
		request.setAttribute("projectName", request.getParameter("projectName"));
		return "csmp/project/weekly/cmProjectWeeklyPlanForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectWeekly entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		PageManager page_ = projectWeeklyService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	 * 我发布的周报
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="myList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager myList(CmProjectWeekly entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		PageManager page_ = projectWeeklyService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
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
	public String manage() throws Exception{
		return "csmp/project/weekly/cmProjectWeeklyList";
	}

	/**
	 * @description 跳转查看页面方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="weeklyToShow.action",method=RequestMethod.GET)
	public String weeklyToShow() throws Exception{
		return "csmp/project/weekly/cmProjectWeeklyShowList";
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
	@ActionLog(operateModelNm="周报管理",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(CmProjectWeekly entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(projectWeeklyService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 项目树信息
	 * @param request
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value="treeList.action")
	@ResponseBody
	public List<Department> treeList(HttpServletRequest request) throws CustomException {
		CmProjectInfo param = new CmProjectInfo();
		param.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		param.addOrderByFieldDesc(" t.create_date ");
		List<CmProjectInfo> dataList = this.cmProjectInfoService.queryAll(param);
		List<Department> resultList = new ArrayList<>();
		resultList.add(Department.createTreeData("0", "-1", "项目信息"));
		if (dataList != null) {
			for (CmProjectInfo entity : dataList) {
				resultList.add(Department.createTreeData(entity.getId(), "0", entity.getProjectName()));
			}
		}
		return resultList;
	}

	/**
	 * 周报树
	 * @param request
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value="weeklyTree.action")
	@ResponseBody
	public List<Department> weeklyTree(HttpServletRequest request) throws CustomException {
		CmProjectInfo param = new CmProjectInfo();
		param.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		param.addOrderByFieldDesc(" t.create_date ");
		List<CmProjectInfo> dataList = this.cmProjectInfoService.queryAll(param);
		List<Department> resultList = new ArrayList<>();
		resultList.add(Department.createTreeData("0", "-1", "周报信息"));
		if (dataList != null) {
			for (CmProjectInfo entity : dataList) {
				resultList.add(Department.createTreeData(entity.getId(), "0", entity.getProjectName(), 2));
				CmProjectWeekly weekly = new CmProjectWeekly();
				weekly.setProjectId(entity.getId());
				weekly.setIfRelease(1);
				weekly.addOrderByFieldDesc("t.create_date");
				List<CmProjectWeekly> weeklyList = this.projectWeeklyService.queryAll(weekly);
				if (weeklyList != null) {
					for (CmProjectWeekly w : weeklyList) {
						resultList.add(Department.createTreeData("weekly_" + w.getId(), entity.getId(), w.getReportName(), 3));
					}
				}
			}
		}
		return resultList;
	}

	@RequestMapping(value="getLastWeekly.action")
	@ResponseBody
	public Result getLastWeekly(HttpServletRequest request) throws CustomException {
		CmProjectWeekly weekly = new CmProjectWeekly();
		weekly.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		String projectId = request.getParameter("projectId");
		if (!StringUtil.isEmpty(projectId)) {
			weekly.setProjectId(projectId);
		}
		weekly.setIfRelease(1);
		weekly.addOrderByFieldDesc(" t.create_date ");
		weekly = GlobalUtil.getFirstItem(this.projectWeeklyService.queryAll(weekly));
		if (weekly == null) {
			return Result.failure(ResultCode.RESULE_DATA_NONE);
		}
		return Result.success(weekly);
	}

	/**
	 * 周报项目列表
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="weeklyItemList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager weeklyItemList(CmProjectWeeklyItem entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = cmProjectWeeklyItemService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	 * 保存周报事项
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveOrUpdateItem.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="周报管理",operateFuncNm="save",operateDescribe="新增操作")
	public Result saveOrUpdateItem(CmProjectWeeklyItem entity) throws Exception {
		if (!StringUtil.isEmpty(entity.getId())) {
			return this.cmProjectWeeklyItemService.updateEntity(entity);
		}
		CmProjectWeeklyItem db = this.cmProjectWeeklyItemService.getByPlanAndWeekly(entity.getWeeklyId(), entity.getPlanId());
		if (db != null) {
			entity.setId(db.getId());
			return this.cmProjectWeeklyItemService.updateEntity(entity);
		}
		return this.cmProjectWeeklyItemService.saveEntity(entity);

	}

	@RequestMapping(value = "getItem.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="周报管理",operateFuncNm="save",operateDescribe="新增操作")
	public CmProjectWeeklyItem getItem(CmProjectWeeklyItem entity) throws Exception {
		return this.cmProjectWeeklyItemService.get(entity);
	}

	@RequestMapping(value="deleteItem.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="周报管理",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteItem(CmProjectWeeklyItem entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectWeeklyItemService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}
}

