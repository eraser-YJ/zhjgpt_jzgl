package com.jc.csmp.project.web;

import com.jc.archive.service.IArchiveFolderService;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.item.service.IItemClassifyService;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.csmp.project.domain.validator.CmProjectInfoValidator;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.csmp.project.vo.CmProjectInfoVo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.util.ResourceDbServer;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-项目管理Controller
 * 数据权限，项目的建设单位、监管单位、参与单位
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/info")
public class CmProjectInfoController extends BaseController{

	@Autowired
	private ICmProjectInfoService cmProjectInfoService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IDicManager dicManager;
	@Autowired
	private IItemClassifyService itemClassifyService;
	@Autowired
	private ICmProjectPersonService cmProjectPersonService;

	@Autowired
	private IArchiveFolderService folderService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectInfo")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectInfoValidator());
    }

	public CmProjectInfoController() {
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
	public Map<String,Object> save(@Valid CmProjectInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(cmProjectInfoService.saveEntity(entity), resultMap, getToken(request));
			/*创建项目文档目录*/
			Map<String,Object> infoMap = new HashMap<>();
			infoMap.put("projectName",entity.getProjectName());
			infoMap.put("projectNumber",entity.getProjectNumber());
			infoMap.put("buildDeptId",entity.getBuildDeptId());
			infoMap.put("folderType","0");//0-资料管理；5-文档管理
			try {
				folderService.saveFolder(infoMap);
			} catch (CustomException e) {
				e.printStackTrace();
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, e.getMessage());
			}
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
	public Map<String, Object> update(CmProjectInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmProjectInfoService.updateEntity(entity), resultMap, getToken(request));
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
	public CmProjectInfo get(CmProjectInfo entity) throws Exception{
		return cmProjectInfoService.get(entity);
	}

	/**
	 * 验证项目编码是否存在
	 * @param projectNumber
	 * @param deptResourceId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="checkProjectNumberAndGetDept.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public Result checkProjectNumberAndGetDept(String projectNumber, String deptResourceId) throws Exception {
		if (StringUtil.isEmpty(projectNumber)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmProjectInfo entity = new CmProjectInfo();
		entity.setProjectNumber(projectNumber);
		List<CmProjectInfo> projectList = this.cmProjectInfoService.queryAll(entity);
		if (projectList == null || projectList.size() == 0) {
			Department dept = new Department();
			dept.setResourceId(deptResourceId);
			List<Department> deptList = this.departmentService.query(dept);
			Department deptEntity = null;
			if (deptList != null && deptList.size() > 0) {
				deptEntity = deptList.get(0);
			}
			return Result.success(deptEntity);
		}
		return Result.failure(1, "项目已存在");
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
		String id = request.getParameter("id");
		String operator = request.getParameter("operator");
		operator = operator == null ? "" : operator;
		if (StringUtil.isEmpty(id)) {
			String deptId = request.getParameter("deptId");
			String resourceDataId = request.getParameter("resourceDataId");
			String canBandBuild = "false";
			String loginUserDeptId = "";
			String loginUserDeptName = "";
			if (StringUtil.isEmpty(deptId)) {
				User user = SystemSecurityUtils.getUser();
				if (user != null) {
					Department department = DeptCacheUtil.getDeptById(user.getDeptId());
					if (department != null) {
						loginUserDeptId = department.getId();
						loginUserDeptName = department.getName();
						canBandBuild = GlobalUtil.needChange(department.getCode()) + "";
					}
				}
			} else {
				//选择过来的，直接绑定
				Department department = DeptCacheUtil.getDeptById(deptId);
				if (department != null) {
					loginUserDeptId = department.getId();
					loginUserDeptName = department.getName();
					canBandBuild = GlobalUtil.needChange(department.getCode()) + "";
				}
			}
			map.put("canBandBuild", canBandBuild);
			map.put("loginUserDeptId", loginUserDeptId);
			map.put("loginUserDeptName", loginUserDeptName);
			if (!StringUtil.isEmpty(resourceDataId)) {
				map.put("resourceData", JsonUtil.java2Json(ResourceEnums.pt_project_info.getResourceService().detail(resourceDataId)));
				map.put("resourceDataId", resourceDataId);
			}
		}
		map.put("operator", operator);
		map.put("id", id);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("itemClassify", this.itemClassifyService.getByItemCode("cmProjectInfo"));
		model.addAttribute("data", map);
		if (operator != null && operator.equals("look")) {
			model.addAttribute("id", id);
			return "csmp/project/info/cmProjectInfoLook";
		}
		return "csmp/project/info/cmProjectInfoForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManagerEx<CmProjectInfoVo> manageList(CmProjectInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		if (StringUtil.isEmpty(entity.getDeptCodeCondition())) {
			entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		}
		PageManager page_ = cmProjectInfoService.query(entity, page);
		int no = page.getPage() * page.getPageRows() + 1;
		if (page.getPage() != 0) {
			no = (page.getPage() - 1) * page.getPageRows() + 1;
		}
		List<CmProjectInfoVo> dataList = new ArrayList<>();
		List<CmProjectInfo> entityList = (List<CmProjectInfo>) page_.getData();
		if (entityList != null) {
			for (CmProjectInfo project : entityList) {
				CmProjectInfoVo vo = new CmProjectInfoVo();
				vo.setId(project.getId());
				vo.setBuildDeptIdValue(project.getBuildDeptIdValue());
				vo.setProjectTypeValue(project.getProjectTypeValue());
				vo.setRegionValue(project.getRegionValue());
				vo.setProjectName(project.getProjectName());
				vo.setProjectNumber(project.getProjectNumber());
				vo.setLandArea(project.getLandArea());
				vo.setInvestmentAmount(project.getInvestmentAmount());
				vo.setTableRowNo(no++);
				vo.setSuperviseDeptIdValue(project.getSuperviseDeptIdValue());
				dataList.add(vo);
			}
		}

		return new PageManagerEx(page_, dataList, page.getPageRows());
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
		return "csmp/project/info/cmProjectInfoList";
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="change.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String change(HttpServletRequest request) throws Exception {
		String deptId = request.getParameter("deptId");
		if (!StringUtil.isEmpty(deptId)) {
			String code = DeptCacheUtil.getCodeById(deptId);
			if (!StringUtil.isEmpty(code)) {
				request.setAttribute("deptId", code);
			}
		}
		return "csmp/project/info/cmProjectInfoChange";
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
	public  Map<String, Object> deleteByIds(CmProjectInfo entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectInfoService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value = "checkProjectNumber.action")
	@ResponseBody
	public boolean checkProjectNumber(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String projectNumber = request.getParameter("projectNumber");
		Result result = cmProjectInfoService.checkProjectNumber(id, projectNumber);
		return result.getCode().intValue() == ResultCode.SUCCESS.code().intValue() ? true : false;
	}

	@RequestMapping(value = "projectTree.action")
	@ResponseBody
	public List<Department> projectTree(HttpServletRequest request) {
		List<Dic> dicList = this.dicManager.getDicsByTypeCode(DicKeyEnum.region.getTypeCode(), DicKeyEnum.region.getParentCode());
		List<Department> resultList = new ArrayList<>();
		if (dicList != null) {
			for (Dic dic : dicList) {
				resultList.add(Department.createTreeData("region_" + dic.getCode(), "0", dic.getName()));
			}
		}
		return resultList;
	}

	@RequestMapping(value = "importExcel.action")
	public void importExcel(@RequestParam(value = "importFileExcel") MultipartFile excelFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		System.out.println(excelFile.getOriginalFilename());
		Writer writer = response.getWriter();
		writer.write("{\"success\":\"true\",\"message\":\"aaa\"}");
		writer.flush();
		writer.close();
	}

	@RequestMapping(value="module.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String module() throws Exception{
		return "csmp/project/info/module";
	}

	/**
	 * 项目驾驶舱总体统计
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "projectCockpit.action")
	@ResponseBody
	public Object projectCockpit(String projectId) {
		Map<String, Object> resultMap = new HashMap<>(3);
		CmProjectInfo project = this.cmProjectInfoService.getById(projectId);
		List<CmProjectPerson> personList = this.cmProjectPersonService.getByProjectId(projectId);
		int companyCount = personList == null ? 0 : personList.size();
		companyCount = (companyCount - 1) < 0 ? 0 : (companyCount - 1);
		resultMap.put("project", project);
		resultMap.put("companyCount", companyCount);
		resultMap.put("personCount", ResourceDbServer.getInstance().getPersonCountByProject(project.getProjectNumber()));
		return resultMap;
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author  limin  li
	 * @version  2020-09-30
	 */
	@RequestMapping(value="changeSafety.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String changeSafety(HttpServletRequest request) throws Exception {
		String deptId = request.getParameter("deptId");
		if (!StringUtil.isEmpty(deptId)) {
			String code = DeptCacheUtil.getCodeById(deptId);
			if (!StringUtil.isEmpty(code)) {
				request.setAttribute("deptId", code);
			}
		}
		return "csmp/safe/common/cmProjectInfoChangeSafety";
	}
	/**
	 * 根据未发起报监申请的项目
	 * @param entity
	 * @param page
	 * @author  limin  li
	 * @version  2020-09-30
	 * @return
	 */
	@RequestMapping(value="manageSafetyList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManagerEx<CmProjectInfoVo> manageSafetyList(CmProjectInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		if (StringUtil.isEmpty(entity.getDeptCodeCondition())) {
			entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		}
		PageManager page_ = cmProjectInfoService.queryProjectSafety(entity, page);
		int no = page.getPage() * page.getPageRows() + 1;
		if (page.getPage() != 0) {
			no = (page.getPage() - 1) * page.getPageRows() + 1;
		}
		List<CmProjectInfoVo> dataList = new ArrayList<>();
		List<CmProjectInfo> entityList = (List<CmProjectInfo>) page_.getData();
		if (entityList != null) {
			for (CmProjectInfo project : entityList) {
				CmProjectInfoVo vo = new CmProjectInfoVo();
				vo.setId(project.getId());
				vo.setBuildDeptIdValue(project.getBuildDeptIdValue());
				vo.setProjectTypeValue(project.getProjectTypeValue());
				vo.setRegionValue(project.getRegionValue());
				vo.setProjectName(project.getProjectName());
				vo.setProjectNumber(project.getProjectNumber());
				vo.setLandArea(project.getLandArea());
				vo.setInvestmentAmount(project.getInvestmentAmount());
				vo.setTableRowNo(no++);
				vo.setSuperviseDeptIdValue(project.getSuperviseDeptIdValue());
				dataList.add(vo);
			}
		}

		return new PageManagerEx(page_, dataList, page.getPageRows());
	}

	@RequestMapping(value="detailTab.action",method=RequestMethod.GET)
	public String detailTab(HttpServletRequest request) {
		String id = request.getParameter("id");
		String page = request.getParameter("p");
		if (page == null) {
			page = "detail";
		}
		if (id != null) {
			CmProjectInfo projectInfo = this.cmProjectInfoService.getById(id);
			if (projectInfo != null) {
				request.setAttribute("projectNumber", projectInfo.getProjectNumber());
			}
		}
		request.setAttribute("id", id);
		return "csmp/project/info/detail/" + page;
	}
}