package com.jc.system.security.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.event.DepartmentAddEvent;
import com.jc.system.event.DepartmentDeleteEvent;
import com.jc.system.event.DepartmentLogicDeleteEvent;
import com.jc.system.event.DepartmentUpdateEvent;
import com.jc.system.security.DepartmentConstant;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.DepartmentValidator;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;

/**
 * 组织控制类
 * @author Administrator
 * @date 2020-06-29
 */
@Controller
@RequestMapping(value = "/department")
public class DepartmentController extends BaseController {
	@Autowired
	private IDepartmentService departmentService;

	@org.springframework.web.bind.annotation.InitBinder("department")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new DepartmentValidator());
	}

	public DepartmentController() {
	}

	/**
	 * 分页查询方法[单独查询部门]
	 * @param department
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="查询组织",operateFuncNm="manageList",operateDescribe="机构或部门查询操作")
	public PageManager manageList(Department department, final PageManager page, HttpServletRequest request) {
		PageManager rePage = departmentService.query(department, page);
		return rePage;
	}

	/**
	 * 分页查询方法[用户查询]
	 * @param user
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "userManageList.action")
	@ResponseBody
	public PageManager userManageList(User user, final PageManager page) {
		if(StringUtils.isEmpty(user.getOrderBy())) {
			user.addOrderByField("t.ORDER_NO");
		}
		return departmentService.userManageList(user, page);
	}

	/**
	 * 用户查询
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "searchUserList.action", method = RequestMethod.GET)
	@ResponseBody
	public List<User> searchUserList() {
		return departmentService.searchUserList();
	}

	/**
	 * 查询分页查询方法[关联用户表查询]
	 * @param department
	 * @param page
	 * @param request
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "searchManageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="查询组织",operateFuncNm="searchManageList",operateDescribe="机构或部门查询操作")
	public PageManager searchManageList(Department department, final PageManager page, HttpServletRequest request) throws CustomException {
		if(StringUtils.isEmpty(department.getOrderBy())) {
			department.addOrderByField("t.QUEUE");
			department.addOrderByField("t.ID");
		}
		PageManager rePage = departmentService.searchQuery(department, page);
		return rePage;
	}

	@RequestMapping(value = "deptTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> deptTree(Department department) throws CustomException {
		Department dept = new Department();
		dept.setDeleteFlag(0);
		if (department.getDeptType() != null) {
            dept.setDeptType(department.getDeptType());
        }
		List<Department> treeList = departmentService.query(dept);
		return treeList;
	}

	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="机构设置",operateFuncNm="manage",operateDescribe="对机构设置进行访问")
	public String manage(Model model, HttpServletRequest request) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()){
				menuUtil.saveMenuID("/department/manage.action",request);
				return "sys/department/departmentUser";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 显示组织树-带人员名称
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryDeptTree.action", method = RequestMethod.GET)
	@ResponseBody
	public List<Department> queryDeptTree() throws Exception {
		Department department = new Department();
		department.setDeleteFlag(0);
		return departmentService.queryTree(department);
	}

	/**
	 * 删除方法
	 * @param department
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="组织删除",operateFuncNm="deleteByIds",operateDescribe="机构或部门删除操作")
	public Integer deleteByIds(Department department, String ids, HttpServletRequest request) throws Exception {
		department.setPrimaryKeys(ids.split(","));
		SpringContextHolder.getApplicationContext().publishEvent(new DepartmentDeleteEvent(department));
		departmentService.deleteByIds(department);
		return 1;
	}

	/**
	 * 逻辑删除部门-[删除关联关系adminSide]
	 * @param department
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "logicDeleteById.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="组织删除",operateFuncNm="logicDeleteById",operateDescribe="机构或部门删除操作")
	public Map<String, Object> logicDeleteById(Department department, HttpServletRequest request) throws Exception {
		Map<String, Object> result = departmentService.logicDeleteById(department);
		SpringContextHolder.getApplicationContext().publishEvent(new DepartmentLogicDeleteEvent(department));
		return result;
	}

	/**
	 * 保存方法
	 * @param department
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="添加组织",operateFuncNm="save",operateDescribe="添加机构或部门操作")
	public Map<String, Object> save(@Valid Department department, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		User userInfo = SystemSecurityUtils.getUser();
		if (department.getParentId() == null) {
			resultMap.put("errorMessage", "数据异常");
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		}
		// 校验同一级部门或机构名称是否存在
		Department dept = new Department();
		dept.setName(department.getName());
		dept.setParentId(department.getParentId());
		if (departmentService.get(dept) != null) {
			resultMap.put("errorMessage", "同级组织已存在");
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		}
		Department parent = this.departmentService.getById(department.getParentId());
		if (parent != null) {
			department.setCode(parent.getCode() + department.getCode());
		}
		Department checkCode = this.departmentService.getByCode(department.getCode());
		if (checkCode != null) {
            resultMap.put("errorMessage", "编码已存在");
            resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
            return resultMap;
        }
		department.setDeleteFlag(0);
		department.setCreateDate(DateUtils.getSysDate());
		department.setCreateUser(userInfo.getCreateUser());
		department.setModifyUser(userInfo.getCreateUser());
		department.setModifyDate(DateUtils.getSysDate());
		if (department.getQueue() == null) {
			department.setQueue(50);
		}
		if (departmentService.save(department) >= 1) {
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			Department department2 = departmentService.queryOne(department);
			resultMap.put("dept", department2);
			SpringContextHolder.getApplicationContext().publishEvent(new DepartmentAddEvent(department2));
		} else {
			resultMap.put("success", "false");
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param department
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="修改组织",operateFuncNm="update",operateDescribe="修改机构或部门操作")
	public Map<String, Object> update(Department department, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String name = departmentService.queryOne(department).getName();
		Department dept = new Department();
		dept.setName(department.getName());
		dept.setParentId(department.getParentId());
		List<Department> deptList = departmentService.queryAll(dept);
		if (deptList != null && deptList.size() > 0){
			if (deptList.size() == 1){
				Department d = deptList.get(0);
				if (d != null) {
					if(!name.equals(d.getName())){
						resultMap.put("errorMessage", "同级组织已存在");
						return resultMap;
					}
				}
			} else {
				resultMap.put("errorMessage", "同级组织已存在");
				return resultMap;
			}
		}
		department.setModifyUser(SystemSecurityUtils.getUser().getModifyUser());
		department.setModifyDate(DateUtils.getSysDate());
		if (department.getLeaderId() == null) {
			department.setLeaderId("0");
		}
		Department parent = this.departmentService.getById(department.getParentId());
		if (parent != null) {
			department.setCode(parent.getCode() + department.getCode());
		}
		department.setCode(null);
        if (departmentService.update(department) >= 1) {
			resultMap.put("success", "true");
			resultMap.put("dept", department);
			String token = getToken(request);
			resultMap.put(GlobalContext.SESSION_TOKEN, token);
			SpringContextHolder.getApplicationContext().publishEvent(new DepartmentUpdateEvent(department));
		} else {
			resultMap.put("success", "false");
		}
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param department
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	public Department get(Department department) throws Exception {
		return departmentService.get(department);
	}

	/**
	 * 获取部门单条记录方法(带父节点部门)
	 * @param model
	 * @param request
	 * @param department
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryOne.action",method=RequestMethod.POST)
	@ResponseBody
	public Department queryOne(Model model, HttpServletRequest request, Department department) throws Exception {
		Department reDepartment = departmentService.queryOne(department);
		if (reDepartment.getUserDelFlag() == 1) {
			reDepartment.setLeaderId(null);
		}
		model.addAttribute("token", getToken(request));
		return reDepartment;
	}

	/**
	 * 获取全部部门及人员
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getAllDeptAndUser.action",method=RequestMethod.GET)
	@ResponseBody
	public void getAllDeptAndUser(HttpServletResponse response) throws Exception {
		String jsonArray = departmentService.getAllDeptAndUser();
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 获取全部部门及人员
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getDeptAndUserByOnLine.action",method=RequestMethod.GET)
	@ResponseBody
	public void getDeptAndUserByOnLine(HttpServletResponse response) throws Exception {
		String jsonArray = departmentService.getDeptAndUserByOnLine();
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 获取职务人员
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getPostAndUser.action",method=RequestMethod.GET)
	@ResponseBody
	public void getPostAndUser(HttpServletResponse response) throws Exception {
		String jsonArray = departmentService.getPostAndUser();
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 获取个人组别
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getPersonGroupAndUser.action",method=RequestMethod.GET)
	@ResponseBody
	public void getPersonGroupAndUser(HttpServletResponse response) throws Exception {
		String jsonArray = departmentService.getPersonGroupAndUser();
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 获取公共组别
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "getPublicGroupAndUser.action",method=RequestMethod.GET)
	@ResponseBody
	public void getPublicGroupAndUser(HttpServletResponse response) throws Exception {
		String jsonArray = departmentService.getPublicGroupAndUser();
		response.setContentType("application/javascript; charset=UTF-8");
		response.getWriter().print(jsonArray);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 查询机构树
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "orgTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> orgTree() throws CustomException {
		Department department = new Department();
		department.setDeleteFlag(0);
		department.setDeptType(1);
		return departmentService.queryOrgTree(department);
	}

	/**
	 * 显示添加层
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "showDeptInsertHtml.action",method=RequestMethod.GET)
	public String showDeptInsertHtml(Model model, HttpServletRequest request) {
		model.addAttribute("token", getToken(request));
		return "sys/department/departmentUserInsert";
	}

	/**
	 * 显示编辑层
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "showDeptEditHtml.action",method=RequestMethod.GET)
	public String showDeptEditHtml(Model model, HttpServletRequest request) {
		model.addAttribute("token", getToken(request));
		return "sys/department/departmentUserEdit";
	}

	/**
	 * 根据登录人所在机构查询组织树
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "getOrgTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getOrgTree() throws CustomException {
		return departmentService.getOrgTree();
	}

	/**
	 * 查询整个机构组织树不包含部门
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "getAllOrgNoDeptTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getAllOrgNoDeptTree() {
		return departmentService.getAllOrgNoDeptTree();
	}

	/**
	 * 根据登录人所在机构查询组织树
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "getOrgAndPersonTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getOrgAndPersonTree() throws CustomException {
		return departmentService.getOrgAndPersonTree();
	}

	/**
	 * 根据部门id查询本级及以上所有直系父级部门及父级同级组织集合（包含机构和部门）
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "getAllParentDeptAndBrotherDeptByDeptId.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getAllParentDeptAndBrotherDeptByDeptId(Department department){
		return departmentService.getAllParentDeptAndBrotherDeptByDeptId(department.getId());
	}

	/**
	 * 根据部门id查询本级及以下所有子级组织集合（包含机构和部门）
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "getAllChildDeptAndBrohterDeptByDeptId.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getAllChildDeptAndBrohterDeptByDeptId(Department department){
		return departmentService.getAllChildDeptAndBrohterDeptByDeptId(department.getId());
	}

	/**
	 * 根据部门id获取直属下级的部门集合
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "getSubChildDeptByDeptId.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getSubChildDeptByDeptId(Department department){
		return departmentService.getSubChildDeptAndUserByDeptId(department.getId());
	}

	/**
	 * 根据部门id查询本级部门所有人员数据集
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "getSameLevelDeptAndUser.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Department> getSameLevelDeptAndUser(Department department) {
		return departmentService.getSameLevelDeptAndUserByDeptId(department.getId());
	}

}