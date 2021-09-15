package com.jc.sys.web;

import com.jc.foundation.exception.CustomException;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubDepartmentService;
import com.jc.sys.service.ISubUserService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.AdminSide;
import com.jc.system.security.domain.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value = "/api/subDepartment")
public class ApiSubDepartmentController {
    private static Logger logger = Logger.getLogger(ApiSubDepartmentController.class);

    @Autowired
    private ISubDepartmentService subDepartmentService;

    @Autowired
    private ISubUserService subUserService;

    /**
     * @description 显示机构树
     * @author
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "managerDeptTree.action",method=RequestMethod.GET)
    @ResponseBody
    public List<AdminSide> managerDeptTree(HttpServletRequest request) {
        User userInfo = SystemSecurityUtils.getUser();
        return subDepartmentService.queryManagerDeptRree(userInfo);
    }

    /**
     * @description 显示人员部门树
     * @author
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllDeptAndUser.action",method=RequestMethod.GET)
    @ResponseBody
    public void getAllDeptAndUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String jsonArray = "";
        User userInfo = SystemSecurityUtils.getUser();
        jsonArray = subDepartmentService.getAllDeptAndUser(userInfo);
        sendJson(response, jsonArray);
    }

    protected void sendJson(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(content);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @param deptId
     * @return
     * @author 池海洲
     */
    @RequestMapping(value = "getUserById.action",method=RequestMethod.POST)
    @ResponseBody
    public SubUser getUserById(String id,String deptId) throws IOException {
        SubUser subUser = new SubUser();
        subUser.setId(id);
        subUser.setDeptId(deptId);
        try {
            subUser = subUserService.get(subUser);
            subUser.setDutyIdValue(subUser.getDutyId());
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
        Map<String, Object> map = SystemSecurityUtils.loginState(String.valueOf(id));
        String onLine = (String) map.get("onLine");
        if(onLine.length() > 0){
            subUser.setExtStr5("1");
        }else{
            subUser.setExtStr5("0");
        }
        return subUser;
    }

}
