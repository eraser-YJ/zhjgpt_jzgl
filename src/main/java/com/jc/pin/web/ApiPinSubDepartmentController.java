package com.jc.pin.web;

import com.jc.pin.service.IApiPinSubDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value = "/api/pinSubDepartment")
public class ApiPinSubDepartmentController {

    private static final String WEIGHT = "weight";

    @Autowired
    private IApiPinSubDepartmentService apiPinDepartmentService;

    public ApiPinSubDepartmentController() {
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
        sendJson(response, apiPinDepartmentService.getAllDeptAndUser(orgId,dutyId,tabType,spellType,search,request.getParameter(WEIGHT)));
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
        sendJson(response, apiPinDepartmentService.getSelDeptAndUser(search,request.getParameter(WEIGHT)));
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
        sendJson(response, apiPinDepartmentService.getSelUsers(search,request.getParameter(WEIGHT)));
    }

    protected void sendJson(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(content);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
