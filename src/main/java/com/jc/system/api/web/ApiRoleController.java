package com.jc.system.api.web;

import com.jc.system.security.domain.Role;
import com.jc.system.security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/role")
public class ApiRoleController {
    @Autowired
    private IRoleService roleService;

    public ApiRoleController(){

    }

    /**
     * @description 获得部门及部门下所有角色集合
     * @param response
     */
    @RequestMapping(value = "getAllDeptAndRole.action",method=RequestMethod.GET)
    public void getAllDeptAndRole(HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(roleService.getAllDeptAndRole().toString());
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping(value = "getAllRole.action",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllRole() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Role> body = roleService.getAllRole();
        result.put("code", "000000");
        result.put("errormsg", "");
        result.put("body", body);
        return result;
    }

}
