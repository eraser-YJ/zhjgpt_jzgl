package com.jc.system.sys.web;

import com.jc.system.sys.service.IApiPinDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 部门拼音
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/pinDepartment")
public class ApiPinDepartmentController {
    @Autowired
    private IApiPinDepartmentService apiPinDepartmentService;

    public ApiPinDepartmentController() {
    }

    /**
     * 通用获取全部部门及人员可进行拼音检索
     * @param orgId
     * @param dutyId
     * @param tabType
     * @param spellType
     * @param search
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getAllDeptAndUser.action",method=RequestMethod.GET)
    public void getAllDeptAndUser(String orgId,String dutyId,String tabType,String spellType,String search, HttpServletRequest request, HttpServletResponse response) throws Exception {
        sendJson(response, apiPinDepartmentService.getAllDeptAndUser(orgId,dutyId,tabType,spellType,search,request.getParameter("weight")));
    }

    /**
     * 通用弹出树检索功能组装树
     * @param search
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getSelDeptAndUser.action",method=RequestMethod.GET)
    public void getSelDeptAndUser(String search,HttpServletRequest request, HttpServletResponse response) throws Exception {
        sendJson(response, apiPinDepartmentService.getSelDeptAndUser(search,request.getParameter("weight")));
    }

    /**
     * 通用获取人员名称进行检索
     * @param search
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getSelUsers.action",method=RequestMethod.GET)
    public void getSelUsers(String search,HttpServletRequest request, HttpServletResponse response) throws Exception {
        sendJson(response, apiPinDepartmentService.getSelUsers(search,request.getParameter("weight")));
    }

    protected void sendJson(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(content);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
