package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.api.util.ApiServiceUtil;
import com.jc.system.security.beans.DeptTree;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/department")
public class ApiDepartmentController {

    private static final String ROLE_ORG_ID = "roleOrgId";
    private static final String SYS_ORG_VAL = "[0-9]+";
    private static final String SYS_ORG_ID = "sys.orgid";

    @Autowired
    private IDepartmentService departmentService;
    
    public ApiDepartmentController() {
    }

    /**
     * 获取全部部门及人员
     *
     * @return
     */
    @RequestMapping(value = "getAllDeptAndUser.action",method=RequestMethod.GET)
    public void getAllDeptAndUser(String unique,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Object orgId = request.getSession().getAttribute(ROLE_ORG_ID);
        if (unique != null && !"".equals(unique)) {
            UserUtils.setUnique("unique",unique);
        }
        String jsonArray = "";
        if (orgId != null && !"".equals(orgId.toString())){
            boolean result=orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getAllDeptAndUser(orgId.toString());
            }
        } else if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))){
            boolean result=GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getAllDeptAndUser(GlobalContext.getProperty(SYS_ORG_ID));
            }
        } else {
            jsonArray = departmentService.getAllDeptAndUser();
        }
        sendJson(response, jsonArray);
    }

    /**
     * 根据登录人所在机构查询组织树
     * @return
     */
    @RequestMapping(value = "getOrgTree.action",method=RequestMethod.GET)
    @ResponseBody
    public List<Department> getOrgTree(HttpServletRequest request) throws CustomException {
        Object orgId = request.getSession().getAttribute(ROLE_ORG_ID);
        if (orgId != null && !"".equals(orgId.toString())) {
            boolean result = orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                return departmentService.getOrgTree(orgId.toString());
            } else {
                return departmentService.getOrgTree();
            }
        } else if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))){
            boolean result=GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                return departmentService.getOrgTree(GlobalContext.getProperty(SYS_ORG_ID));
            } else {
                return departmentService.getOrgTree();
            }
        } else {
            return departmentService.getOrgTree();
        }
    }

    /**
     * 查询整个机构组织树不包含部门
     * @return
     */
    @RequestMapping(value = "getAllOrgNoDeptTree.action",method=RequestMethod.GET)
    @ResponseBody
    public List<Department> getAllOrgNoDeptTree(HttpServletRequest request) throws CustomException {
        return departmentService.getAllOrgNoDeptTree();
    }



    /**
     * 获取全部部门及人员
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getDeptAndUserByOnLine.action",method=RequestMethod.POST)
    @ResponseBody
    public void getDeptAndUserByOnLine(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Object orgId = request.getSession().getAttribute(ROLE_ORG_ID);
        String jsonArray = "";
        if (orgId != null && !"".equals(orgId.toString())) {
            boolean result = orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getDeptAndUserByOnLine(orgId.toString());
            }
        } else  if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))) {
            boolean result = GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getDeptAndUserByOnLine(GlobalContext.getProperty(SYS_ORG_ID));
            }
        } else {
            jsonArray = departmentService.getDeptAndUserByOnLine();
        }
        sendJson(response, jsonArray);
    }

    /**
     * 获取全部部门及人员
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getDeptAndUserByOnLine.action",method=RequestMethod.GET)
    @ResponseBody
    public void getDeptAndUserByOnLineGet(HttpServletRequest request,HttpServletResponse response) throws Exception {
        getDeptAndUserByOnLine(request,response);
    }

    /**
     * 获取职务人员
     * @throws Exception
     */
    @RequestMapping(value = "getPostAndUser.action",method=RequestMethod.GET)
    @ResponseBody
    public void getPostAndUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Object orgId = request.getSession().getAttribute(ROLE_ORG_ID);
        String jsonArray = "";
        if (orgId != null && !"".equals(orgId.toString())) {
            boolean result = orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getPostAndUser(orgId.toString());
            }
        } else  if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))) {
            boolean result = GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                jsonArray = departmentService.getPostAndUser(GlobalContext.getProperty(SYS_ORG_ID));
            }
        } else {
            jsonArray = departmentService.getPostAndUser();
        }
        sendJson(response, jsonArray);
    }

    /**
     * 获取个人组别
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getPersonGroupAndUser.action",method=RequestMethod.GET)
    @ResponseBody
    public void getPersonGroupAndUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String jsonArray = departmentService.getPersonGroupAndUser();
        sendJson(response, jsonArray);
    }

    /**
     * 获取公共组别
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getPublicGroupAndUser.action",method=RequestMethod.GET)
    @ResponseBody
    public void getPublicGroupAndUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String jsonArray = departmentService.getPublicGroupAndUser();
        sendJson(response, jsonArray);
    }

    /**
     * 根据登录人所在机构查询组织树
     * @return
     * @throws CustomException
     */
    @RequestMapping(value = "getOrgAndPersonTree.action",method=RequestMethod.GET)
    @ResponseBody
    public List<DeptTree> getOrgAndPersonTree(HttpServletRequest request) throws CustomException {
        Object orgId = request.getSession().getAttribute(ROLE_ORG_ID);
        List<DeptTree> deptlist = null;
        if (orgId != null && !"".equals(orgId.toString())) {
            boolean result = orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                deptlist = departmentService.getOrgAndPersonDeptTree(orgId.toString());
            }
        } else  if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))) {
            boolean result = GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                deptlist = departmentService.getOrgAndPersonDeptTree(GlobalContext.getProperty(SYS_ORG_ID));
            }
        } else {
            deptlist =  departmentService.getOrgAndPersonDeptTree("");
        }
        return deptlist;
    }

    /**
     * 根据部门id获取直属下级的部门及部门人员集合
     * @param department
     * @return
     */
    @RequestMapping(value = "getSubChildDeptAndUserByDeptId.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSubChildDeptAndUserByDeptId(Department department){
        Map<String, Object> result = new HashMap<String, Object>();
        if(department == null || "".equals(department.getId())){
            result.put("code", "200000");
            result.put("errormsg", MessageUtils.getMessage("JC_SYS_131"));
            result.put("body", null);
            return result;
        }

        List<Department> body = departmentService.getSubChildDeptAndUserByDeptId(department.getId());
        result.put("code", "000000");
        result.put("errormsg", "");
        result.put("body", body);
        return result;
    }

    /**
     * 根据部门id获取部门人员集合及直属下级的部门集合及部门所有人员个数
     * @param request
     * @param department
     * @return
     */
    @RequestMapping(value = "getUserAndSubChildDeptByDeptId.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserAndSubChildDeptByDeptId(HttpServletRequest request, Department department){
        Map<String, Object> result = new HashMap<String, Object>();
        if(department == null || "".equals(department.getId())){
            result.put("code", "200000");
            result.put("errormsg", MessageUtils.getMessage("JC_SYS_131"));
            result.put("body", null);
            return result;
        }

        try {
        	Map<String, Object> body =  departmentService.getUserAndSubChildDeptByDeptId(department.getId());
        	List<User> users = (List<User>)body.get("users");
        	for (User user : users) {
        		user.setPhoto(ApiServiceUtil.getUserPhotoAbsolutePath(request, user.getPhoto()));
			}
        	
        	result.put("code", "000000");
			result.put("errormsg", "");
			result.put("body", body);
		} catch (NullPointerException e) {
			result.put("code", "200000");
            result.put("errormsg", e.getMessage());
            result.put("body", null);
		}
        
        return result;
    }

    /**
     * 根据部门id获取部门所有人员集合
     * @param request
     * @param department
     * @return
     */
    @RequestMapping(value = "getAllUserByDeptId.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllUserByDeptId(HttpServletRequest request, Department department){
        Map<String, Object> result = new HashMap<>();
        if(department == null || "".equals(department.getId())){
            result.put("code", "200000");
            result.put("errormsg", MessageUtils.getMessage("JC_SYS_131"));
            result.put("body", null);
            return result;
        }
        try{
        	List<User> body = departmentService.getAllUsersByDeptId(department.getId());
        	for (User user : body) {
        		user.setPhoto(ApiServiceUtil.getUserPhotoAbsolutePath(request, user.getPhoto()));
			}
            result.put("code", "000000");
            result.put("errormsg", "");
            result.put("body", body);
        } catch (NullPointerException e) {
        	result.put("code", "200000");
            result.put("errormsg", e.getMessage());
            result.put("body", null);
		}
        
        return result;
    }

    /**
     * 根据部门id获取所有子级部门及部门人员集合
     * @param department
     * @return
     */
    @RequestMapping(value = "getAllChildDeptAndUserByDeptId.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllChildDeptAndUserByDeptId(Department department){
        Map<String, Object> result = new HashMap<String, Object>();
        if(department == null || "".equals(department.getId())){
            result.put("code", "200000");
            result.put("errormsg", MessageUtils.getMessage("JC_SYS_131"));
            result.put("body", null);
            return result;
        }
        try {
        	List<Department> body = departmentService.getAllChildDeptAndUserByDeptId(department.getId());
        	result.put("code", "000000");
            result.put("errormsg", "");
            result.put("body", body);
		} catch (NullPointerException e) {
			result.put("code", "200000");
	        result.put("errormsg", e.getMessage());
	        result.put("body", null);
		}
        return result;
    }

    /**
     * 获得所有部门集合
     * @return
     */
    @RequestMapping(value = "getAllDepartment.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllDepartment(){
        Map<String, Object> result = new HashMap<>();
		try {
            List<Department> body = departmentService.queryAll(new Department());
			result.put("code", "000000");
	        result.put("errormsg", "");
	        result.put("body", body);
		} catch (CustomException e) {
			result.put("code", "200000");
	        result.put("errormsg", e.getMessage());
	        result.put("body", null);
		}
        return result;
    }

    protected void sendJson(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(content);
        response.getWriter().flush();
        response.getWriter().close();
    }

}